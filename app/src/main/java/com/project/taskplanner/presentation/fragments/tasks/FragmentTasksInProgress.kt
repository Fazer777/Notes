package com.project.taskplanner.presentation.fragments.tasks

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable.Orientation
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.domain.models.SubTaskInterim
import com.project.domain.models.TaskInterim
import com.project.taskplanner.R
import com.project.taskplanner.databinding.FragmentTasksInProgressBinding
import com.project.taskplanner.presentation.activities.task.AddTaskActivity
import com.project.taskplanner.presentation.adapters.task.RecyclerViewTaskAdapter
import com.project.taskplanner.presentation.viewmodels.tasks.TaskInProgressVM
import com.project.taskplanner.presentation.viewmodels.tasks.TaskInProgressViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.Serializable
import java.time.LocalDate

class FragmentTasksInProgress : Fragment() {
    companion object{
        @JvmStatic
        fun newInstance() = FragmentTasksInProgress()
    }

    private lateinit var binding : FragmentTasksInProgressBinding
    private lateinit var taskAdapter : RecyclerViewTaskAdapter
    private lateinit var createTask : ActivityResultLauncher<Intent>
    private val viewModel : TaskInProgressVM by viewModels{ TaskInProgressViewModelFactory(requireContext())}
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTasksInProgressBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskAdapter = RecyclerViewTaskAdapter(requireContext())

        createTask = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == Activity.RESULT_OK){
                val taskInterim = getSerializable(
                    result.data!!,
                    resources.getString(R.string.INTENT_CREATE_TASK),
                    TaskInterim::class.java
                )
                // I need to update this bad code
                val r = Runnable {
                    val taskDTO = viewModel.onAddBtnClicked(taskInterim)
                    taskInterim.id = taskDTO.taskId
                    taskInterim.subTasks?.let {list ->
                        for (i in 0..list.lastIndex){
                            list[i].id = taskDTO.subtaskIdList?.get(i)!!
                        }
                    }
                }
                Thread(r, "ThreadAddTask").start()
                taskAdapter.addTask(taskInterim)
            }
            else{
                Toast.makeText(requireContext(), "Canceled adding task", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.taskListLive.observe(viewLifecycleOwner){
            taskAdapter.taskList = it
        }

        initRecyclerView()
        initActions()

    }


    private fun initActions() {
        binding.floatBtnAddTask.setOnClickListener {
            createTask.launch(Intent(requireContext(), AddTaskActivity::class.java))
        }
    }

    private fun initRecyclerView() {
        binding.recyclerViewTask.adapter = taskAdapter
        binding.recyclerViewTask.layoutManager = LinearLayoutManager(requireContext())

        taskAdapter.setOnItemClickListener(object: RecyclerViewTaskAdapter.OnItemClickListener{
            override fun onItemClick(itemView: View, position: Int) {
                return
            }

            override fun onItemLongClick(itemView: View, position: Int) {
                AlertDialog.Builder(requireActivity())
                    .setTitle("Удалить выбранную задачу?")
                    .setNegativeButton("Нет") { dialogInterface: DialogInterface?, i: Int ->

                        Toast.makeText(
                            requireContext(),
                            "Удаление отменено",
                            Toast.LENGTH_SHORT
                        ).show()

                        dialogInterface?.dismiss()
                    }
                    .setPositiveButton("Да") { dialogInterface: DialogInterface, i: Int ->

                        Toast.makeText(
                            requireContext(),
                            "Задача удалена",
                            Toast.LENGTH_SHORT
                        ).show()

                        viewModel.onDelBtnClicked(taskAdapter.getItemId(position).toInt())
                        taskAdapter.deleteTask(position)
                        dialogInterface.dismiss()

                    }.setCancelable(false).create().show()
            }
        })

    }

    private fun <T : Serializable?> getSerializable(intent: Intent, key: String, m_class: Class<T>): T {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getSerializableExtra(key, m_class)!!
        else
            intent.getSerializableExtra(key) as T
    }

}