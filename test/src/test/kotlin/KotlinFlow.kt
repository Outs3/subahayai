import com.outs.utils.kotlin.d
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlin.system.exitProcess
import kotlin.test.Test

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2022/3/23 16:47
 * desc:
 */
class KotlinFlow {

    @Test
    fun testFlow1() {
        val flow = MutableStateFlow("akdlk")
//        runBlocking {
//            "runBlocking1: ".d()
//            repeat(5) {
//                "emit: $it".d()
//                flow.emit(it.toString())
//            }
//            "runBlocking1: End".d()
//        }
        CoroutineScope(Dispatchers.IO).launch {
            "runBlocking1: ".d()
            repeat(5) {
                "emit: $it".d()
                flow.emit(it.toString())
                delay(1000)
            }
            "runBlocking1: End".d()
        }
        runBlocking {
            "runBlocking2: ".d()
            flow.collect { s ->
                "collect: $s".d()
            }
        }

    }

    @DelicateCoroutinesApi
    @Test
    fun testFlow2() {
        class LiveData<T>(def: T) {
            var value: T = def
                set(value) {
                    field = value
                    observer(value)
                }
            var observer: (T) -> Unit = {}

            fun asFlow() = flow {
                val channel = Channel<T>(Channel.CONFLATED)
                this@LiveData.observer = { channel.trySend(it) }
                withContext(Dispatchers.IO) {
                    "observeForever".d()
                }
                try {
                    for (value in channel) {
                        emit(value)
                    }
                } finally {
                    GlobalScope.launch(Dispatchers.IO) {
                        "removeObserver".d()
                    }
                }
            }

        }

        val viewModelScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        val liveData = LiveData("我是初始化字符串")
        val flow = liveData.asFlow().stateIn(viewModelScope, SharingStarted.Lazily, liveData.value)
        CoroutineScope(Dispatchers.IO).launch {
            "runBlocking1: ".d()
            repeat(5) {
                delay(1000)
                "emit: $it".d()
                liveData.value = it.toString()
            }
            "runBlocking1: End".d()
            exitProcess(0)
        }
        runBlocking {
            "runBlocking2: ".d()
            flow.collect { s ->
                "collect: $s".d()
            }
        }
    }

    @Test
    fun testFlow3() {
        val flow = MutableStateFlow("akdlk")
        flow.transform {
            emit(1)
        }
            .distinctUntilChanged()
    }
}