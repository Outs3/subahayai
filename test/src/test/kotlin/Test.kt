import com.outs.utils.kotlin.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.test.Test

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2022/3/23 16:33
 * desc:
 */
class Test {

    companion object {
        init {
            setLogger(object : ILogger {
                override fun d(tag: String?, msg: String) {
                    println(
                        "[${tag ?: "LogByOu"}][${
                            System.currentTimeMillis().let(::Date).simpleFormat()
                        }]$msg"
                    )
                }

                override fun e(tag: String?, msg: String) {
                    System.err.println(
                        "[${tag ?: "LogByOu"}][${
                            System.currentTimeMillis().let(::Date).simpleFormat()
                        }]$msg"
                    )
                }

            })
        }
    }

    @Test
    fun test1() {
        fun Long.isInvalid(): Boolean =
            System.currentTimeMillis() - this >= TimeUnit.DAYS.toMicros(1)

        val src = System.currentTimeMillis() - 1000 * 60 * 60 * 24 - 50
        src.isInvalid().d()
    }

    @org.junit.Test
    fun test2() {
        val input = """
        org.jetbrains.kotlin:kotlin-android-extensions-runtime:1.6.10
        org.jetbrains.kotlin:kotlin-parcelize-runtime:1.6.10
        org.jetbrains.kotlin:kotlin-reflect:1.4.31
        org.jetbrains.kotlin:kotlin-reflect:1.6.10
        org.jetbrains.kotlin:kotlin-reflect:1.6.10
        org.jetbrains.kotlin:kotlin-stdlib:1.4.31
        org.jetbrains.kotlin:kotlin-stdlib:1.6.10
        org.jetbrains.kotlin:kotlin-stdlib:1.6.10
        org.jetbrains.kotlin:kotlin-stdlib-common:1.4.31
        org.jetbrains.kotlin:kotlin-stdlib-common:1.6.10
        org.jetbrains.kotlin:kotlin-stdlib-common:1.6.10
        org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.4.31
        org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.6.10
        org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.6.10
        org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.4.31
        org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.10
        org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.10
        org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0
        org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.6.0
        org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.6.0
        org.jetbrains.kotlinx:kotlinx-coroutines-test-jvm:1.6.0
    """.trimIndent()

        data class Dependency(val group: String, val module: String, val version: String) {
            override fun toString(): String = "$group:$module:$version"
            fun toExclude() = "exclude(group = \"$group\", module = \"$module\")"
            fun toApi() = "api(\"$group:$module:$version\")"
        }

        fun dependencyOfString(s: String): Dependency {
            val names = s.split(":").toMutableList()
            val version = names.removeLast().trim()
            val module = names.removeLast().trim()
            val group = names.joinToString(".").trim()
            return Dependency(group, module, version)
        }

        input.split("\n")
            .asSequence()
            .mapNotNull { it.trim().emptyToNull() }
            .map(::dependencyOfString)
            .associateBy(Dependency::module)
            .toList()
            .map(Pair<String, Dependency>::second).toList()
            .also { dependencies ->
                dependencies.joinToString("\n", transform = Dependency::toExclude)
                    .let { "exclude dependencies: \n$it\n" }
                    .d()
                dependencies.joinToString("\n", transform = Dependency::toApi)
                    .let { "api dependencies: \n$it\n" }.d()
            }
    }

    @org.junit.Test
    fun test3() {
        Integer.valueOf("1.0.9").d()
    }

}