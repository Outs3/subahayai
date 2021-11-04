package com.outs.utils.android.launch

import android.content.Context
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.outs.utils.android.newTempFile
import com.outs.utils.android.provide
import com.outs.utils.kotlin.d
import java.io.File

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/10/26 10:30
 * desc:
 */
class TakePictureLauncher(activity: AppCompatActivity) :
    ActivityLauncher<Uri, Boolean>(activity, ActivityResultContracts.TakePicture()) {

    fun takePhotoAsUri(
        context: Context,
        authority: String = com.outs.utils.android.getFileProviderAuthority(),
        onSuccess: (Uri) -> Unit
    ) {
        val tempFile = context.newTempFile()
        val uri = tempFile.provide(context = context, authority = authority)
        launch(uri) { success ->
            "takePicture: $success filePath: ${tempFile.path} uri: $uri".d()
            if (success) {
                onSuccess(uri)
            }
        }
    }

    fun takePhotoAsFile(
        context: Context,
        authority: String = com.outs.utils.android.getFileProviderAuthority(),
        onSuccess: (File) -> Unit
    ) {
        val tempFile = context.newTempFile()
        val uri = tempFile.provide(context = context, authority = authority)
        launch(uri) { success ->
            "takePicture: $success filePath: ${tempFile.path} uri: $uri".d()
            if (success) {
                onSuccess(tempFile)
            }
        }
    }

}