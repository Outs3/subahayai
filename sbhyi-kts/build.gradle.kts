plugins {
    id("kotlin")
    `java-library`
    `maven-publish`
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8

    withJavadocJar()
    withSourcesJar()
}

dependencies {
    //kotlin
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    api("org.jetbrains.kotlin:kotlin-reflect:${Versions.KOTLIN_VERSION}")
    //json
    api("com.google.code.gson:gson:2.8.8")
    //OkHttp
    api("com.squareup.okhttp3:okhttp:4.9.2")
    //Retrofit
    api("com.squareup.retrofit2:retrofit:2.9.0")
    api("com.squareup.retrofit2:converter-gson:2.9.0")
}

group = ConfigData.GROUP_NAME
version = Versions.SBHYI_VERSION

afterEvaluate {
    publishing {
        publications {
            // Creates a Maven publication called "release".
            create<MavenPublication>("release")
                .of(components["java"], artifactId = ConfigData.MODULE_KTS)
        }
    }
}

