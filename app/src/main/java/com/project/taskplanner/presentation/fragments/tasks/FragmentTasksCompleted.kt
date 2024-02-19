package com.project.taskplanner.presentation.fragments.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.project.taskplanner.databinding.FragmentTasksCompletedBinding

class FragmentTasksCompleted : Fragment() {
    private lateinit var binding: FragmentTasksCompletedBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTasksCompletedBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object{
        @JvmStatic
        fun newInstance() = FragmentTasksCompleted()
    }
}