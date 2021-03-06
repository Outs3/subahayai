plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    `maven-publish`
}

android {
    namespace = "com.outs.utils.android"
    compileSdk = ConfigData.COMPILE_SDK_VERSION

    defaultConfig {
        minSdk = ConfigData.MIN_SDK_VERSION
        targetSdk = ConfigData.TARGET_SDK_VERSION
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    publishing {
//        multipleVariants(ConfigData.MODULE_AKTS) {
//            includeBuildTypeValues("release")
//            withSourcesJar()
//            withJavadocJar()
//        }
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
    api(project(":${ConfigData.MODULE_KTS}"))

    //kotlin
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.KOTLIN_COROUTINES_VERSION}")

    val lifecycleKtxVersion = "2.5.0-rc02"
    //ktx
    api("androidx.core:core-ktx:1.8.0")
    api("androidx.collection:collection-ktx:1.2.0")
    api("androidx.activity:activity-ktx:1.5.0-rc01")
    api("androidx.fragment:fragment-ktx:1.5.0-rc01")
    api("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleKtxVersion")
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleKtxVersion")
    api("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleKtxVersion")
    api("androidx.lifecycle:lifecycle-reactivestreams-ktx:$lifecycleKtxVersion")
    api("androidx.room:room-ktx:2.4.2")

    //view
    api("androidx.appcompat:appcompat:1.4.2")
    api("androidx.recyclerview:recyclerview:1.2.1")
    api("androidx.constraintlayout:constraintlayout:2.1.4")
    api("androidx.viewpager2:viewpager2:1.1.0-beta01")
    api("com.google.android.material:material:1.6.1")
    api("com.google.android.flexbox:flexbox:3.0.0")

    //image cache
    api("com.github.bumptech.glide:glide:4.13.2")
    kapt("com.github.bumptech.glide:compiler:4.13.2")
//    api("com.github.bumptech.glide:okhttp3-integration:4.11.0")

    //Log
    api("com.orhanobut:logger:2.2.0")

    //blankj utils
    api("com.blankj:utilcodex:1.31.0")

    //paging3
    api("androidx.paging:paging-runtime:${Versions.COMPOSE_PAGING_VERSION}")
    testApi("androidx.paging:paging-common:${Versions.COMPOSE_PAGING_VERSION}")

    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.test:core:1.4.0")
    testImplementation("org.mockito:mockito-core:4.6.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}

tasks.register("sourceJar", Jar::class) {
    from(android.sourceSets["main"].java.srcDirs)
    archiveClassifier.convention("sources").set("sources")
}

publishing {
    publications {
        register<MavenPublication>("release") {
            applyArtifact(
                artifactId = ConfigData.MODULE_AKTS,
//                artifact = tasks.getByName("sourceJar")
            )
            afterEvaluate {
                from(components["release"])
            }
        }
    }
}