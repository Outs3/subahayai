package com.outs.utils.android

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import com.blankj.utilcode.util.FileIOUtils
import com.outs.utils.android.launch.SimpleActivityLauncher
import com.outs.utils.android.store.Image
import com.outs.utils.android.store.readRows
import java.io.File
import java.util.*

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/11 10:37
 * desc:
 */
fun File.saveToThumb(context: Context, isVideo: Boolean = false) {
    if (isVideo)
        context.insertVideo(this)
    else /*isImage*/
        context.insertImage(this)
}

fun File.saveFileToSystem(
    context: Context,
    launcher: SimpleActivityLauncher? = null,
    withToast: Boolean = true
): Boolean {
    val mimeType = mimeTypeOrThrow()
    return when {
        mimeType.startsWith("image") -> context.insertImage(this).let {
            if (withToast) "已保存到相册".toast()
            true
        }
        mimeType.startsWith("video") -> context.insertVideo(this).let {
            if (withToast) "视频已保存".toast()
            true
        }
        mimeType.startsWith("audio") -> context.insertAudio(this).let {
            if (withToast) "音频已保存".toast()
            true
        }
        isDocumentFile(mimeType) -> {
            val intent = createDocument(this)
            launcher?.launch(intent) { activityResult ->
                if (Activity.RESULT_OK == activityResult.resultCode) {
                    val uri = activityResult.data?.data ?: throw RuntimeException("写入文档失败，无效的uri！")
                    val data = FileIOUtils.readFile2BytesByStream(this)
                    context.contentResolver.write(uri, data)
                    if (withToast) "文档已保存".toast()
                }
            }
            true
        }
        android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q ->
            context.insertDownload(this).let {
                if (withToast) "文件已保存".toast()
                true
            }
        else -> false
    }
}

fun newFile(parent: File, name: String, data: ByteArray): File =
    File(parent, name).apply { FileIOUtils.writeFileFromBytesByStream(this, data) }

fun ByteArray.asTempFile(context: Context, name: String? = null, ext: String? = null) =
    context.newTempFile(name, ext, this)

fun Uri.asFile(context: Context): File? {
    if (scheme.isNullOrEmpty())
        return File(toString())
    val path = path
        ?: return null
    val file = File(path)
    if (file.canRead())
        return file
    val data = context.contentResolver.read(this)
        ?: return null
    return newFile(context.cacheDir, file.name, data)
}

@Deprecated(
    "由于没有处理相册图片旋转角度问题，这个方法已经被废弃了，请使用Uri.readImageAsFile等相关方法",
    replaceWith = ReplaceWith("Uri.readImageAsFile()", "com.outs.utils.android")
)
fun Uri.asImageFile(context: Context): File? {
    if (scheme.isNullOrEmpty())
        return File(toString())
    val path = path
        ?: return null
    val file = File(path)
    if (file.canRead())
        return file
    val data = context.contentResolver.read(this)
        ?: return null
    val image =
        context.contentResolver.query(this, null, null, null, null)?.use { it.readRows<Image>() }
            ?.firstOrNull() ?: return null
    return newFile(context.cacheDir, image.displayName ?: image.title ?: file.name, data)
}

fun String.asMimeType(parent: String? = null): String? =
    MimeTypeMap.getSingleton().getMimeTypeFromExtension(this) ?: parent?.let { "$it/$this" }

fun Context.newTempFile(name: String? = null, ext: String? = null, data: ByteArray? = null): File {
    val fileName = name ?: UUID.randomUUID().toString()
    val fileExt = ext?.let { ".$it" } ?: ""
    return File(cacheDir, "$fileName$fileExt").also { file ->
        data?.let { data -> FileIOUtils.writeFileFromBytesByStream(file, data) }
    }
}

fun Context.getTempFile(name: String): File = File(cacheDir, name)

fun File.provideFile(context: Context = appInstance, authority: String): Uri =
    FileProvider.getUriForFile(context, authority, this)

fun File.viewIntent(
    mimeType: String = mimeType(),
    chooserTitle: String = "打开文件",
    authority: String
): Intent = this
    .provideFile(authority = authority)
    .let { uri ->
        Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, mimeType)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }
    .let { Intent.createChooser(it, chooserTitle) }