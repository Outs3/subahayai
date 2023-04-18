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
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.KOTLIN_COROUTINES_VERSION}")
    api("org.jetbrains.kotlin:kotlin-reflect:${Versions.KOTLIN_VERSION}")
    //json
    api("com.google.code.gson:gson:2.10")
    //OkHttp
    api("com.squareup.okhttp3:okhttp:4.10.0")
    //Retrofit
    api("com.squareup.retrofit2:retrofit:2.9.0")
    api("com.squareup.retrofit2:converter-gson:2.9.0")
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
