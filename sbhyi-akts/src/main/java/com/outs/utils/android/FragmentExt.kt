package com.outs.utils.android

import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.outs.utils.android.fragment.FragmentListAdapter
import com.outs.utils.android.fragment.FragmentPositionAdapter

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/12 0:07
 * desc:
 */
fun Fragment.setStatusBarColor(@ColorInt color: Int) {
    activity?.window?.let { window ->
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = color
    }
}

fun simpleFragmentAdapter(
    activity: FragmentActivity,
    count: Int,
    createFragment: (Int) -> Fragment
): FragmentStateAdapter = FragmentPositionAdapter(activity, count, createFragment)

fun simpleFragmentAdapter(
    fragment: Fragment,
    count: Int,
    createFragment: (Int) -> Fragment
): FragmentStateAdapter = FragmentPositionAdapter(fragment, count, createFragment)

fun simpleFragmentAdapter(
    activity: FragmentActivity,
    getItemCount: () -> Int,
    createFragment: (Int) -> Fragment
): FragmentStateAdapter = FragmentPositionAdapter(activity, getItemCount, createFragment)

fun simpleFragmentAdapter(
    fragment: Fragment,
    getItemCount: () -> Int,
    createFragment: (Int) -> Fragment
): FragmentStateAdapter = FragmentPositionAdapter(fragment, getItemCount, createFragment)

fun <D, F : Fragment> simpleFragmentAdapter(
    activity: FragmentActivity,
    data: List<D>,
    createFragment: (D) -> F
) = FragmentListAdapter(activity, data, createFragment)

fun <D, F : Fragment> simpleFragmentAdapter(
    fragment: Fragment,
    data: List<D>,
    createFragment: (D) -> F
) = FragmentListAdapter(fragment, data, createFragment)