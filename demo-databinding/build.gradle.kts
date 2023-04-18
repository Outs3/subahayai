plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
}

android {
    namespace = "com.outs.demo_databinding"
    compileSdk = ConfigData.COMPILE_SDK_VERSION

    defaultConfig {
        applicationId = ConfigData.applicationId + "demo_databinding"
        minSdk = ConfigData.MIN_SDK_VERSION
        targetSdk = ConfigData.TARGET_SDK_VERSION
        versionCode = 1
        versionName = "1.0"
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
    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(project(":${ConfigData.MODULE_DBIND}"))

    //paging
    api("androidx.paging:paging-runtime-ktx:3.1.1")

    //timber
    api("com.jakewharton.timber:timber:5.0.1")

    //guava
    api("com.google.guava:guava:30.1-android")

    //rouned image
    api("com.makeramen:roundedimageview:2.3.0")

    //tencent mmkv
    api("com.tencent:mmkv:1.2.13")

    //饺子播放器
    api("cn.jzvd:jiaozivideoplayer:7.7.0")

    //banner
    api("com.youth.banner:banner:2.1.0")

    //rich text
    api("com.zzhoujay.richtext:richtext:3.0.5")

    //qrcode
    api("com.github.jenly1314:zxing-lite:2.3.1")

    //日期选择器
    api("com.contrarywind:Android-PickerView:4.1.9")

    //camera
    val camerax_version = "1.2.1"
    api("androidx.camera:camera-core:${camerax_version}")
    api("androidx.camera:camera-camera2:${camerax_version}")
    api("androidx.camera:camera-lifecycle:${camerax_version}")
    api("androidx.camera:camera-view:1.2.1")
    api("androidx.camera:camera-extensions:1.1.0-beta02")

}