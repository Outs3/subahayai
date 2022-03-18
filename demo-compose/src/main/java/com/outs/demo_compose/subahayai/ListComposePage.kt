package com.outs.demo_compose.subahayai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.outs.core.android.compose.SwipeRefreshList
import com.outs.demo_compose.subahayai.ui.theme.TComposeTheme
import com.outs.utils.android.IntPagingSource
import com.outs.utils.android.d
import kotlinx.coroutines.delay

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2022/3/18 9:39
 * desc:
 */
class ListComposePage : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pager = Pager(PagingConfig(pageSize = 20)) {
            MessagePagingSource(API())
        }

        setContent {
            TComposeTheme {
                MessageList(pager = pager)
            }
        }
    }

    data class User(val id: Long, val name: String, val avatar: String, val sex: Int)

    data class MessageBody(
        val id: Long,
        val type: Int,
        val text: String? = null,
        val image: String? = null,
        val video: String? = null
    )

    data class Message(
        val id: Long,
        val from: User,
        val to: User,
        val isSender: Boolean = true,
        val content: MessageBody
    )

    class API {
        private var keyId: Long = 0

        private val SELF = User(0, "我是你大爷", "", 0)
        private val TARGET = User(1, "你好", "", 1)

        private fun textMessageBody(text: String) = MessageBody(2, 0, text)

        private fun data(): List<Message> = listOf(
            Message(++keyId, SELF, TARGET, true, textMessageBody("你好")),
            Message(++keyId, TARGET, SELF, false, textMessageBody("哦啊到来的离开")),
            Message(++keyId, TARGET, SELF, false, textMessageBody("发了快递费克里斯方面考虑试客联盟")),
            Message(++keyId, SELF, TARGET, true, textMessageBody("123")),
            Message(++keyId, SELF, TARGET, true, textMessageBody("987654321")),
            Message(++keyId, TARGET, SELF, false, textMessageBody("m,vcmkxv,xm,v")),
            Message(++keyId, SELF, TARGET, true, textMessageBody("我不好"))
        )

        suspend fun loadMessage(pageNum: Int, pageSize: Int = 20): List<Message> {
            "loadMessage($pageNum, $pageSize)".d()
            if (10 == pageNum) return emptyList()
            delay(1200)
            return data()
        }

    }

    class MessagePagingSource(private val service: API) : IntPagingSource<Message>() {
        override suspend fun loadDataOrThrow(pageNum: Int): List<Message> =
            service.loadMessage(pageNum)
    }

    @Composable
    fun MessageList(pager: Pager<Int, Message>) {
        SwipeRefreshList(
            modifier = Modifier
                .background(Color(0xFFF1F1F1))
                .fillMaxSize(),
            pager = pager,
            lazyColumn = { content ->
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    content = content
                )
            },
            key = Message::id,
            item = { message ->
                if (null != message) MessageRow(message = message)
            })
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun MessageList(messages: List<Message>) {
        var last: Int = -1
        var lastUser: User? = null
        val groupMessages =
            messages.groupBy {
                if (null == lastUser || lastUser != it.from) {
                    lastUser = it.from
                    ++last
                } else last
            }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF1F1F1)),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            groupMessages.forEach { entry ->
                stickyHeader(key = entry.key) {
                    MessageHeader(entry)
                }
                items(items = entry.value, key = Message::id) { message -> MessageRow(message) }
            }
        }
    }

    @Composable
    fun MessageHeader(entry: Map.Entry<Int, List<Message>>) {
        Box(Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .background(Color(0xFFCCCCCC), RoundedCornerShape(5.dp))
                    .padding(5.dp),
                textAlign = TextAlign.Center,
                text = entry.value.firstOrNull()?.from?.name ?: ""
            )
        }
    }

    @Composable
    fun MessageRow(message: Message) {
        Text(
            modifier = Modifier
                .background(Color.LightGray, RoundedCornerShape(5.dp))
                .padding(5.dp),
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Magenta)) {
                    append(message.from.name)
                }
                append(" :  ")
                withStyle(style = SpanStyle(color = Color(0xFF666666))) {
                    append(message.content.text ?: "")
                }
            }
        )
    }

}
