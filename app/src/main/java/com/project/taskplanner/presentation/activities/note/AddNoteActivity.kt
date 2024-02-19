package com.project.taskplanner.presentation.activities.note

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.project.domain.models.CategoryInterim
import com.project.domain.models.NoteInterim
import com.project.taskplanner.R
import com.project.taskplanner.databinding.AddNoteActivityBinding
import com.project.taskplanner.presentation.adapters.category.CategorySpinnerAdapter
import com.project.taskplanner.presentation.viewmodels.notes.AddNoteViewModelFactory
import com.project.taskplanner.presentation.viewmodels.notes.AddNoteVM
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class AddNoteActivity : AppCompatActivity() {

    private lateinit var toolbar : Toolbar
    private lateinit var viewModel : AddNoteVM
    private lateinit var binding: AddNoteActivityBinding
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

        viewModel = ViewModelProvider(
            this@AddNoteActivity,
            AddNoteViewModelFactory(this@AddNoteActivity)
        ).get(AddNoteVM::class.java)


        viewModel.categoriesLive.observe(this@AddNoteActivity) {
            categorySpinnerAdapter.categoryList = it
            categorySpinnerAdapter.notifyDataSetChanged()
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
        var noteInterim : NoteInterim? = null
        apply {
            noteInterim = NoteInterim(
                idAddNoteEdittext.text.toString(),
                getSelectedCategory(),
                getDateTimeNow(),
                itemIndex = 0
            )
        }

        val intent = Intent()
        intent.putExtra(resources.getString(R.string.INTENT_CREATE_NOTE), noteInterim)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun getSelectedCategory() : CategoryInterim{
        return binding.idAddNoteSpinner.selectedItem as CategoryInterim
    }

    private fun getDateTimeNow() : LocalDateTime {
        return LocalDateTime.now()
    }
}


