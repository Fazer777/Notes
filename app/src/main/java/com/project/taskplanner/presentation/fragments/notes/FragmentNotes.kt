package com.project.taskplanner.presentation.fragments.notes

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
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
import com.project.taskplanner.R
import com.project.taskplanner.databinding.FragmentNotesBinding
import com.project.taskplanner.presentation.activities.note.AddNoteActivity
import com.project.taskplanner.presentation.activities.category.CategoryActivity
import com.project.taskplanner.presentation.activities.note.UpdateNoteActivity
import com.project.taskplanner.presentation.adapters.note.RecyclerViewNoteAdapter
import com.project.taskplanner.presentation.adapters.note.RecyclerViewNoteAdapter.MyOnItemClickListener
import com.project.taskplanner.presentation.viewmodels.notes.FragmentNotesVM
import org.koin.androidx.viewmodel.ext.android.viewModel


class FragmentNotes : Fragment() {

    private lateinit var binding: FragmentNotesBinding;
    private val viewModel by viewModel<FragmentNotesVM>()

    private val adapter = RecyclerViewNoteAdapter()

    private var createNote : ActivityResultLauncher<Intent>? = null
    private var updateNote : ActivityResultLauncher<Intent>? = null


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

        viewModel.notes.observe(viewLifecycleOwner){newList->
            adapter.setAdapterList(newList = newList)
            showImageEmpty()
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
                        startActivity(Intent(requireContext(), CategoryActivity::class.java))
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
                Toast.makeText(requireContext(), "Note has been created", Toast.LENGTH_SHORT).show()
            }
        }

        updateNote = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK){
                Toast.makeText(requireContext(), "Note has been updated", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initButtons() {
        binding.apply {
            idFloatBtnAddNote.setOnClickListener {
                createNote?.launch(Intent(context, AddNoteActivity::class.java))
                //startActivity(Intent(requireContext(), AddNoteActivity::class.java ))
            }
        }
    }

    private fun initRecyclerView() {
        binding.apply {
            recyclerViewNote.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            recyclerViewNote.adapter = adapter

            adapter.setOnItemClickListener(object : MyOnItemClickListener {
                override fun onItemClick(itemView: View, position: Int) {
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

                            viewModel.deleteNote(adapter.getItem(position))
                            showImageEmpty()

                            dialogInterface.dismiss()

                        }.setCancelable(false).create().show()
                }
            })
        }
    }

    private fun showImageEmpty() =with(binding) {
        val isEmpty = adapter.itemCount == 0
        imgViewEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
        textViewEmptyText.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }
}