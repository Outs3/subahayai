package com.outs.core.android.compose.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.outs.core.android.compose.textUnitDp
import com.outs.core.android.compose.theme.Gray333
import com.outs.core.android.compose.theme.Gray666
import com.outs.core.android.compose.theme.GrayCCC

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2022/4/9 11:02
 * desc:
 */

@Preview(widthDp = 375, heightDp = 675)
@Composable
private fun PreviewInputs() {
    val text = remember { mutableStateOf("打击你") }
    Column(modifier = Modifier.padding(10.dp)) {
        Text(text = "昵称：")
        LineInput(
            modifier = Modifier.padding(top = 15.dp),
            text = text,
            hint = "请输入昵称"
        )
        LabelInput(
            modifier = Modifier.padding(top = 15.dp),
            title = "昵称",
            text = text,
            hint = "请输入昵称"
        )
        CardTextField(
            modifier = Modifier.padding(top = 15.dp),
            text = text,
            title = "昵称",
            hint = "请输入昵称"
        )
    }
}

@Composable
fun LineInput(
    modifier: Modifier = Modifier,
    text: MutableState<String>,
    hint: String = "请输入内容",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    LineTextField(
        value = text.value,
        onValueChange = { text.value = it },
        modifier = modifier.height(30.dp),
        placeholder = {
            Text(
                text = hint,
                modifier = Modifier.padding(horizontal = 4.dp),
                color = GrayCCC,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )
}

@Composable
fun LabelInput(
    modifier: Modifier = Modifier,
    inputModifier: Modifier = Modifier,
    title: String,
    text: MutableState<String>,
    hint: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    LabelInput(
        modifier = modifier,
        title = title,
        input = {
            LineInput(
                modifier = inputModifier,
                text = text,
                hint = hint,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
            )
        }
    )
}

@Composable
fun LabelInput(
    modifier: Modifier = Modifier,
    title: String,
    titlePaddingEnd: Dp = 12.dp,
    input: @Composable RowScope.() -> Unit
) {
    @Composable
    fun LabelTitle(modifier: Modifier = Modifier, text: String) {
        Text(text = text, modifier = modifier, color = Gray666, fontSize = 15.textUnitDp())
    }
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        LabelTitle(modifier = Modifier.padding(end = titlePaddingEnd), text = title)
        input()
    }
}

@Composable
fun CardTextField(
    modifier: Modifier = Modifier,
    text: MutableState<String>,
    title: String,
    hint: String,
    enabled: Boolean = true,
    fontSize: TextUnit = 15.textUnitDp()
) {
    CardTextField(
        modifier = modifier,
        value = text.value,
        onValueChanged = { text.value = it },
        enabled = enabled,
        title = title,
        hint = hint,
        fontSize = fontSize
    )
}

@Composable
fun CardTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChanged: (String) -> Unit,
    title: String,
    hint: String,
    enabled: Boolean = true,
    fontSize: TextUnit = 15.textUnitDp()
) {
    DecorationTextField(
        modifier = modifier
            .background(color = Color.White, shape = RoundedCornerShape(10.dp))
            .padding(horizontal = 12.dp, vertical = 20.dp),
        value = value,
        onValueChange = onValueChanged,
        enabled = enabled,
        textStyle = TextStyle(
            color = Gray333,
            fontSize = fontSize
        ),
        contentStart = {
            Box(modifier = Modifier.padding(end = 15.dp)) {
                Text(text = title, color = Gray666, fontSize = fontSize)
            }
        },
        contentEnd = {
            if (value.isNotEmpty() && enabled) {
                Icon(
                    imageVector = Icons.Rounded.Cancel,
                    contentDescription = "清除输入",
                    modifier = Modifier
                        .clickable(onClick = { onValueChanged("") })
                        .padding(5.dp)
                        .size(15.dp),
                    tint = GrayCCC
                )
            }
        },
        placeholder = {
            Text(
                text = hint,
                color = GrayCCC,
                fontSize = fontSize,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    )
}
