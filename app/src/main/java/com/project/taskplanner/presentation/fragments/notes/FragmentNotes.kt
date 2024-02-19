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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.project.domain.models.NoteInterim
import com.project.taskplanner.R
import com.project.taskplanner.databinding.FragmentNotesBinding
import com.project.taskplanner.presentation.activities.note.AddNoteActivity
import com.project.taskplanner.presentation.activities.category.CategoryActivity
import com.project.taskplanner.presentation.activities.note.UpdateNoteActivity
import com.project.taskplanner.presentation.adapters.note.RecyclerViewNoteAdapter
import com.project.taskplanner.presentation.adapters.note.RecyclerViewNoteAdapter.MyOnItemClickListener
import com.project.taskplanner.presentation.viewmodels.notes.FragmentNoteViewModelFactory
import com.project.taskplanner.presentation.viewmodels.notes.FragmentNotesVM
import java.io.Serializable


class FragmentNotes : Fragment() {

    private lateinit var binding: FragmentNotesBinding;
    private val viewModel : FragmentNotesVM by viewModels {FragmentNoteViewModelFactory(requireContext())}
    private var adapter = RecyclerViewNoteAdapter()

    private val observer = Observer<ArrayList<NoteInterim>> {
        adapter.noteList = it
    }

    private var createNote : ActivityResultLauncher<Intent>? = null
    private var updateNote : ActivityResultLauncher<Intent>? = null
    private var updateCategory : ActivityResultLauncher<Intent>? = null
    private var notePosition  = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding  = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost : MenuHost = requireActivity()

        requireActivity().actionBar?.setDisplayShowTitleEnabled(false)

        // So OnCreateOptionsMenu is deprecated
        menuHost.addMenuProvider(object : MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.note_main_tool_bar, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId){
                    R.id.id_category_management -> {
                        Toast.makeText(requireContext(), "OK", Toast.LENGTH_SHORT).show()
                        updateCategory?.launch(Intent(requireContext(), CategoryActivity::class.java))
                    }
                    else ->{
                        Toast.makeText(requireContext(), "NOK", Toast.LENGTH_SHORT).show()
                    }
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)


        createNote = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK){
                val noteInterim = getSerializable(
                    result.data!!,
                    resources.getString(R.string.INTENT_CREATE_NOTE),
                    NoteInterim::class.java
                )
                noteInterim.itemIndex = adapter.itemCount
                viewModel.onAddNoteEvent(noteInterim)
                adapter.addNote(noteInterim)
            }
            else{
                Toast.makeText(context, "Adding note canceled", Toast.LENGTH_SHORT).show()
            }
        }

        updateNote = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK){
                val noteInterim = getSerializable(result.data!!, resources.getString(R.string.INTENT_UPDATE_NOTE), NoteInterim::class.java)
                viewModel.onUpdateNoteEvent(noteInterim)
                adapter.updateNote(notePosition, noteInterim)
            }
            else{
                Toast.makeText(context, "Updating note canceled", Toast.LENGTH_SHORT).show()
            }

        }

        updateCategory = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == Activity.RESULT_OK){
                val changes = result.data?.getBooleanExtra("Changes", true)
                if (changes == true) {
                    viewModel.getNotes()
                    adapter.notifyDataSetChanged()
                    Toast.makeText(context, "List updated", Toast.LENGTH_SHORT).show()
                }

            }
        }

        initRecyclerView()
        initActions()

        viewModel.notesLive.observe(viewLifecycleOwner, observer)
    }

    private fun initActions() {
        binding.apply {
            idFloatBtnAddNote.setOnClickListener {
                Toast.makeText(requireContext(), "btn add note clicked", Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(
                        requireContext(),
                        "Update note launched",
                        Toast.LENGTH_SHORT).show()
                    notePosition = position
                    val intent = Intent(requireContext(), UpdateNoteActivity::class.java)
                    intent.putExtra(resources.getString(R.string.INTENT_UPDATE_NOTE), adapter.noteList[position])
                    updateNote?.launch(intent)
                }

                override fun onItemLongClick(itemView: View, position: Int) {

                    AlertDialog.Builder(requireActivity())
                        .setTitle("Удалить выбранную заметку?")
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
                                "Заметка удалена",
                                Toast.LENGTH_SHORT
                            ).show()

                            viewModel.onDeleteNoteEvent(adapter.noteList[position].itemIndex)
                            adapter.deleteNote(position)
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

    override fun onDestroy() {
        viewModel.notesLive.removeObserver(observer)
        super.onDestroy()
    }
}