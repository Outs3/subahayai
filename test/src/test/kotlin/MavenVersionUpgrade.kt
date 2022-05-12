import com.outs.utils.kotlin.*
import com.outs.utils.kotlin.http.ApiException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import maven.central.Page
import maven.central.Repository
import maven.central.Response
import okhttp3.Request
import java.nio.charset.Charset
import kotlin.test.Test

object MavenVersionUpgrade {

    private val UTF8: Charset = Charset.forName("UTF-8")
    private const val base = "https://search.maven.org/solrsearch/"
    private const val API_SELECT = "select"
    private const val PAGE_START = 0
    private const val PAGE_COUNT = 20
    private val okHttpClient by lazy { simpleOkHttpClient() }

    private fun buildUrl(
        api: String,
        page: Int = PAGE_START,
        size: Int = PAGE_COUNT,
        vararg args: Pair<String, Any?>
    ): String = mutableListOf(*args).apply {
        add(Pair("start", page))
        add(Pair("rows", size))
    }
        .filter { null != it.second }
        .joinToString(
            separator = "&",
            transform = { pair -> "${pair.first}=${pair.second}" }
        )
        .let { params -> "$base$api?$params" }

    fun select(
        page: Int = PAGE_START,
        size: Int = PAGE_COUNT,
        groupId: String,
        artifactId: String? = null
    ): Page<Repository> {
        val qParams = listOf(
            "g" to groupId,
            "a" to artifactId
        )
            .filter { null != it.second }
            .joinToString(
                separator = "%20AND%20",
                transform = { pair -> "${pair.first}:${pair.second}" }
            )
        val url = buildUrl(api = API_SELECT, page = page, size = size, "q" to qParams)
        val request = Request.Builder().url(url).get().build()
        val call = okHttpClient.newCall(request)
        val response = call.execute()
        if (!response.isSuccessful) throw ApiException(response.code, response.message, response)
        val bodyString = response.body?.string() ?: throw ApiException(-1, "空的响应体")
        val apiResponse = bodyString.asObj<Response<Page<Repository>>>()
        if (!apiResponse.isSuccessful) throw ApiException(
            apiResponse.code,
            apiResponse.message,
            apiResponse
        )
        return apiResponse.response
    }
}

class MavenVersionUpgradeTest {

    fun getOne(groupId: String = "com.squareup.retrofit2", artifactId: String = "converter-gson") {
        val data = MavenVersionUpgrade.select(groupId = groupId, artifactId = artifactId)
        data.d()
    }

    @Test
    fun test1() {
        runBlocking {
            withContext(Dispatchers.IO) {
                try {
                    getOne()
                } catch (e: Throwable) {
                    e.e()
                }
            }
        }
    }

