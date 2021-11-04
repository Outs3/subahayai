package com.outs.utils.android

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Consumer
import com.outs.utils.android.launch.ActivityLauncher
import com.outs.utils.android.launch.ILaunchOwner
import com.outs.utils.android.launch.TakePictureLauncher

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/8/25 20:57
 * desc:
 */
inline fun <reified T : Activity> ILaunchOwner.startActivityForResult(
    extras: Bundle? = null,
    withFlags: Int? = null,
    onActivityResult: ActivityResultCallback<ActivityResult>
) = startActivityForResult(getActivity().newIntent<T>(extras, withFlags), onActivityResult)

inline fun <reified T : Activity> ILaunchOwner.startActivityForResultOk(
    extras: Bundle? = null,
    withFlags: Int? = null,
    onActivityResult: Consumer<Intent?>
) = startActivityForResultOk(getActivity().newIntent<T>(extras, withFlags), onActivityResult)

fun ILaunchOwner.startActivityForResultOk(
    intent: Intent,
    onActivityResult: Consumer<Intent?>
) =
    startActivityForResult(intent) { result ->
        if (Activity.RESULT_OK == result.resultCode) {
            onActivityResult.accept(result.data)
        }
    }

fun ILaunchOwner.captureImage(onCapture: (Uri) -> Unit) {
    val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    captureIntent.resolveActivity(getActivity().packageManager)?.let {
        getActivity().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            ContentValues()
        )
            ?.let { cameraUri ->
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri)
                captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                startActivityForResult(captureIntent) { result ->
                    if (Activity.RESULT_OK == result.resultCode) {
                        onCapture(cameraUri)
                    }
                }
            }
    }
}

fun ILaunchOwner.captureVideo(onCapture: (Uri) -> Unit) {
    val captureIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
    captureIntent.resolveActivity(getActivity().packageManager)?.let {
        getActivity().contentResolver.insert(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            ContentValues()
        )
            ?.let { cameraUri ->
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri)
                captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                startActivityForResult(captureIntent) { result ->
                    if (Activity.RESULT_OK == result.resultCode) {
                        onCapture(cameraUri)
                    }
                }
            }
    }
}

fun ILaunchOwner.pickFile(mimeType: String, onPick: (Uri) -> Unit) {
    val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
        type = mimeType
    }
    if (intent.resolveActivity(getActivity().packageManager) != null) {
        startActivityForResult(intent) { result ->
            if (Activity.RESULT_OK == result.resultCode) {
                result.data?.data?.let(onPick)
            }
        }
    }
}

fun ILaunchOwner.pickImage(onPick: (Uri) -> Unit) = pickFile("image/*", onPick)

fun ILaunchOwner.pickVideo(onPick: (Uri) -> Unit) = pickFile("video/*", onPick)

fun ILaunchOwner.pickAudio(onPick: (Uri) -> Unit) = pickFile("audio/*", onPick)

fun AppCompatActivity.newTakePictureLauncher() = TakePictureLauncher(this)

fun AppCompatActivity.newResultLauncher(): ActivityLauncher<Intent, ActivityResult> =
    ActivityLauncher(this, ActivityResultContracts.StartActivityForResult())

fun AppCompatActivity.newPermissionLauncher(): ActivityLauncher<String, Boolean> =
    ActivityLauncher(this, ActivityResultContracts.RequestPermission())

fun AppCompatActivity.newMultiplePermissionLauncher(): ActivityLauncher<Array<String>, Map<String, Boolean>> =
    ActivityLauncher(this, ActivityResultContracts.RequestMultiplePermissions())

