plugins {
    id("org.jetbrains.kotlin.jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation(project(":${ConfigData.MODULE_KTS}"))
    testImplementation(kotlin("test"))
}

tasks.test {
//    useJUnitPlatform()
}

//tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
//    kotlinOptions.jvmTarget = "1.8"
//}
