package com.outs.utils.android.glide

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.outs.utils.android.R
import com.outs.utils.android.appInstance
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.random.Random

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/5/15 16:25
 * desc:
 */
val sManager by lazy { Glide.with(appInstance) }

suspend fun String.asBitmapByGlide(requestManager: RequestManager = sManager) =
    suspendCoroutine<Bitmap> { continuation ->
        requestManager
            .asBitmap()
            .load(this)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    continuation.resume(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    continuation.resumeWithException(RuntimeException("Bitmap load Failed!"))
                }
            })
    }

val tagGlide = Random.nextInt()

fun ImageView.loadUri(uri: Uri?) {
    sManager.load(uri)
        .placeholder(R.drawable.ic_placeholder)
        .error(R.drawable.ic_placeholder)
        .into(this)
    setTag(tagGlide, uri)
}

fun ImageView.loadUrl(url: String?) {
    loadUrl(url, R.drawable.ic_placeholder)
}

fun ImageView.loadUrlWithoutError(url: String?) {
    sManager.load(url).transform(CenterCrop()).into(this)
    setTag(tagGlide, url)
}

fun ImageView.loadUrl(url: String?, @DrawableRes placeHolder: Int) {
    sManager.load(url).transform(CenterCrop()).placeholder(placeHolder)
        .error(R.drawable.ic_placeholder).into(this)
    setTag(tagGlide, url)
}

fun ImageView.loadUrl(url: String?, placeHolder: Drawable? = null) {
    sManager.load(url).transform(CenterCrop()).placeholder(placeHolder)
        .error(placeHolder).into(this)
    setTag(tagGlide, url)
}

fun ImageView.loadUrlSkipMemory(url: String?) {
    sManager.load(url).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
        .transform(CenterCrop()).placeholder(R.drawable.ic_placeholder)
        .error(R.drawable.ic_placeholder)
        .into(this)
    setTag(tagGlide, url)
}

fun ImageView.fitCenter(url: String?) {
    sManager.load(url).transform(FitCenter()).error(R.drawable.ic_placeholder).into(this)
    setTag(tagGlide, url)
}

fun ImageView.imageNoPlace(url: String?) {
    sManager.load(url).transform(CenterCrop()).into(this)
    setTag(tagGlide, url)
}

fun ImageView.circleBack(url: String?) {
    sManager.load(url).transform(CenterCrop()).placeholder(R.drawable.ic_placeholder)
        .error(R.drawable.ic_placeholder).into(this)
    setTag(tagGlide, url)
}


