plugins {
    id("com.android.application") version "7.4.0-alpha02" apply false
    id("com.android.library") version "7.4.0-alpha02" apply false
    id("org.jetbrains.kotlin.android") version Versions.KOTLIN_VERSION apply false
    id("org.jetbrains.kotlin.jvm") version Versions.KOTLIN_VERSION apply false
    `maven-publish`
}

allprojects {
    if (name.startsWith("sbhyi")) {
        group = ConfigData.GROUP_NAME
        version = Versions.SBHYI_VERSION
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}