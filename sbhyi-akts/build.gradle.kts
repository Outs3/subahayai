plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    `maven-publish`
}

android {
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    api(project(":${ConfigData.MODULE_KTS}"))

    //kotlin
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.KOTLIN_COROUTINES_VERSION}")

    //ktx
    api("androidx.core:core-ktx:1.7.0")
    api("androidx.activity:activity-ktx:1.4.0")
    api("androidx.collection:collection-ktx:1.2.0")
    api("androidx.fragment:fragment-ktx:1.4.1")
    api("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1")
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1")
    api("androidx.lifecycle:lifecycle-livedata-ktx:2.4.1")
    api("androidx.lifecycle:lifecycle-reactivestreams-ktx:2.4.1")
    api("androidx.room:room-ktx:2.4.1")

    //view
    api("androidx.appcompat:appcompat:1.4.1")
    api("androidx.recyclerview:recyclerview:1.2.1")
    api("androidx.constraintlayout:constraintlayout:2.1.3")
    api("androidx.viewpager2:viewpager2:1.1.0-beta01")
    api("com.google.android.material:material:1.5.0")
    api("com.google.android.flexbox:flexbox:3.0.0")

    //image cache
    api("com.github.bumptech.glide:glide:4.13.0")
    kapt("com.github.bumptech.glide:compiler:4.13.0")
//    api("com.github.bumptech.glide:okhttp3-integration:4.11.0")

    //Log
    api("com.orhanobut:logger:2.2.0")

    //blankj utils
    api("com.blankj:utilcodex:1.31.0")

    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.test:core:1.4.0")
    testImplementation("org.mockito:mockito-core:3.3.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}

group = ConfigData.GROUP_NAME
version = Versions.SBHYI_VERSION

tasks.register("sourceJar", Jar::class) {
    from(android.sourceSets["main"].java.srcDirs)
    archiveClassifier.convention("sources").set("sources")
}

afterEvaluate {
    publishing {
        publications {
            // Creates a Maven publication called "release".
            create<MavenPublication>("release")
                .of(
                    from = components["release"],
                    artifactId = ConfigData.MODULE_AKTS,
                    artifact = tasks.getByName("sourceJar")
                )
        }
    }
}
