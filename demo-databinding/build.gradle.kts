plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
}

android {
    compileSdk = ConfigData.COMPILE_SDK_VERSION

    defaultConfig {
        applicationId = ConfigData.applicationId + "demo_databinding"
        minSdk = ConfigData.MIN_SDK_VERSION
        targetSdk = ConfigData.TARGET_SDK_VERSION
        versionCode = 1
        versionName = "1.0"
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
    buildFeatures {
        dataBinding = true
    }
    namespace = "com.outs.demo_databinding"
}

dependencies {
    implementation(project(":${ConfigData.MODULE_DBIND}"))

    //paging
    api("androidx.paging:paging-runtime-ktx:3.0.1")

    //timber
    api("com.jakewharton.timber:timber:5.0.1")

    //guava
    api("com.google.guava:guava:30.1-android")

    //rouned image
    api("com.makeramen:roundedimageview:2.3.0")

    //tencent mmkv
    api("com.tencent:mmkv:1.2.11")

    //饺子播放器
    api("cn.jzvd:jiaozivideoplayer:7.7.0")

    //banner
    api("com.youth.banner:banner:2.1.0")

    //rich text
    api("com.zzhoujay.richtext:richtext:2.4.7")

    //qrcode
    api("com.github.jenly1314:zxing-lite:2.1.1")

    //日期选择器
    api("com.contrarywind:Android-PickerView:4.1.9")

    //camera
    val camerax_version = "1.1.0-alpha10"
    api("androidx.camera:camera-core:${camerax_version}")
    api("androidx.camera:camera-camera2:${camerax_version}")
    api("androidx.camera:camera-lifecycle:${camerax_version}")
    api("androidx.camera:camera-view:1.0.0-alpha30")
    api("androidx.camera:camera-extensions:1.0.0-alpha30")

}