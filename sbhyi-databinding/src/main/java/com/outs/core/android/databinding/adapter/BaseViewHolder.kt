package com.outs.core.android.databinding.adapter

import android.view.View
import kotlinx.android.extensions.LayoutContainer

abstract class BaseViewHolder<T> : SimpleItem<T>(), LayoutContainer {
    override val containerView: View?
        get() = rootView
}
