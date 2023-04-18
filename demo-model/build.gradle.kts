plugins {
    id("kotlin")
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    implementation(project(":${ConfigData.MODULE_KTS}"))
}
