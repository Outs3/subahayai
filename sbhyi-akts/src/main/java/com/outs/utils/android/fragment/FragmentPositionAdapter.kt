package com.outs.utils.android.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/9/24 14:44
 * desc:
 */
class FragmentPositionAdapter : FragmentStateAdapter {

    private val getItemCount: () -> Int
    private val createFragment: (Int) -> Fragment

    constructor(
        activity: FragmentActivity,
        count: Int,
        createFragment: (Int) -> Fragment
    ) : super(activity) {
        this.getItemCount = { count }
        this.createFragment = createFragment
    }

    constructor(
        activity: FragmentActivity,
        getItemCount: () -> Int,
        createFragment: (Int) -> Fragment
    ) : super(activity) {
        this.getItemCount = getItemCount
        this.createFragment = createFragment
    }

    constructor(
        fragment: Fragment,
        count: Int,
        createFragment: (Int) -> Fragment
    ) : super(fragment) {
        this.getItemCount = { count }
        this.createFragment = createFragment
    }

    constructor(
        fragment: Fragment,
        getItemCount: () -> Int,
        createFragment: (Int) -> Fragment
    ) : super(fragment) {
        this.getItemCount = getItemCount
        this.createFragment = createFragment
    }

    override fun getItemCount(): Int = getItemCount.invoke()

    override fun createFragment(position: Int): Fragment = createFragment.invoke(position)

}