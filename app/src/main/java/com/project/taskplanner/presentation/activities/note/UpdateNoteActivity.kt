package com.project.taskplanner.presentation.activities.note

import android.content.Intent
import android.os.Build
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
import com.project.taskplanner.databinding.UpdateNoteActivityBinding
import com.project.taskplanner.presentation.adapters.category.CategorySpinnerAdapter
import com.project.taskplanner.presentation.viewmodels.notes.UpdateNoteVM
import com.project.taskplanner.presentation.viewmodels.notes.UpdateNoteViewModelFactory
import java.io.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class UpdateNoteActivity : AppCompatActivity() {

    private var categorySpinnerAdapter = CategorySpinnerAdapter()
    private var checkMenuItem : MenuItem? = null

    private lateinit var toolbar : Toolbar
    private lateinit var binding: UpdateNoteActivityBinding
    private lateinit var viewModel : UpdateNoteVM

    private var itemIndex = 0
   // private var itemPos = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UpdateNoteActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = findViewById(R.id.id_upd_note_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Редактирование"

        viewModel = ViewModelProvider(
            this@UpdateNoteActivity,
            UpdateNoteViewModelFactory(this@UpdateNoteActivity)
        ).get(UpdateNoteVM::class.java)

        viewModel.categoriesLive.observe(this@UpdateNoteActivity){
            categorySpinnerAdapter.categoryList = it
            categorySpinnerAdapter.notifyDataSetChanged()
        }


        initEditText()
        initSpinner()

        Thread(
            Runnable { getIntentData() },
            "ThreadGetIntentData"
        ).start()
    }

    private fun initSpinner() {
        binding.apply {
            idUpdNoteSpinner.adapter = categorySpinnerAdapter
        }
    }

    // TODO (IF bundle equals null???) -> to finish act this error message
    private fun getIntentData() {

        val data : Bundle? = intent.extras
        val noteInterim = getSerializable(data, resources.getString(R.string.INTENT_UPDATE_NOTE), NoteInterim::class.java)

        itemIndex = noteInterim.itemIndex

        binding.idUpdNoteEdittext.post {
            binding.idUpdNoteEdittext.setText(noteInterim.noteDescription)
        }
        binding.idUpdNoteTextviewDatetime.post {
            binding.idUpdNoteTextviewDatetime.text = noteInterim.noteDate
                .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))
        }
        binding.idUpdNoteSpinner.post {
            binding.idUpdNoteSpinner.setSelection(
                categorySpinnerAdapter.getItemPositionByName(noteInterim.category.name)
            )
        }

    }

    private fun updateNote() = with(binding){
        val noteInterimUpd = NoteInterim(
            noteDescription = idUpdNoteEdittext.text.toString(),
            category = idUpdNoteSpinner.selectedItem as CategoryInterim,
            noteDate = LocalDateTime.now(),
            itemIndex = itemIndex
        )
        val intent = Intent()
        intent.putExtra(resources.getString(R.string.INTENT_UPDATE_NOTE), noteInterimUpd)
        setResult(RESULT_OK, intent)
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