package com.outs.core.android.compose.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.outs.core.android.compose.textUnitDp
import com.outs.core.android.compose.theme.Gray999

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2022/3/28 18:27
 * desc:
 */
@Composable
fun Dropdown(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center,
    text: String? = null,
    hint: String = "",
    textColor: Color = MaterialTheme.colors.primary,
    hintColor: Color = Gray999,
    content: @Composable BoxScope.() -> Unit = {}
) {
    Box(
        modifier = modifier.padding(5.dp),
        contentAlignment = contentAlignment
    ) {
        Row {
            Text(
                text = text ?: hint,
                color = if (null == text) hintColor else textColor,
                fontSize = 16.textUnitDp(),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "展开",
                modifier = Modifier.padding(start = 2.dp),
                tint = hintColor
            )
        }
        content()
    }
}

@Composable
fun Spinner(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center,
    opts: Array<String>,
    textColor: Color = MaterialTheme.colors.primary,
    hintColor: Color = Gray999,
    current: String? = null,
    onChecked: (Int, String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Dropdown(
        modifier = modifier.clickable { expanded = !expanded },
        contentAlignment = contentAlignment,
        text = current,
        hint = opts.firstOrNull() ?: "",
        textColor = textColor,
        hintColor = hintColor
    ) {
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            opts.forEachIndexed { index, s ->
                DropdownMenuItem(onClick = {
                    expanded = false
                    onChecked(index, s)
                }) {
                    Text(text = s)
                }
            }
        }
    }
}

@Composable
fun <T> Spinner(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center,
    text: (T?) -> String?,
    opts: List<T>,
    textColor: Color = MaterialTheme.colors.primary,
    hintColor: Color = Gray999,
    current: T? = null,
    onChecked: (Int, T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Dropdown(
        modifier = modifier,
        contentAlignment = contentAlignment,
        text = text(current),
        hint = text(opts.firstOrNull()) ?: "",
        textColor = textColor,
        hintColor = hintColor
    ) {
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            opts.forEachIndexed { index, item ->
                DropdownMenuItem(onClick = { onChecked(index, item) }) {
                    Text(text = text(item) ?: "")
                }
            }
        }
    }
}