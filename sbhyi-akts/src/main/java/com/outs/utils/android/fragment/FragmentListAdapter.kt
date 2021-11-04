package com.outs.utils.android.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/9/24 14:48
 * desc:
 */
class FragmentListAdapter<D, F : Fragment> : FragmentStateAdapter {

    val list: ArrayList<D>
    private val createFragment: (D) -> F

    constructor(activity: FragmentActivity, data: List<D>, createFragment: (D) -> F) : super(
        activity
    ) {
        this.list = ArrayList(data)
        this.createFragment = createFragment
    }

    constructor(fragment: Fragment, data: List<D>, createFragment: (D) -> F) : super(fragment) {
        this.list = ArrayList(data)
        this.createFragment = createFragment
    }

    override fun getItemCount(): Int = list.size

    override fun createFragment(position: Int): Fragment = createFragment.invoke(list[position])

}