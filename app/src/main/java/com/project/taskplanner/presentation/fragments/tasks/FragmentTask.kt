package com.project.taskplanner.presentation.fragments.tasks

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.project.taskplanner.R
import com.project.taskplanner.databinding.FragmentTaskBinding
import com.project.taskplanner.presentation.activities.task.EditTaskActivity
import com.project.taskplanner.presentation.adapters.task.RecyclerViewTaskAdapter
import com.project.taskplanner.presentation.viewmodels.tasks.FragmentTaskVM
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate


class FragmentTask : Fragment() {

    private lateinit var binding: FragmentTaskBinding

    private lateinit var sharedPref: SharedPreferences
    private val SHARED_PREF_NAME = "shared_preferences"
    private val LAYOUT_STATE_KEY = "layout_state"

    private val taskAdapter = RecyclerViewTaskAdapter()
    private val viewModelTask by viewModel<FragmentTaskVM>()

    private var gridLayoutMenuItem: MenuItem? = null
    private var linearLayoutMenuItem: MenuItem? = null

    private var createTask: ActivityResultLauncher<Intent>? = null
    private var updateTask: ActivityResultLauncher<Intent>? = null

    private lateinit var layoutEnum: LayoutEnum


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createToolbar()
        initOperations()
        initRecyclerView()
        initButtons()

        sharedPref = requireActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

        viewModelTask.getTasks().observe(viewLifecycleOwner) { newList ->
            taskAdapter.setList(newList = newList)
            showImageEmpty()
        }

        viewModelTask.layoutStateLive.observe(viewLifecycleOwner) { state ->
            layoutEnum = state
            showLayoutMenuButtons()
            binding.recyclerViewTask.layoutManager = getLayout()
            taskAdapter.updateLayout(state)
        }
    }

    private fun createToolbar() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.task_toolbar, menu)
                gridLayoutMenuItem = menu.findItem(R.id.set_grid_layout)
                linearLayoutMenuItem = menu.findItem(R.id.set_linear_layout)
                showLayoutMenuButtons()
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.set_grid_layout -> {
                        binding.recyclerViewTask.layoutManager =
                            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                        viewModelTask.changeLayoutState(state = LayoutEnum.GRID)
                    }

                    R.id.set_linear_layout -> {
                        binding.recyclerViewTask.layoutManager =
                            LinearLayoutManager(requireContext())
                        viewModelTask.changeLayoutState(state = LayoutEnum.LINEAR)
                    }
                }
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun initOperations() {
        createTask = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Toast.makeText(requireContext(), "Task has been added", Toast.LENGTH_SHORT).show()
            }
        }

        updateTask = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Toast.makeText(requireContext(), "Task has been updated", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initButtons() = with(binding) {
        floatBtnAddTask.setOnClickListener {
            createTask?.launch(Intent(requireActivity(), EditTaskActivity::class.java))
        }
    }

    private fun initRecyclerView() = with(binding) {
        recyclerViewTask.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewTask.adapter = taskAdapter
        taskAdapter.setOnItemClickListener(object : RecyclerViewTaskAdapter.OnItemClickListener {
            override fun onItemClick(itemView: View, position: Int) {
                val task = taskAdapter.getItem(position)
                val intent = Intent(requireActivity(), EditTaskActivity::class.java)
                intent.putExtra(resources.getString(R.string.INTENT_UPDATE_TASK), task)
                updateTask?.launch(intent)
            }

            override fun onItemLongClick(itemView: View, position: Int) {
                initDeleteAlertDialog(position)
            }

            override fun onThreeDotClick(itemView: View, position: Int) {
                val popupMenu = PopupMenu(requireContext(), itemView, Gravity.END)
                val task = taskAdapter.getItem(position)

                popupMenu.inflate(
                    if (task.isChecked) R.menu.pop_up_menu_uncheck
                    else R.menu.pop_up_menu_check
                )

                popupMenu.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.action_check -> {
                            viewModelTask.completeTask(
                                taskId = task.id,
                                isChecked = true,
                                completionDate = LocalDate.now()
                            )
                            return@setOnMenuItemClickListener true
                        }

                        R.id.action_uncheck -> {
                            viewModelTask.completeTask(
                                taskId = task.id,
                                isChecked = false,
                                completionDate = null
                            )
                            return@setOnMenuItemClickListener true
                        }

                        R.id.delete_task -> {
                            initDeleteAlertDialog(position)
                            return@setOnMenuItemClickListener true
                        }

                        else -> {
                            return@setOnMenuItemClickListener false
                        }
                    }
                }
                popupMenu.show()
            }
        })
    }

    private fun getLayout(): RecyclerView.LayoutManager {
        return when (layoutEnum) {
            LayoutEnum.LINEAR -> {
                LinearLayoutManager(requireContext())
            }

            LayoutEnum.GRID -> {
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            }
        }
    }

    private fun initDeleteAlertDialog(position: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle(resources.getString(R.string.delete_selected_category))
            .setPositiveButton(resources.getString(R.string.positiveAnswer)) { dialog, which ->

                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.task_deleted),
                    Toast.LENGTH_SHORT
                ).show()

                viewModelTask.deleteTask(taskAdapter.getItem(position))
                showImageEmpty()
                dialog.dismiss()
            }
            .setNegativeButton(resources.getString(R.string.negativeAnswer)) { dialog, which ->
                dialog.dismiss()
            }.setCancelable(false).create().show()
    }


    private fun showImageEmpty() = with(binding) {
        val isEmpty = taskAdapter.itemCount == 0
        imgViewEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
        textViewEmptyText.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    private fun showLayoutMenuButtons() {
        gridLayoutMenuItem?.isVisible = layoutEnum == LayoutEnum.LINEAR
        linearLayoutMenuItem?.isVisible = layoutEnum == LayoutEnum.GRID
    }

    override fun onResume() {
        super.onResume()

        val stateCode = sharedPref.getInt(LAYOUT_STATE_KEY, LayoutEnum.entries[0].code)
        viewModelTask.changeLayoutState(LayoutEnum.getByValue(stateCode)!!)
    }

    override fun onPause() {
        super.onPause()

        sharedPref.edit().also { it.putInt(LAYOUT_STATE_KEY, layoutEnum.code) }.apply()
    }
}