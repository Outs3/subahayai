package com.outs.utils.android

import com.blankj.utilcode.util.PermissionUtils
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/6/28 16:34
 * desc:
 */
fun permission(
    permissions: Array<out String>,
    onDenied: (List<String>) -> Unit = { denied ->
        "Permission denied: ${denied.joinToString(",")}".toast()
    },
    onAllGranted: () -> Unit,
) {
    PermissionUtils.permission(*permissions)
        .callback { isAllGranted, granted, deniedForever, denied ->
            if (isAllGranted) {
                onAllGranted()
            } else {
                onDenied(denied)
            }
        }
        .request()
}

suspend fun permissionOrThrow(throwMsg: String? = null, vararg permissions: String) =
    suspendCoroutine<Unit> { continuation ->
        PermissionUtils.permission(*permissions)
            .callback { isAllGranted, granted, deniedForever, denied ->
                if (isAllGranted) {
                    continuation.resume(Unit)
                } else {
                    continuation.resumeWithException(
                        RuntimeException(
                            throwMsg ?: "Permission denied: ${
                                denied.joinToString(
                                    ","
                                )
                            }"
                        )
                    )
                }
            }
            .request()
    }