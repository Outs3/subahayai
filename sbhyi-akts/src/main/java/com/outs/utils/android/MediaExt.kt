package com.outs.utils.android

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.annotation.RequiresApi
import com.blankj.utilcode.util.FileIOUtils
import com.outs.utils.android.store.Audio
import com.outs.utils.android.store.Ringtone
import com.outs.utils.android.store.readRows
import com.outs.utils.kotlin.tryOrNull
import java.io.File

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/11 10:18
 * desc:
 */
const val MIMETYPE_ALL = "*/*"

data class MediaInfo(val uri: String, val duration: Long, val cover: Bitmap?)

fun mimeTypeOfExtension(extension: String) =
    MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)

fun File.mimeType(parent: String = "image"): String = mimeTypeOrNull() ?: "$parent/$extension"

fun File.mimeTypeOrNull(): String? =
    MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension) ?: asDocumentMimeType()

fun File.mimeTypeOrThrow(): String =
    mimeTypeOrNull() ?: throw RuntimeException("无法解析的文件类型: ${name}")

fun pickFile(mimeType: String = MIMETYPE_ALL): Intent = Intent(Intent.ACTION_GET_CONTENT).apply {
    type = mimeType
    addCategory(Intent.CATEGORY_OPENABLE)
}

fun pickFileByTypes(vararg mimeTypes: String): Intent =
    pickFile().putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)

fun pickImage(mimeType: String = "image/*"): Intent = pickFile(mimeType)

fun pickVideo(mimeType: String = "video/*"): Intent = pickFile(mimeType)

fun pickAudio(mimeType: String = "audio/*"): Intent = pickFile(mimeType)

fun pickRingtone(): Intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)

fun Context.insertImage(image: File, editValues: ((ContentValues) -> ContentValues)? = null) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        insertImageQ(image, editValues)
    } else {
        MediaStore.Images.Media.insertImage(
            contentResolver,
            image.absolutePath,
            image.name,
            image.name
        )
    }
}

fun Context.insertImageQ(
    image: File,
    editValues: ((ContentValues) -> ContentValues)? = null
): Uri? {
    val name = image.name
    val mimeType = image.mimeType("image")
    val contentValues = ContentValues().apply {
        val currentTime = System.currentTimeMillis()
        put(MediaStore.Images.Media.TITLE, name)
        put(MediaStore.Images.Media.DISPLAY_NAME, name)
        put(MediaStore.Images.Media.MIME_TYPE, mimeType)
        put(MediaStore.Images.Media.DATE_ADDED, currentTime)
        put(MediaStore.Images.Media.DATE_MODIFIED, currentTime)
        put(MediaStore.Images.Media.SIZE, image.length())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/")
            put(MediaStore.Images.Media.DATE_TAKEN, currentTime)
        } else {
            put(
                MediaStore.Images.Media.DATA,
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).path
            )
        }
    }.let { editValues?.invoke(it) ?: it }

    val data = FileIOUtils.readFile2BytesByStream(image)

    // Android 10 插入到图库标志位
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        contentValues.put(MediaStore.MediaColumns.IS_PENDING, 1)
    }
    return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues, data)
        ?.also { uri ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.clear()
                contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                contentResolver.update(uri, contentValues, null, null)
            }
        }
}

fun Context.insertVideo(video: File, editValues: ((ContentValues) -> ContentValues)? = null) {
    val name = video.name
    val mimeType = video.mimeType("video")
    val contentValues = ContentValues().apply {
        put(MediaStore.Video.Media.DISPLAY_NAME, name)
    }.let { editValues?.invoke(it) ?: it }

    val data = FileIOUtils.readFile2BytesByStream(video)
    contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues, data)
}

fun Context.insertAudio(audio: File, editValues: ((ContentValues) -> ContentValues)? = null) {
    val name = audio.name
    val mimeType = audio.mimeType("audio")
    val contentValues = ContentValues().apply {
        put(MediaStore.Audio.Media.DISPLAY_NAME, name)
    }.let { editValues?.invoke(it) ?: it }

    val data = FileIOUtils.readFile2BytesByStream(audio)
    contentResolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentValues, data)
}

@RequiresApi(Build.VERSION_CODES.Q)
fun Context.insertDownload(download: File, editValues: ((ContentValues) -> ContentValues)? = null) {
    val name = download.name
    val mimeType = download.mimeTypeOrThrow()
    val contentValues = ContentValues().apply {
        put(MediaStore.Audio.Media.DISPLAY_NAME, name)
    }.let { editValues?.invoke(it) ?: it }

    val data = FileIOUtils.readFile2BytesByStream(download)
    contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues, data)
}

fun Context.queryAudio(): List<Audio> {
    val projection: Array<String>? = null
    val selection: String? = null
    val selectionArgs: Array<String>? = null
    val sortOrder: String? = null
    return contentResolver.query(
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        projection,
        selection,
        selectionArgs,
        sortOrder
    )?.use {
        it.readRows()
    } ?: emptyList()
}

fun Context.queryRingtone(): List<Ringtone> = RingtoneManager(this).cursor.use { it.readRows() }

//this： File path
fun String.getMediaInfo(
    frameAtTime: Long = 0
): MediaInfo = MediaMetadataRetriever().let { retriever ->
    retriever.setDataSource(this)
    val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        ?.toLongOrNull()
        ?: -1
    val cover = retriever.getFrameAtTime(frameAtTime)
    retriever.release()
    MediaInfo(this, duration, cover)
}

//this： File path
fun String.frameAtTime(timeUs: Long = 0): Bitmap? =
    MediaMetadataRetriever().let { retriever ->
        retriever.setDataSource(this)
        val cover = retriever.getFrameAtTime(timeUs)
        retriever.release()
        cover
    }

//this: File path
fun String.frameAtTimeAsFile(
    context: Context,
    header: Map<String, String> = emptyMap(),
    timeUs: Long = 0L
): File? =
    tryOrNull { frameAtTime(timeUs)?.use { it.compressToTempFile(context) } }

//this： network url
fun String.getMediaInfoByNet(
    header: Map<String, String> = emptyMap(),
    frameAtTime: Long = 0
): MediaInfo = MediaMetadataRetriever().let { retriever ->
    retriever.setDataSource(this, header)
    val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        ?.toLongOrNull()
        ?: -1
    val cover = retriever.getFrameAtTime(frameAtTime)
    retriever.release()
    MediaInfo(this, duration, cover)
}

//this： network url
fun String.frameAtTimeByNet(header: Map<String, String> = emptyMap(), timeUs: Long = 0): Bitmap? =
    MediaMetadataRetriever().let { retriever ->
        retriever.setDataSource(this, header)
        val cover = retriever.getFrameAtTime(timeUs)
        retriever.release()
        cover
    }
