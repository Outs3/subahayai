package com.outs.demo_compose.subahayai.compose

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.outs.demo_compose.subahayai.data.*
import com.outs.demo_compose.subahayai.ui.theme.TComposeTheme

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/10/11 16:24
 * desc:
 */
val randomUser: User get() = User(randomLong, randomName, randomIcon, randomSign)

val randomMessage: Message
    get() = Message(
        randomLong,
        randomUser,
        randomUser,
        randomBoolean,
        TextMessageContent(randomLong, 0, randomString)
    )

@Composable
fun CMessage(message: Message) {
    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colors.secondary, CircleShape),
            painter = painterResource(message.fromUser.icon), contentDescription = "user icon"
        )
        Spacer(modifier = Modifier.width(8.dp))

        var isExpanded by remember { mutableStateOf(false) }
        val surfaceColor by animateColorAsState(if (isExpanded) MaterialTheme.colors.primary else MaterialTheme.colors.surface)

        Column(modifier = Modifier.clickable {
            isExpanded = !isExpanded
        }) {
            Text(
                text = message.fromUser.name,
                color = MaterialTheme.colors.secondaryVariant,
                style = MaterialTheme.typography.subtitle2
            )
            Spacer(modifier = Modifier.width(4.dp))
            Surface(
                shape = MaterialTheme.shapes.medium,
                elevation = 1.dp,
                color = surfaceColor,
                modifier = Modifier.animateContentSize().padding(1.dp)
            ) {
                Text(
                    text = message.content.desc,
                    modifier = Modifier.padding(all = 4.dp),
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

@Composable
fun CConversation(messages: List<Message>) {
    Column {
        TestConstraintLayout()
        LazyColumn { items(messages) { message -> CMessage(message) } }
    }
}

@Composable
fun TestConstraintLayout() {
    ConstraintLayout {
        val (btn, text) = createRefs()
        Button(
            onClick = {},
            modifier = Modifier.constrainAs(btn) {
                top.linkTo(parent.top, margin = 16.dp)
            }
        ) {
            Text("Button")
        }
        Text(text = "Text", modifier = Modifier.constrainAs(text) {
            top.linkTo(btn.bottom, margin = 16.dp)
        })
    }
}

@Preview(showBackground = true)
@Composable
fun ChatPreview() {
    TComposeTheme {
        CConversation(
            (0 until 5).map { randomMessage }
        )
    }
}
