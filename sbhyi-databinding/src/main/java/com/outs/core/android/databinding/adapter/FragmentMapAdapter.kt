package com.outs.core.android.databinding.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/5/11 9:13
 * desc:
 */
@Deprecated(
    "Use FragmentListAdapter instead.",
    ReplaceWith("com.outs.utils.android.fragment.FragmentListAdapter")
)
open class FragmentMapAdapter<F : Fragment>(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    val fragmentMap: HashMap<Int, F> = HashMap(),
    val getItemCount: () -> Int,
    val createFragment: (position: Int) -> F
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = getItemCount.invoke()

    override fun createFragment(position: Int): Fragment =
        fragmentMap.getOrPut(position) { createFragment.invoke(position) }

}