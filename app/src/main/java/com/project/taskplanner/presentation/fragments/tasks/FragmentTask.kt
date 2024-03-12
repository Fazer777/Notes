package com.project.taskplanner.presentation.fragments.tasks

import android.app.Activity
import android.content.Intent
import android.os.Build
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
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.project.domain.models.SubTaskInterim
import com.project.domain.models.TaskInterim
import com.project.taskplanner.R
import com.project.taskplanner.databinding.FragmentTaskBinding
import com.project.taskplanner.presentation.activities.task.EditTaskActivity
import com.project.taskplanner.presentation.adapters.task.RecyclerViewTaskAdapter
import com.project.taskplanner.presentation.viewmodels.tasks.FragmentTaskVM
import com.project.taskplanner.presentation.viewmodels.tasks.subtask.SubtaskViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.Serializable
import java.time.LocalDate

class FragmentTask : Fragment() {

    private lateinit var binding: FragmentTaskBinding
    private val taskAdapter = RecyclerViewTaskAdapter()
    private var createTask: ActivityResultLauncher<Intent>? = null
    private var updateTask: ActivityResultLauncher<Intent>? = null

    private val viewModelTask by viewModel<FragmentTaskVM>()
    private val viewModelSubtask by viewModel<SubtaskViewModel>()

    private var positionSelectedItem = 0
    private var gridLayoutMenuItem: MenuItem? = null
    private var linearLayoutMenuItem: MenuItem? = null

    private var layoutEnum: LayoutEnum = LayoutEnum.LINEAR

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

        CoroutineScope(Dispatchers.Main).launch {
            val taskList = viewModelTask.getTasks()
            val subtaskList = viewModelSubtask.getSubtasks()

            taskList.forEach { task ->
                val list = mutableListOf<SubTaskInterim>()
                subtaskList.forEach { subtask ->
                    if (subtask.taskId == task.id) {
                        list.add(subtask)
                    }
                }
                task.subTasks = list
            }

            taskAdapter.setAdapterList(taskList)
            showImageEmpty()
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
                            StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
                        layoutEnum = LayoutEnum.GRID
                        showLayoutMenuButtons()
                        taskAdapter.updateLayout(layoutEnum)
                    }

                    R.id.set_linear_layout -> {
                        binding.recyclerViewTask.layoutManager =
                            LinearLayoutManager(requireContext())
                        layoutEnum = LayoutEnum.LINEAR
                        showLayoutMenuButtons()
                        taskAdapter.updateLayout(layoutEnum)
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
                val intentResult = result.data
                intentResult?.let { intent ->
                    val createdTask = getSerializable(
                        intent,
                        resources.getString(R.string.INTENT_CREATE_TASK),
                        TaskInterim::class.java
                    )

                    CoroutineScope(Dispatchers.Main).launch {
                        createdTask.id =
                            viewModelTask.onAddTaskButtonClicked(createdTask = createdTask)
                        val listId = viewModelSubtask.onAddTaskButtonClicked(
                            createdTask.id,
                            createdTask.subTasks
                        )
                        createdTask.subTasks?.let { list ->
                            listId?.let { listId ->
                                for (i in 0..listId.lastIndex) {
                                    list[i].taskId = createdTask.id
                                    list[i].id = listId[i]
                                }
                            }
                        }
                        taskAdapter.addTask(createdTask)
                        showImageEmpty()
                    }
                }
            }
        }

        updateTask = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intentResult = result.data
                intentResult?.let { intent ->
                    val updatedTask = getSerializable(
                        intent,
                        resources.getString(R.string.INTENT_UPDATE_TASK),
                        TaskInterim::class.java
                    )

                    CoroutineScope(Dispatchers.Main).launch {
                        viewModelTask.onUpdateTaskButtonClicked(updatedTask = updatedTask)
                        taskAdapter.updateTask(positionSelectedItem, updatedTask)
                    }
                }
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
                positionSelectedItem = position
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
                val flag = taskAdapter.getItem(position).isChecked
                popupMenu.inflate(
                    if (flag) R.menu.pop_up_menu_uncheck
                    else R.menu.pop_up_menu_check
                )

                popupMenu.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.action_check -> {
                            updateTaskFlag(
                                taskId = taskAdapter.getItem(position).id,
                                flag = !flag,
                                completionDate = LocalDate.now(),
                                position = position
                            )
                            return@setOnMenuItemClickListener true
                        }

                        R.id.action_uncheck -> {
                            updateTaskFlag(
                                taskId = taskAdapter.getItem(position).id,
                                flag = !flag,
                                completionDate = taskAdapter.getItem(position).completionDate!!,
                                position = position
                            )
                            return@setOnMenuItemClickListener true
                        }

                        R.id.delete_task ->{
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

    private fun initDeleteAlertDialog(position: Int){
        AlertDialog.Builder(requireContext())
            .setTitle(resources.getString(R.string.delete_selected_category))
            .setPositiveButton(resources.getString(R.string.positiveAnswer)) { dialog, which ->

                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.task_deleted),
                    Toast.LENGTH_SHORT
                ).show()

                CoroutineScope(Dispatchers.Main).launch {
                    viewModelTask.onDeleteTaskButtonClicked(
                        taskId = taskAdapter.getItem(
                            position
                        ).id
                    )
                    taskAdapter.deleteTask(position)
                    showImageEmpty()
                }
                dialog.dismiss()
            }
            .setNegativeButton(resources.getString(R.string.negativeAnswer)) { dialog, which ->
                dialog.dismiss()
            }.setCancelable(false).create().show()
    }
    private fun updateTaskFlag(
        taskId: Int,
        flag: Boolean,
        completionDate: LocalDate,
        position: Int
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            viewModelTask.onThreeDotButtonClicked(
                taskId = taskId,
                flag = flag,
                completionDate = completionDate
            )
            taskAdapter.updateTaskFlag(
                position = position,
                flag = flag,
                completionDate = completionDate
            )
        }
    }

    private fun <T : Serializable?> getSerializable(
        intent: Intent,
        key: String,
        m_class: Class<T>
    ): T {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getSerializableExtra(key, m_class)!!
        else
            intent.getSerializableExtra(key) as T
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
}