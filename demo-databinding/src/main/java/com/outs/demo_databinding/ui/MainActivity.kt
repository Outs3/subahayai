package com.outs.demo_databinding.ui

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.net.Uri
import com.outs.core.android.databinding.activity.BaseActivity
import com.outs.core.android.takePhoto
import com.outs.demo_databinding.R
import com.outs.demo_databinding.databinding.ActivityMainBinding
import com.outs.utils.android.readImageAsFile
import com.outs.utils.android.viewModel
import kotlinx.coroutines.delay
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/11/4 14:39
 * desc:
 */
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override val getViewModel: MainViewModel
        get() = viewModel()

    override val getLayoutId: Int
        get() = R.layout.activity_main

    override fun onDebug() {
        super.onDebug()
        addDebugOpt(
            "3s Loading" to {
                mViewModel.launchOnUI(true) {
                    delay(3000)
                }
            },
            "readImgUri" to { readImgUri() }
        )
    }

    @SuppressLint("MissingPermission")
    private fun readImgUri() {
        mViewModel.launchOnUI {
            permissionOrThrow(Manifest.permission.CAMERA)
            //选择图片
            val uri = suspendCoroutine<Uri> { takePhoto(it::resume) }
            //读取uri内容
            uri.also(mBinding.imageCover1::setImageURI)
                .readImageAsFile(context)
                ?.path
                ?.let(BitmapFactory::decodeFile)
                ?.also(mBinding.imageCover2::setImageBitmap)
        }
    }

}