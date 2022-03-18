package com.outs.demo_compose.subahayai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                Surface(color = MaterialTheme.colors.background) {
                    CConversation(
                        (0 until 5).map { randomMessage }
                    )
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
//        injectDebugDialogBug(this)
    }
}

@Composable
fun AppointPage(title: String, subTitle: String? = null, menus: List<String>? = null) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Image(
                        modifier = Modifier
                            .size(width = 40.dp, height = 38.dp)
                            .padding(vertical = 10.dp, horizontal = 15.dp),
                        painter = painterResource(com.outs.demo_compose.R.drawable.icon_back),
                        contentDescription = "back button",
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary)
                    )
                },
                title = {
                    Column {
                        Text(text = title, color = MaterialTheme.colors.onPrimary)
                        subTitle?.let {
                            Text(text = subTitle, color = MaterialTheme.colors.secondary)
                        }
                    }
                }
            )
        },
        drawerContent = {
            Text(text = "Menu 1")
            Text(text = "Menu 2")
        },
        content = {
            Column {
                repeat(50) {
                    Text(
                        text = "暂无数据",
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    )

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