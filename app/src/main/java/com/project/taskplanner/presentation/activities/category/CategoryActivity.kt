package com.project.taskplanner.presentation.activities.category

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.project.domain.models.category.CategoryParam
import com.project.taskplanner.R
import com.project.taskplanner.databinding.BottomSheetCategoriesBinding
import com.project.taskplanner.databinding.CategoryActivityBinding
import com.project.taskplanner.presentation.adapters.category.RecyclerViewCategoryAdapter
import com.project.taskplanner.presentation.dialogs.GridViewDialog
import com.project.taskplanner.presentation.viewmodels.categories.CategoryVM
import org.koin.androidx.viewmodel.ext.android.viewModel


class CategoryActivity : AppCompatActivity() {
    private lateinit var binding: CategoryActivityBinding
    private lateinit var bindingBottomSheet: BottomSheetCategoriesBinding
    private lateinit var colorStringArray: Array<String>
    private lateinit var selectedColor: String

    private var categoryAdapter = RecyclerViewCategoryAdapter()
    private val viewModel by viewModel<CategoryVM>()

    private var isDeleted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CategoryActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        initActions()

        onBackPressedDispatcher.addCallback(this) {
            finish()
        }

        viewModel.categories.observe(this@CategoryActivity) { newList ->
            categoryAdapter.setAdapterList(newList = newList)
        }

        colorStringArray = resources.getStringArray(R.array.color_values)
    }

    private fun initActions() {
        binding.idFloatBtnAddCategory.setOnClickListener {
            initBottomSheetDialog()
        }
    }

    private fun initBottomSheetDialog() {

        val dialog = BottomSheetDialog(this@CategoryActivity)
        bindingBottomSheet = BottomSheetCategoriesBinding.inflate(layoutInflater)
        dialog.setContentView(bindingBottomSheet.root)

        bindingBottomSheet.idBotSheetTextView.background?.setTint(Color.parseColor(colorStringArray[0]))
        selectedColor = colorStringArray[0]

        bindingBottomSheet.idBotSheetBtnConfirm.setOnClickListener {
            dialog.dismiss()
        }

        bindingBottomSheet.idBotSheetBtnConfirm.setOnClickListener {
            val text = bindingBottomSheet.idBotSheetEdittext.text.toString()

            if (text.isBlank()) {
                Toast.makeText(
                    this@CategoryActivity,
                    resources.getString(R.string.name_category_is_empty),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (categoryAdapter.checkDuplicate(text)) {
                Toast.makeText(
                    this@CategoryActivity,
                    resources.getString(R.string.category_already_exists),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            createCategory()
            dialog.dismiss()
        }

        bindingBottomSheet.idBotSheetTextView.setOnClickListener {
            val gridDialog = GridViewDialog(this@CategoryActivity, colorStringArray)
            gridDialog.setOnItemClickListener(object : GridViewDialog.OnItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    val item: String = gridDialog.getItem(position)
                    bindingBottomSheet.idBotSheetTextView.background?.setTint(Color.parseColor(item))
                    selectedColor = item
                    gridDialog.dismiss()
                }

            })
            gridDialog.show()
        }

        dialog.setCancelable(true)
        dialog.show()
    }

    private fun createCategory() {
        val categoryParam = CategoryParam(
            id = 0,
            name = bindingBottomSheet.idBotSheetEdittext.text.toString(),
            color = Color.parseColor(selectedColor)
        )
        viewModel.addCategory(categoryParam)
    }

    private fun initRecyclerView() = with(binding) {
        recyclerViewCategory.layoutManager = LinearLayoutManager(this@CategoryActivity)
        recyclerViewCategory.adapter = categoryAdapter

        categoryAdapter
            .setOnItemClickListener(object : RecyclerViewCategoryAdapter.MyOnItemClickListener {
                override fun onItemLongClick(itemView: View, position: Int) {
                    if (position == 0) {
                        Toast.makeText(
                            this@CategoryActivity,
                            resources.getString(R.string.can_not_removed),
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }
                    AlertDialog.Builder(this@CategoryActivity)
                        .setTitle(resources.getString(R.string.delete_selected_category))
                        .setNegativeButton(resources.getString(R.string.negativeAnswer)) { dialogInterface: DialogInterface?, i: Int ->
                            dialogInterface?.dismiss()
                        }
                        .setPositiveButton(resources.getString(R.string.positiveAnswer)) { dialogInterface: DialogInterface?, i: Int ->

                            Toast.makeText(
                                this@CategoryActivity,
                                resources.getString(R.string.category_deleted),
                                Toast.LENGTH_SHORT
                            ).show()

                            val categoryId = categoryAdapter.getItem(position).id
                            viewModel.updateNotesWithDeletedCategory(
                                defaultCategoryId = 1,
                                deletedCategoryId = categoryId
                            )
                            viewModel.deleteCategory(categoryAdapter.getItem(position))

                            isDeleted = true
                            dialogInterface?.dismiss()

                        }.setCancelable(false).create().show()
                }
            })
    }
}