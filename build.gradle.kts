plugins {
    id("com.android.application") version "7.1.3" apply false
    id("com.android.library") version "7.1.3" apply false
    id("org.jetbrains.kotlin.android") version Versions.KOTLIN_VERSION apply false
    id("org.jetbrains.kotlin.jvm") version Versions.KOTLIN_VERSION apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}