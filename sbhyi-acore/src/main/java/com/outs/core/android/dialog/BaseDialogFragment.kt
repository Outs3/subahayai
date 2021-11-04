package com.outs.core.android.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.*
import androidx.lifecycle.LifecycleOwner
import com.outs.core.android.R
import com.outs.utils.android.injectArgs
import com.outs.utils.kotlin.className

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/11/2 13:59
 * desc:
 */
abstract class BaseDialogFragment : DialogFragment(), View.OnClickListener {

    protected val owner: LifecycleOwner = this

    protected open val getThemeId: Int
        get() = R.style.default_dialog_style

    protected open val getDimAmount: Float
        get() = 0.7F

    protected open val getGravity: Int
        get() = Gravity.CENTER

    protected open val canCancel: Boolean
        get() = true

    protected open val getWidth: Int
        get() = ViewGroup.LayoutParams.WRAP_CONTENT

    protected open val getHeight: Int
        get() = ViewGroup.LayoutParams.WRAP_CONTENT

    protected abstract val getContentView: View

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val context = requireContext()
        initView(context)
        injectArgs()
        val dialog = Dialog(context, getThemeId)
        initDialog(dialog)
        initView()
        return dialog
    }

    protected abstract fun initView(context: Context)

    open fun initDialog(dialog: Dialog) {
        dialog.setContentView(getContentView)
        val canCancel = canCancel
        dialog.setCancelable(canCancel)
        dialog.setCanceledOnTouchOutside(canCancel)
        dialog.window?.let(::initWindow)
        isCancelable = canCancel
    }

    open fun initWindow(window: Window) {
        window.setGravity(getGravity)
        window.setLayout(getWidth, getHeight)
        window.attributes.dimAmount = getDimAmount
    }

    open fun initView() {

    }

    fun show(manager: FragmentManager) {
        show(manager, className)
    }

    fun show(transaction: FragmentTransaction): Int = show(transaction, className)

    fun show(activity: FragmentActivity) {
        show(activity.supportFragmentManager)
    }

    fun show(fragment: Fragment) {
        show(fragment.childFragmentManager)
    }

    override fun onClick(v: View) {

    }

}