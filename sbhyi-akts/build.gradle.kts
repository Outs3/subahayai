plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
//    alias(libs.plugins.jetbrains.kotlin.parcelize)
    id("kotlin-parcelize")
    alias(libs.plugins.google.devtools.ksp)
    `maven-publish`
}

android {
    namespace = "com.outs.utils.android"
    compileSdk = ConfigData.COMPILE_SDK_VERSION

    defaultConfig {
        minSdk = ConfigData.MIN_SDK_VERSION
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
}

dependencies {
    api(project(":${ConfigData.MODULE_KTS}"))

    //kotlin
    api(libs.jetbrains.kotlinx.coroutines.android)

    //ktx
    api(libs.androidx.core.ktx)
    api(libs.androidx.collection.ktx)
    api(libs.androidx.activity.ktx)
    api(libs.androidx.fragment.ktx)
    api(libs.androidx.lifecycle.runtime.ktx)
    api(libs.androidx.lifecycle.viewmodel.ktx)
    api(libs.androidx.lifecycle.livedata.ktx)
    api(libs.androidx.lifecycle.reactivestreams.ktx)
    api(libs.androidx.room.ktx)

    //view
    api(libs.androidx.appcompat)
    api(libs.androidx.recyclerview)
    api(libs.androidx.constraintlayout)
    api(libs.androidx.viewpager2)
    api(libs.google.android.material)
    api(libs.google.android.flexbox)

    //image cache
    api(libs.github.bumptech.glide)
    ksp(libs.github.bumptech.glide.ksp)
//    api("com.github.bumptech.glide:okhttp3-integration:4.11.0")

    //Log
    api(libs.orhanobut.logger)

    //blankj utils
    api(libs.blankj.utilcodex)

    //paging3
    api(libs.androidx.paging.runtime)
    testApi(libs.androidx.paging.common)

    testImplementation(libs.junit)
    testImplementation(libs.androidx.core)

    testImplementation(libs.androidx.core)
    testImplementation(libs.mockito.core)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

tasks.register("sourceJar", Jar::class) {
    from(android.sourceSets["main"].java.srcDirs)
    archiveClassifier.convention("sources").set("sources")
}

publishing {
    publications {
        register<MavenPublication>("release") {
            applyArtifact(artifactId = ConfigData.MODULE_AKTS)
            afterEvaluate {
                from(components["release"])
            }
        }
    }
}