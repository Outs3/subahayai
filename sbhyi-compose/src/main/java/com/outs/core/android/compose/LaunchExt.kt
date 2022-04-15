package com.outs.core.android.compose

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.outs.utils.android.isSuccess

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2022/4/15 11:01
 * desc:
 */
@Composable
fun startActivityForResult(
    intent: () -> Intent,
    onResult: (ActivityResult) -> Unit
): () -> Unit {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = onResult
    )
    return {
        launcher.launch(intent())
    }
}

@Composable
fun startActivityForResultOk(
    intent: () -> Intent,
    onResult: (Intent?) -> Unit
): () -> Unit {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { if (it.isSuccess) onResult(it.data) }
    )
    return {
        launcher.launch(intent())
    }
}

@Composable
fun startActivityForResult(intent: Intent, onResult: (ActivityResult) -> Unit): () -> Unit =
    startActivityForResult(intent = { intent }, onResult = onResult)

@Composable
fun startActivityForResultOk(intent: Intent, onResult: (Intent?) -> Unit): () -> Unit =
    startActivityForResultOk(intent = { intent }, onResult = onResult)
