package com.slaytertv.tcgbolivia.ui.viewmodel.profile.tabprofile

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewpagerAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {
    val fragments = mutableListOf<Pair<Fragment, String>>()

    fun addFragment(fragment: Fragment, title: String) {
        fragments.add(Pair(fragment, title))
    }

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position].first
    }
}