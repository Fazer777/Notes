package com.project.taskplanner.presentation.adapters.task

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.project.taskplanner.presentation.fragments.tasks.FragmentTasksCompleted
import com.project.taskplanner.presentation.fragments.tasks.FragmentTasksInProgress

class ViewPagerAdapter(fragmentAct : FragmentActivity)
    :  FragmentStateAdapter(fragmentAct) {

    private val fragmentList = listOf<Fragment>(
        FragmentTasksInProgress.newInstance(),
        FragmentTasksCompleted.newInstance()
    )
    override fun getItemCount(): Int {
        return fragmentList.count()
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}