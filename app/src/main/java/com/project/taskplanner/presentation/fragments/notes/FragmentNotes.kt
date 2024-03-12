package com.project.taskplanner.presentation.fragments.notes

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.project.domain.models.NoteInterim
import com.project.taskplanner.R
import com.project.taskplanner.databinding.FragmentNotesBinding
import com.project.taskplanner.presentation.activities.note.AddNoteActivity
import com.project.taskplanner.presentation.activities.category.CategoryActivity
import com.project.taskplanner.presentation.activities.note.UpdateNoteActivity
import com.project.taskplanner.presentation.adapters.note.RecyclerViewNoteAdapter
import com.project.taskplanner.presentation.adapters.note.RecyclerViewNoteAdapter.MyOnItemClickListener
import com.project.taskplanner.presentation.viewmodels.notes.FragmentNotesVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.Serializable
import org.koin.androidx.viewmodel.ext.android.viewModel


class FragmentNotes : Fragment() {

    private lateinit var binding: FragmentNotesBinding;
    private val viewModel by viewModel<FragmentNotesVM>()
    private val adapter = RecyclerViewNoteAdapter()

    private var createNote : ActivityResultLauncher<Intent>? = null
    private var updateNote : ActivityResultLauncher<Intent>? = null
    private var updateCategory : ActivityResultLauncher<Intent>? = null
    private var notePosition  = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding  = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createToolbar()
        initActions()
        initRecyclerView()
        initButtons()

        viewModel.notesLive.observe(viewLifecycleOwner){newList->
            adapter.setAdapterList(newList = newList)
            showImageEmpty()
        }

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getNotes()
        }
    }

    private fun createToolbar() {
        val menuHost : MenuHost = requireActivity()

        requireActivity().actionBar?.setDisplayShowTitleEnabled(false)

        menuHost.addMenuProvider(object : MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.note_main_tool_bar, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId){
                    R.id.id_category_management -> {
                        updateCategory?.launch(Intent(requireContext(), CategoryActivity::class.java))
                    }
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun initActions(){
        createNote = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK){
                val noteInterim = getSerializable(
                    result.data!!,
                    resources.getString(R.string.INTENT_CREATE_NOTE),
                    NoteInterim::class.java
                )
                noteInterim.itemIndex = adapter.itemCount
                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.onAddNoteEvent(noteInterim)
                    adapter.addNote(noteInterim)
                    showImageEmpty()
                }
            }
        }

        updateNote = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK){
                val noteInterim = getSerializable(result.data!!, resources.getString(R.string.INTENT_UPDATE_NOTE), NoteInterim::class.java)
                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.onUpdateNoteEvent(noteInterim)
                    adapter.updateNote(notePosition, noteInterim)
                }
            }
        }

        updateCategory = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == Activity.RESULT_OK){
                val changes = result.data?.getBooleanExtra(resources.getString(R.string.INTENT_CATEGORY_CHANGED), true)
                if (changes == true) {
                    CoroutineScope(Dispatchers.Main).launch{
                        viewModel.getNotes()
                    }
                }
            }
        }
    }

    private fun initButtons() {
        binding.apply {
            idFloatBtnAddNote.setOnClickListener {
                createNote?.launch(Intent(context, AddNoteActivity::class.java))
            }
        }
    }

    private fun initRecyclerView() {
        binding.apply {
            recyclerViewNote.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            recyclerViewNote.adapter = adapter

            adapter.setOnItemClickListener(object : MyOnItemClickListener {
                override fun onItemClick(itemView: View, position: Int) {
                    notePosition = position
                    val intent = Intent(requireContext(), UpdateNoteActivity::class.java)
                    intent.putExtra(resources.getString(R.string.INTENT_UPDATE_NOTE), adapter.getItem(position))
                    updateNote?.launch(intent)
                }

                override fun onItemLongClick(itemView: View, position: Int) {

                    AlertDialog.Builder(requireActivity())
                        .setTitle(resources.getString(R.string.delete_selected_note))
                        .setNegativeButton(resources.getString(R.string.negativeAnswer)) { dialogInterface: DialogInterface?, i: Int ->

                            dialogInterface?.dismiss()
                        }
                        .setPositiveButton(resources.getString(R.string.positiveAnswer)) { dialogInterface: DialogInterface, i: Int ->

                            Toast.makeText(
                                requireContext(),
                                resources.getString(R.string.note_deleted),
                                Toast.LENGTH_SHORT
                            ).show()

                            CoroutineScope(Dispatchers.Main).launch {
                                viewModel.onDeleteNoteEvent(adapter.getItem(position).itemIndex)
                                adapter.deleteNote(position)
                                showImageEmpty()
                            }
                            dialogInterface.dismiss()

                        }.setCancelable(false).create().show()
                }
            })
        }
    }

    private fun <T : Serializable?> getSerializable(intent: Intent, key: String, m_class: Class<T>): T {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getSerializableExtra(key, m_class)!!
        else
            intent.getSerializableExtra(key) as T
    }

    private fun showImageEmpty() =with(binding) {
        val isEmpty = adapter.itemCount == 0
        imgViewEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
        textViewEmptyText.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

}