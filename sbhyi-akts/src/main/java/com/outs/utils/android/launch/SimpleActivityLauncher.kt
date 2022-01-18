package com.outs.utils.android.launch

import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.appcompat.app.AppCompatActivity
import com.outs.utils.android.newMultiplePermissionLauncher
import com.outs.utils.android.newPermissionLauncher
import com.outs.utils.android.newResultLauncher

/**
 * 必须在onCreate及之前创建该Launcher 不能在onStart及以后再去初始化 不能用Lazy
 */
class SimpleActivityLauncher(activity: AppCompatActivity) {

    val resultLauncher = activity.newResultLauncher()
    val permissionLauncher = activity.newPermissionLauncher()
    val multiplePermissionLauncher = activity.newMultiplePermissionLauncher()

    fun launch(i: Intent, onActivityResult: ActivityResultCallback<ActivityResult>) {
        resultLauncher.launch(i, onActivityResult)
    }

    fun requestPermission(
        permission: String,
        onActivityResult: ActivityResultCallback<Boolean>
    ) {
        permissionLauncher.launch(permission, onActivityResult)
    }

    fun requestMultiPermission(
        permissions: Array<String>,
        onActivityResult: ActivityResultCallback<Map<String, Boolean>>
    ) {
        multiplePermissionLauncher.launch(permissions, onActivityResult)
    }

    suspend fun launch(i: Intent): ActivityResult = resultLauncher.launch(i)

    suspend fun requestPermission(permission: String): Boolean =
        permissionLauncher.launch(permission)

    suspend fun requestMultiPermission(permissions: Array<String>): Map<String, Boolean> =
        multiplePermissionLauncher.launch(permissions)


}

