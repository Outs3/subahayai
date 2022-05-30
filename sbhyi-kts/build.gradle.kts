plugins {
    id("kotlin")
    `java-library`
    `maven-publish`
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11

    withJavadocJar()
    withSourcesJar()
}

dependencies {
    //kotlin
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.KOTLIN_COROUTINES_VERSION}")
    api("org.jetbrains.kotlin:kotlin-reflect:${Versions.KOTLIN_VERSION}")
    //json
    api("com.google.code.gson:gson:2.9.0")
    //OkHttp
    api("com.squareup.okhttp3:okhttp:4.9.3")
    //Retrofit
    api("com.squareup.retrofit2:retrofit:2.9.0")
    api("com.squareup.retrofit2:converter-gson:2.9.0")
}

afterEvaluate {
    publishing {
        publications {
            // Creates a Maven publication called "release".
            create<MavenPublication>("release")
                .of(components["java"], artifactId = ConfigData.MODULE_KTS)
        }
    }
}
