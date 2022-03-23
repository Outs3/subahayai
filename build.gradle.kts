// Top-level build file where you can add configuration options common to all sub-projects/modules.
//buildscript {
//    repositories {
//        google()
//        mavenCentral()
//        importMavens()
//    }
//    dependencies {
//        classpath("com.android.tools.build:gradle:7.1.2")
//        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN_VERSION}")
//
//        // NOTE: Do not place your application dependencies here; they belong
//        // in the individual module build.gradle files
//    }
//}

plugins {
    id("com.android.application").version("7.1.2").apply(false)
    id("com.android.library").version("7.1.2").apply(false)
    id("org.jetbrains.kotlin.android").version(Versions.KOTLIN_VERSION).apply(false)
    id("org.jetbrains.kotlin.jvm") version "1.6.10" apply false
//    id("org.jetbrains.kotlin:kotlin-gradle-plugin").version(Versions.KOTLIN_VERSION).apply(false)
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}