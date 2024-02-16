plugins {
    id("kotlin")
    `java-library`
    `maven-publish`
}

java {
    compatibility()
    withJavadocJar()
    withSourcesJar()
}

dependencies {
    //kotlin
    api(libs.jetbrains.kotlinx.coroutines.core)
    api(libs.jetbrains.kotlin.reflect)
    //json
    api(libs.google.code.gson)
    //OkHttp
    api(libs.squareup.okhttp3.okhttp)
    //Retrofit
    api(libs.squareup.retrofit2.retrofit)
    api(libs.squareup.retrofit2.converter.gson)
}

publishing {
    publications {
        register<MavenPublication>("release") {
            applyArtifact(artifactId = ConfigData.MODULE_KTS)
            afterEvaluate {
                from(components["java"])
            }
        }
    }
}
