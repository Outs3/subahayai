package com.outs.core.android.databinding.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/28 9:31
 * desc:
 */
@Deprecated(
    "Use FragmentListAdapter instead.",
    ReplaceWith("com.outs.utils.android.fragment.FragmentListAdapter")
)
class FragmentListAdapter : FragmentStateAdapter {

    val list: List<Fragment>

    constructor(activity: FragmentActivity, list: List<Fragment>) : super(activity) {
        this.list = list
    }

    constructor(fragment: Fragment, list: List<Fragment>) : super(fragment) {
        this.list = list
    }

    override fun getItemCount(): Int = list.size

    override fun createFragment(position: Int): Fragment = list[position]

}