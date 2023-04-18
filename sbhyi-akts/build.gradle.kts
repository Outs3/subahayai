plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
    `maven-publish`
}

android {
    namespace = "com.outs.utils.android"
    compileSdk = ConfigData.COMPILE_SDK_VERSION

    defaultConfig {
        minSdk = ConfigData.MIN_SDK_VERSION
//        targetSdk = ConfigData.TARGET_SDK_VERSION
    }

    compileOptions {
        sourceCompatibility = compatibilityVersion
        targetCompatibility = compatibilityVersion
    }
    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    publishing {
//        multipleVariants(ConfigData.MODULE_AKTS) {
//            includeBuildTypeValues("release")
//            withSourcesJar()
//            withJavadocJar()
//        }
//        singleVariant("release") {
//            withSourcesJar()
//            withJavadocJar()
//        }
    }
}

dependencies {
    api(project(":${ConfigData.MODULE_KTS}"))

    //kotlin
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.KOTLIN_COROUTINES_VERSION}")

    val lifecycleKtxVersion = "2.6.1"
    //ktx
    api("androidx.core:core-ktx:1.9.0")
    api("androidx.collection:collection-ktx:1.2.0")
    api("androidx.activity:activity-ktx:1.7.0")
    api("androidx.fragment:fragment-ktx:1.5.5")
    api("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleKtxVersion")
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleKtxVersion")
    api("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleKtxVersion")
    api("androidx.lifecycle:lifecycle-reactivestreams-ktx:$lifecycleKtxVersion")
    api("androidx.room:room-ktx:2.4.3")

    //view
    api("androidx.appcompat:appcompat:1.5.1")
    api("androidx.recyclerview:recyclerview:1.2.1")
    api("androidx.constraintlayout:constraintlayout:2.1.4")
    api("androidx.viewpager2:viewpager2:1.1.0-beta01")
    api("com.google.android.material:material:1.7.0")
    api("com.google.android.flexbox:flexbox:3.0.0")

    //image cache
    api("com.github.bumptech.glide:glide:4.14.2")
    ksp("com.github.bumptech.glide:ksp:4.14.2")
//    api("com.github.bumptech.glide:okhttp3-integration:4.11.0")

    //Log
    api("com.orhanobut:logger:2.2.0")

    //blankj utils
    api("com.blankj:utilcodex:1.31.0")

    //paging3
    api("androidx.paging:paging-runtime-ktx:${Versions.COMPOSE_PAGING_VERSION}")
    testApi("androidx.paging:paging-common-ktx:${Versions.COMPOSE_PAGING_VERSION}")

    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.test:core:1.5.0")
    testImplementation("org.mockito:mockito-core:4.6.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.0")
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