    @Test
    fun test2() {
        runBlocking {
            withContext(Dispatchers.IO) {
                try {
                    val text = """
    //kotlin
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:{Versions.KOTLIN_COROUTINES_VERSION}")
    api("org.jetbrains.kotlin:kotlin-reflect:{Versions.KOTLIN_VERSION}")
    //json
    api("com.google.code.gson:gson:2.9.0")
    //OkHttp
    api("com.squareup.okhttp3:okhttp:4.9.3")
    //Retrofit
    api("com.squareup.retrofit2:retrofit:2.9.0")
    api("com.squareup.retrofit2:converter-gson:2.9.0")
    //kotlin
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:{Versions.KOTLIN_COROUTINES_VERSION}")

    //ktx
    api("androidx.core:core-ktx:1.7.0")
    api("androidx.collection:collection-ktx:1.2.0")
    api("androidx.activity:activity-ktx:1.5.0-alpha05")
    api("androidx.fragment:fragment-ktx:1.5.0-alpha05")
    api("androidx.lifecycle:lifecycle-runtime-ktx:2.5.0-alpha06")
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.0-alpha06")
    api("androidx.lifecycle:lifecycle-livedata-ktx:2.5.0-alpha06")
    api("androidx.lifecycle:lifecycle-reactivestreams-ktx:2.5.0-alpha06")
    api("androidx.room:room-ktx:2.4.2")

    //view
    api("androidx.appcompat:appcompat:1.4.1")
    api("androidx.recyclerview:recyclerview:1.2.1")
    api("androidx.constraintlayout:constraintlayout:2.1.3")
    api("androidx.viewpager2:viewpager2:1.1.0-beta01")
    api("com.google.android.material:material:1.5.0")
    api("com.google.android.flexbox:flexbox:3.0.0")

    //image cache
    api("com.github.bumptech.glide:glide:4.13.2")
    kapt("com.github.bumptech.glide:compiler:4.13.1")
//    api("com.github.bumptech.glide:okhttp3-integration:4.11.0")

    //Log
    api("com.orhanobut:logger:2.2.0")

    //blankj utils
    api("com.blankj:utilcodex:1.31.0")

    //paging3
    api("androidx.paging:paging-runtime:{Versions.COMPOSE_PAGING_VERSION}")
    testApi("androidx.paging:paging-common:{Versions.COMPOSE_PAGING_VERSION}")

    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.test:core:1.4.0")
    testImplementation("org.mockito:mockito-core:3.3.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    //multidex
    api("androidx.multidex:multidex:2.0.1")

    //refresh-layout-view
    api("io.github.scwang90:refresh-layout-kernel:2.0.5")      //核心必须依赖
    api("io.github.scwang90:refresh-header-classics:2.0.5")    //经典刷新头
    api("io.github.scwang90:refresh-header-material:2.0.5")    //谷歌刷新头
    api("io.github.scwang90:refresh-footer-classics:2.0.5")    //经典加载

    // Compose UI
    api("androidx.compose.ui:ui:{Versions.COMPOSE_VERSION}")
    // Tooling support (Previews, etc.)
    api("androidx.compose.ui:ui-tooling:{Versions.COMPOSE_VERSION}")
    // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
    api("androidx.compose.foundation:foundation:{Versions.COMPOSE_VERSION}")
    // Material Design
    api("androidx.compose.material:material:{Versions.COMPOSE_VERSION}")
    // Material design icons
    api("androidx.compose.material:material-icons-core:{Versions.COMPOSE_VERSION}")
    api("androidx.compose.material:material-icons-extended:{Versions.COMPOSE_VERSION}")
    // Integration with observables
    api("androidx.compose.runtime:runtime-livedata:{Versions.COMPOSE_VERSION}")
    api("androidx.compose.runtime:runtime-rxjava2:{Versions.COMPOSE_VERSION}")

    //jetpack
    api("androidx.activity:activity-compose:1.4.0")
    api("androidx.constraintlayout:constraintlayout-compose:1.0.0-rc02")
    api("androidx.navigation:navigation-compose:2.4.1")
    api("androidx.paging:paging-compose:1.0.0-alpha14")

    //compose accompanist
    api("com.google.accompanist:accompanist-permissions:{Versions.COMPOSE_ACCOMPANIST_VERSION}")
    api("com.google.accompanist:accompanist-swiperefresh:{Versions.COMPOSE_ACCOMPANIST_VERSION}")
    api("com.google.accompanist:accompanist-flowlayout:{Versions.COMPOSE_ACCOMPANIST_VERSION}")
    api("com.google.accompanist:accompanist-webview:{Versions.COMPOSE_ACCOMPANIST_VERSION}")

    //coil
    api("io.coil-kt:coil-compose:2.0.0-rc02")

                    """.trimIndent()
                    text.split("\n")
                        .asSequence()
                        .mapNotNull { it.trim().emptyToNull() }
                        .filterNot { it.startsWith("//") }
                        .map { line ->
                            line.subSequence(
                                line.indexOfFirst { c -> '"' == c } + 1,
                                line.indexOfLast { c -> '"' == c }
                            )
                        }
                        .mapNotNull {
                            val ids = it.split(":")
                            tryOrNull { ids[0] to ids[1] }
                        }
                        .distinct()
                        .mapNotNull { pair ->
                            kotlin.runCatching {
                                MavenVersionUpgrade.select(
                                    groupId = pair.first,
                                    artifactId = pair.second
                                )
                            }
                                .onFailure(Throwable::e)
                                .getOrNull()
                                ?.docs
                                ?.firstOrNull()
                        }
                        .joinToString("", transform = { "\n$it" })
                        .d()
                } catch (e: Throwable) {
                    e.e()
                }
            }
        }
    }

}