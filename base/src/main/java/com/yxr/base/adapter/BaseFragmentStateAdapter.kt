package com.yxr.base.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

open class BaseFragmentStateAdapter<T : FragmentState>(
    private val fragmentStateList: ArrayList<T>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount() = fragmentStateList.size

    override fun createFragment(position: Int) = fragmentStateList[position].fragment

    fun getItem(position: Int): T? = fragmentStateList.getOrNull(position)

    fun getFragment(position: Int) = fragmentStateList.getOrNull(position)?.fragment

    fun getFragmentStateList() = fragmentStateList
}

open class FragmentState(val fragment: Fragment, val title: String? = null)