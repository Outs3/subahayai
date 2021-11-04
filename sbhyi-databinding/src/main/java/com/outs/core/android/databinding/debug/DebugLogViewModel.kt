package com.outs.core.android.databinding.debug

import androidx.lifecycle.LifecycleOwner
import com.outs.core.android.vm.BaseViewModel

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/10/8 10:56
 * desc:
 */
open class DebugLogViewModel : BaseViewModel() {

    private val refreshHelper by lazy { LogHelper() }

    val logData by lazy { refreshHelper.logData }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        refreshHelper.stop()
    }

}