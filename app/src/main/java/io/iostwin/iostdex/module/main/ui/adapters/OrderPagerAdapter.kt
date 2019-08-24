package io.iostwin.iostdex.module.main.ui.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import io.iostwin.iostdex.module.main.ui.fragments.OrderListFragment

class OrderPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    private val fragments = arrayOf(OrderListFragment(),OrderListFragment())

    init {
        val f0 = Bundle()
        f0.putBoolean("fist",true)
        fragments[0].arguments = f0
        val f1 = Bundle()
        f1.putBoolean("fist",false)
        fragments[1].arguments = f1
    }

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = 2
}