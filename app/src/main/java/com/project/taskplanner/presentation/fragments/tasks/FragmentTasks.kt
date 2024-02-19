package com.project.taskplanner.presentation.fragments.tasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.project.taskplanner.databinding.FragmentTasksBinding
import com.project.taskplanner.presentation.adapters.task.ViewPagerAdapter


class FragmentTasks : Fragment() {

    private lateinit var binding: FragmentTasksBinding
    private val fragmentTitles = listOf<String>(
        "Task in progress",
        "Tasks completed"
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTasksBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val vpAdapter = ViewPagerAdapter(requireActivity())
        binding.idViewPager2.adapter = vpAdapter

        TabLayoutMediator(binding.idTabLayout, binding.idViewPager2){  tab, pos ->
            tab.text = fragmentTitles[pos]
        }.attach()
    }
}