package com.outs.core.android

import android.Manifest
import android.net.Uri
import androidx.annotation.RequiresPermission
import com.outs.core.android.databinding.dialog.BottomSelectDialog
import com.outs.utils.android.captureImage
import com.outs.utils.android.launch.ILaunchOwner
import com.outs.utils.android.pickImage

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/10/29 15:39
 * desc:
 */
@RequiresPermission(Manifest.permission.CAMERA)
fun ILaunchOwner.takePhoto(onTake: (Uri) -> Unit) {
    BottomSelectDialog()
        .content(listOf("拍摄", "从相册选择"))
        .onConfirm { dialog, position, text ->
            dialog.dismiss()
            when (position) {
                0 -> {
                    captureImage(onTake)
                }
                1 -> {
                    pickImage(onTake)
                }
            }
        }
        .show(getActivity().supportFragmentManager)
}
