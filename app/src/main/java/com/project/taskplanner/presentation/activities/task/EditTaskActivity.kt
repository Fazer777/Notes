package com.project.taskplanner.presentation.activities.task

import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.domain.models.task.SubtaskParam
import com.project.domain.models.task.TaskParam
import com.project.taskplanner.R
import com.project.taskplanner.databinding.EditTaskActivityBinding
import com.project.taskplanner.presentation.adapters.task.RecyclerViewSubtaskAdapter
import com.project.taskplanner.presentation.dialogs.GridViewDialog
import com.project.taskplanner.presentation.viewmodels.tasks.EditTaskViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Calendar

class EditTaskActivity : AppCompatActivity() {
    private lateinit var binding : EditTaskActivityBinding
    private val viewModel : EditTaskViewModel by viewModel()
    private var subtaskAdapter = RecyclerViewSubtaskAdapter(this@EditTaskActivity)
    private lateinit var colorStringArray : Array<String>

    private var selectedColor : String = ""
    private var selectedDate : LocalDate? = null
    private var completionDate : LocalDate? = null
    private var isEditAction = false
    private var tempIdTask = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditTaskActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        colorStringArray = resources.getStringArray(R.array.color_values)
        setDefaultColor()
        initActions()
        initRecyclerview()
        getMyIntent()
    }

    private fun getMyIntent() {
        val bundle = intent.extras
        if (bundle == null){
            isEditAction = false
            binding.materialBtnAddEditTask.text = resources.getString(R.string.add_record)
        }
        else{
            isEditAction = true
            binding.materialBtnAddEditTask.text = resources.getString(R.string.edit_record)
            val taskParam = bundle.getSerializable(resources.getString(R.string.INTENT_UPDATE_TASK)) as TaskParam?
            taskParam?.let {
                tempIdTask = it.id
                setIntentData(it)
            }
        }
    }

    private fun setIntentData(taskParam: TaskParam) = with(binding) {
        checkboxTask.isChecked = taskParam.isChecked
        editTextTitleTask.setText(taskParam.title)
        editTextTaskDescription.setText(taskParam.description)
        cardViewColorTask.setCardBackgroundColor(taskParam.color)
        taskParam.subTasks.let {
            subtaskAdapter.setList(it)
        }
        textViewDateTask.text = taskParam.appointedDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
        selectedDate =taskParam.appointedDate
        completionDate = taskParam.completionDate
        selectedColor = colorIntToString(taskParam.color)
    }

    private fun setDefaultColor(){
        binding.cardViewColorTask.setCardBackgroundColor(Color.parseColor(colorStringArray[0]))
        selectedColor = colorStringArray[0]
    }

    private fun initRecyclerview() = with(binding) {
        recyclerViewSubItems.adapter = subtaskAdapter
        recyclerViewSubItems.layoutManager = LinearLayoutManager(this@EditTaskActivity)
        subtaskAdapter.setOnItemViewClickListener(object : RecyclerViewSubtaskAdapter.OnItemViewClickListener{
            override fun onCheckboxItemClick(view: View, position: Int, isChecked : Boolean) {
                subtaskAdapter.updateChecked(!isChecked, position)
            }

            override fun onDeleteButtonClick(view: View, position: Int) {
                subtaskAdapter.deleteSubtask(position)
            }
        })
    }


    private fun initActions() = with(binding){

        floatBtnAddSubtask.setOnClickListener {
            initSubtaskDialog()
        }

        materialBtnAddEditTask.setOnClickListener {
            if (selectedDate == null){
                Toast.makeText(
                    this@EditTaskActivity,
                    resources.getString(R.string.task_no_data_selected),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            createTask()
        }

        cardViewColorTask.setOnClickListener {
            initColorDialog()
        }

        checkboxTask.setOnClickListener {
            val flag = (it as CheckBox).isChecked
            completionDate = if (flag) LocalDate.now() else null
        }

        textViewDateTask.setOnClickListener {
            val calendar = Calendar.getInstance()
            val dateDialog = DatePickerDialog(
                this@EditTaskActivity,
                { view, year, monthOfYear, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, monthOfYear)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    selectedDate = LocalDate.of(year, monthOfYear+1, dayOfMonth)
                    textViewDateTask.text =  selectedDate?.format(
                        DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
                    )
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            dateDialog.setCancelable(true)
            dateDialog.show()
        }
    }

    private fun createTask() = with(binding) {

        val taskParam = TaskParam(
            id = 0,
            title = editTextTitleTask.text.toString(),
            description = editTextTaskDescription.text.toString(),
            subTasks = subtaskAdapter.getAdapterList(),
            color = Color.parseColor(selectedColor),
            appointedDate = selectedDate!!,
            completionDate = completionDate,
            isChecked = checkboxTask.isChecked
        )

        if(isEditAction){
            taskParam.id = tempIdTask
            viewModel.updateTask(updatedTask =  taskParam)
        }
        else{
            viewModel.addTask(taskParam)
        }

        setResult(RESULT_OK)
        finish()
    }

    private fun initSubtaskDialog(){
        val dialog = Dialog(this@EditTaskActivity)
        dialog.setContentView(R.layout.create_subtask_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)

        val btnCreateSubtask = dialog.findViewById<Button>(R.id.button_create_subtask)
        val editTextTitle = dialog.findViewById<EditText>(R.id.edittext_dialog_title)

        btnCreateSubtask.setOnClickListener {

            val title = editTextTitle.text.toString()
            if (title.isBlank()){
                return@setOnClickListener
            }

            val subtaskParam = SubtaskParam( title, false, 0)
            subtaskAdapter.addSubTask(subtaskParam)

            dialog.dismiss()
        }
        dialog.show()
    }

    private fun initColorDialog() {
        val colorDialog = GridViewDialog(this@EditTaskActivity, colorStringArray)
        colorDialog.setOnItemClickListener(object : GridViewDialog.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                val item : String = colorDialog.getItem(position)
                binding.cardViewColorTask.setCardBackgroundColor(Color.parseColor(item))
                selectedColor = item
                colorDialog.dismiss()
            }
        })
        colorDialog.show()
    }

    private fun colorIntToString(colorInt : Int) : String{
        return String.format("#%06X", 0xFFFFFF and colorInt)
    }
}

