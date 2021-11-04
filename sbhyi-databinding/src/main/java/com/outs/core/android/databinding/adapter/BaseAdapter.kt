package com.outs.core.android.databinding.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T : RecyclerView.ViewHolder> :
    RecyclerView.Adapter<T>(), View.OnClickListener, View.OnLongClickListener {

    override fun onClick(p0: View) {
    }

    override fun onLongClick(p0: View): Boolean {
        return true
    }

}