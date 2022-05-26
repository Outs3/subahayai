package com.outs.demo_compose.subahayai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.outs.core.android.compose.widgets.BackIcon
import com.outs.core.android.compose.widgets.TitleBar
import com.outs.demo_compose.subahayai.compose.CConversation
import com.outs.demo_compose.subahayai.compose.randomMessage
import com.outs.demo_compose.subahayai.ui.theme.TComposeTheme
import com.outs.utils.android.d
import com.outs.utils.android.startActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        "MainActivity".d()
        setContent {
            TComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colorScheme.background) {
                    Column {
                        var show by remember { mutableStateOf(false) }
                        Button(onClick = { show = !show }) {
                            Text(text = "切换")
                        }
                        Text(text = "标题内容", modifier = Modifier.align(Alignment.CenterHorizontally))
                        if (show)
                            TitleBar(title = "标题内容", withBack = true, withMore = false)
                        TitleBar(
                            title = "标题内容",
                            contentLeft = {
                                BackIcon()
                            },
                            contentRight = {
                                Text(
                                    text = "完成",
                                    modifier = Modifier
                                        .clickable(onClick = {})
                                        .padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                                Text(
                                    text = "删除", modifier = Modifier
                                        .clickable(onClick = {})
                                        .padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }
                        )
                        CConversation((0 until 5).map { randomMessage })
//                    AppointPage(title = "预约")
                        Text(
                            modifier = Modifier
                                .clickable {
                                    startActivity<ListComposePage>()
                                },
                            text = "ListPage"
                        )
                    }
                }
            }
        }
//        injectDebugDialogBug(this)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointPage(title: String, subTitle: String? = null, menus: List<String>? = null) {
//    Scaffold(
//        topBar = {
//            TitleBar(title = title, withBack = true, withMore = true)
////            SmallTopAppBar(
////                navigationIcon = {
////                    Image(
////                        modifier = Modifier
////                            .size(width = 40.dp, height = 38.dp)
////                            .padding(vertical = 10.dp, horizontal = 15.dp),
////                        painter = painterResource(com.outs.demo_compose.R.drawable.icon_back),
////                        contentDescription = "back button",
////                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
////                    )
////                },
////                title = {
////                    Column {
////                        Text(text = title, color = MaterialTheme.colorScheme.onPrimary)
////                        subTitle?.let {
////                            Text(text = subTitle, color = MaterialTheme.colorScheme.secondary)
////                        }
////                    }
////                }
////            )
//        },
//        snackbarHost = {
//            Text(text = "Menu 1")
//            Text(text = "Menu 2")
//        },
//        content = {
//            Column {
//                repeat(50) {
//                    Text(
//                        text = "暂无数据",
//                        textAlign = TextAlign.Center
//                    )
//                }
//            }
//        }
//    )

}

//@Preview(showBackground = true)
@Composable
fun AppointPreview() {
    TComposeTheme {
        AppointPage(title = "预约")
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Compose $name!")
}

@Preview(showBackground = true, widthDp = 375, heightDp = 675)
@Composable
fun DefaultPreview() {
    TComposeTheme {
        Greeting("Android")
    }
}