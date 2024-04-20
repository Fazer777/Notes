package com.project.taskplanner.presentation.activities.note

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.project.domain.models.category.CategoryParam
import com.project.domain.models.note.NoteParam
import com.project.taskplanner.R
import com.project.taskplanner.databinding.UpdateNoteActivityBinding
import com.project.taskplanner.presentation.adapters.category.CategorySpinnerAdapter
import com.project.taskplanner.presentation.viewmodels.notes.UpdateNoteVM
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class UpdateNoteActivity : AppCompatActivity() {

    private var categorySpinnerAdapter = CategorySpinnerAdapter()
    private var checkMenuItem : MenuItem? = null

    private lateinit var toolbar : Toolbar
    private lateinit var binding: UpdateNoteActivityBinding
    private val viewModel by viewModel<UpdateNoteVM>()

    private var noteId : Long =0
   // private var itemPos = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UpdateNoteActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = findViewById(R.id.id_upd_note_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Редактирование"


        viewModel.categories.observe(this@UpdateNoteActivity){newlist ->
            categorySpinnerAdapter.setList(newList = newlist)
        }


        initEditText()
        initSpinner()

        getIntentData()

    }

    private fun initSpinner() {
        binding.apply {
            idUpdNoteSpinner.adapter = categorySpinnerAdapter
        }
    }

    private fun getIntentData() {

        val data : Bundle? = intent.extras
        if (data != null){
            val noteParam = getSerializable(data, resources.getString(R.string.INTENT_UPDATE_NOTE), NoteParam::class.java)
            noteId = noteParam.id
            binding.idUpdNoteEdittext.post {
                binding.idUpdNoteEdittext.setText(noteParam.description)
            }
            binding.idUpdNoteTextviewDatetime.post {
                binding.idUpdNoteTextviewDatetime.text = noteParam.date
                    .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))
            }
            binding.idUpdNoteSpinner.post {
                binding.idUpdNoteSpinner.setSelection(
                    categorySpinnerAdapter.getItemPositionByName(noteParam.category.name)
                )
            }
        }
    }

    private fun updateNote() = with(binding){
        val noteParam= NoteParam(
            id = noteId,
            description = idUpdNoteEdittext.text.toString(),
            category = idUpdNoteSpinner.selectedItem as CategoryParam,
            date = LocalDateTime.now(),
        )
        viewModel.updateNote(noteParam = noteParam)
        finish()

    }

    private fun initEditText() {
        binding.apply {
            idUpdNoteEdittext.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    viewModel.textChanged(s.toString())
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_note_tool_bar, menu)
        checkMenuItem = menu?.findItem(R.id.menu_toolbar_check)
        viewModel.isVisibleCheckLive.observe(this@UpdateNoteActivity){ state ->
            checkMenuItem?.isVisible = state
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){

            android.R.id.home -> finish()

            R.id.menu_toolbar_check -> updateNote()

            else -> {}
        }
        return true
    }

    private fun <T : Serializable?> getSerializable(bundle: Bundle?, key: String, m_class: Class<T>): T {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            bundle?.getSerializable(key, m_class)!!
        else
            bundle?.getSerializable(key) as T
    }
}