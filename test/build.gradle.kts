plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    implementation(project(":${ConfigData.MODULE_KTS}"))
    testImplementation(kotlin("test"))
}

tasks.test {
//    useJUnitPlatform()
}
