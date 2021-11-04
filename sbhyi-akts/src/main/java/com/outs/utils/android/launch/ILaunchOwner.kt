package com.outs.utils.android.launch

import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.appcompat.app.AppCompatActivity
import com.outs.utils.android.hasPermission

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/8/25 20:49
 * desc: 解耦BaseActivity与startActivityForResult
 */
interface ILaunchOwner {

    fun getActivity(): AppCompatActivity

    fun getLauncher(): SimpleActivityLauncher

    fun startActivityForResult(
        intent: Intent,
        onActivityResult: ActivityResultCallback<ActivityResult>
    ) = getLauncher().launch(intent, onActivityResult)

    fun requestPermission(permission: String, onActivityResult: ActivityResultCallback<Boolean>) =
        getLauncher().requestPermission(permission, onActivityResult)

    fun requestMultiPermission(
        permissions: Array<String>,
        onActivityResult: ActivityResultCallback<Map<String, Boolean>>
    ) = getLauncher().requestMultiPermission(permissions, onActivityResult)

    suspend fun launch(input: Intent): ActivityResult = getLauncher().launch(input)

    suspend fun requestPermission(input: String): Boolean = getActivity().hasPermission(input)
        .let { has -> if (has) true else getLauncher().requestPermission(input) }

    suspend fun permissionOrThrow(input: String) {
        if (!getActivity().hasPermission(input) && !getLauncher().requestPermission(input)) {
            throw RuntimeException(input)
        }
    }

    suspend fun requestMultiPermission(input: Array<String>): Map<String, Boolean> =
        input.filter { !getActivity().hasPermission(it) }.takeIf { it.isNotEmpty() }
            ?.let { getLauncher().requestMultiPermission(it.toTypedArray()) }
            ?: emptyMap()

    suspend fun permissionsOrThrow(input: Array<String>) {
        if (
            !input.all { getActivity().hasPermission(it) }
            && !getLauncher().requestMultiPermission(input).all { it.value }
        ) {
            throw RuntimeException(input.joinToString(","))
        }
    }

}