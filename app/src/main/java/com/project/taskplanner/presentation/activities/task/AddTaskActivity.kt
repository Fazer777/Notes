package com.project.taskplanner.presentation.activities.task

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.domain.models.SubTaskInterim
import com.project.domain.models.TaskInterim
import com.project.taskplanner.R
import com.project.taskplanner.databinding.AddTaskActivityBinding
import com.project.taskplanner.presentation.adapters.task.RecyclerViewSubtaskAdapter
import com.project.taskplanner.presentation.dialogs.GridViewDialog
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Calendar
import java.util.Locale

class AddTaskActivity : AppCompatActivity() {
    private lateinit var binding : AddTaskActivityBinding
    private var adapter = RecyclerViewSubtaskAdapter(this@AddTaskActivity)
    private val subtaskList = ArrayList<SubTaskInterim>()
    private lateinit var colorStringArray : Array<String>

    private var selectedColor : String = ""
    private var selectedDate : LocalDate? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddTaskActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        colorStringArray = resources.getStringArray(R.array.color_values)

        setDefaultColor()
        initActions()
        initRecyclerview()
    }

    private fun setDefaultColor(){
        binding.cardViewColorTask.setCardBackgroundColor(Color.parseColor(colorStringArray[0]))
        selectedColor = colorStringArray[0]
    }

    private fun initRecyclerview() = with(binding) {
        recyclerViewSubItems.adapter = adapter
        recyclerViewSubItems.layoutManager = LinearLayoutManager(this@AddTaskActivity)
        adapter.setOnItemViewClickListener(object : RecyclerViewSubtaskAdapter.OnItemViewClickListener{
            override fun onCheckboxItemClick(view: View, position: Int, isChecked : Boolean) {
                adapter.updateChecked(!isChecked, position)
                // You can remove this line and
                // just take subTaskList from adapter for create task
                updateCheckedSubtask(!isChecked, position)
            }

            override fun onDeleteButtonClick(view: View, position: Int) {
                adapter.deleteSubtask(position)
            }
        })
    }

    private fun updateCheckedSubtask(value : Boolean, position : Int){
        subtaskList[position].isChecked = value
    }

    private fun initActions() = with(binding){

        binding.imageButtonAddSubtask.setOnClickListener {
            initSubtaskDialog()
        }

        binding.subtaskMaterialBtnAdd.setOnClickListener {
            if (selectedDate == null){
                Toast.makeText(
                    this@AddTaskActivity,
                    "Не выбрана дата выполнения задачи",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            createTask()
        }

        binding.cardViewColorTask.setOnClickListener {
            initColorDialog()
        }

        binding.textViewDateTask.setOnClickListener {
            val calendar = Calendar.getInstance()
            val dateDialog = DatePickerDialog(
                this@AddTaskActivity,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
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

        val taskInterim = TaskInterim(
            id = 0,
            title = editTextTitle.text.toString(),
            description = taskEdittextDescription.text.toString(),
            subTasks = subtaskList,
            color = Color.parseColor(selectedColor),
            appointedDate = selectedDate!!,
            completionDate = null,
            taskCheckbox.isChecked
        )

        if (taskInterim.isChecked)
            taskInterim.completionDate = LocalDate.now()

        val intent = Intent()
        intent.putExtra(resources.getString(R.string.INTENT_CREATE_TASK), taskInterim)
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun initSubtaskDialog(){
        val dialog = Dialog(this@AddTaskActivity)
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

            val subTaskInterim = SubTaskInterim(0, title, false, 0)
            subtaskList.add(subTaskInterim)
            adapter.addSubTask(subTaskInterim)

            dialog.dismiss()
        }

        dialog.show()
    }

    private fun initColorDialog() {
        val colorDialog = GridViewDialog(this@AddTaskActivity, colorStringArray)
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
}

