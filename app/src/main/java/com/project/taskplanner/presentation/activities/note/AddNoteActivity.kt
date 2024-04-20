package com.project.taskplanner.presentation.activities.note

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
import com.project.taskplanner.databinding.AddNoteActivityBinding
import com.project.taskplanner.presentation.adapters.category.CategorySpinnerAdapter
import com.project.taskplanner.presentation.viewmodels.notes.AddNoteVM
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class AddNoteActivity : AppCompatActivity() {

    private lateinit var toolbar : Toolbar
    private lateinit var binding: AddNoteActivityBinding
    private val viewModel by viewModel<AddNoteVM>()
    private var categorySpinnerAdapter = CategorySpinnerAdapter()
    private var checkMenuItem : MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddNoteActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = findViewById(R.id.id_add_note_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Создание"


        viewModel.categories.observe(this@AddNoteActivity) {newList ->
            categorySpinnerAdapter.setList(newList = newList)
        }

        initEditText()
        initTextViewDataTime()
        initSpinnerCategory()

    }

    private fun initTextViewDataTime() {
        binding.idAddNoteTextviewDatetime.text = getDateTimeNow()
            .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))
    }

    private fun initSpinnerCategory() {

        binding.apply {
            idAddNoteSpinner.adapter = categorySpinnerAdapter
            idAddNoteSpinner.setSelection(0);
        }
    }

    private fun initEditText() {
        binding.apply {
            idAddNoteEdittext.addTextChangedListener(object : TextWatcher {
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

        viewModel.isVisibleCheckLive.observe(this@AddNoteActivity){ state ->
            checkMenuItem?.isVisible = state
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){

            android.R.id.home -> finish()

            R.id.menu_toolbar_check -> createNote()

            else ->{}
        }
        return true
    }

    private fun createNote() = with(binding) {
        val noteParam = NoteParam(
            id = 0,
            description = idAddNoteEdittext.text.toString(),
            category = getSelectedCategory(),
            date = getDateTimeNow()
        )

        viewModel.addNote(noteParam)
        finish()
    }

    private fun getSelectedCategory() : CategoryParam {
        return binding.idAddNoteSpinner.selectedItem as CategoryParam
    }

    private fun getDateTimeNow() : LocalDateTime {
        return LocalDateTime.now()
    }
}


