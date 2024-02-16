plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    `maven-publish`
}

android {
    namespace = "com.outs.core.android.databinding"
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
    buildFeatures {
        dataBinding = true
    }
    publishing {
//        multipleVariants(ConfigData.MODULE_DBIND) {
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
    api(project(":${ConfigData.MODULE_ACORE}"))

    //refresh-layout-view
    api(libs.refresh.layout.kernel)      //核心必须依赖
    api(libs.refresh.header.classics)    //经典刷新头
    api(libs.refresh.header.material)    //谷歌刷新头
    api(libs.refresh.footer.classics)    //经典加载

    testImplementation(libs.junit)
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
            applyArtifact(
                artifactId = ConfigData.MODULE_DBIND,
//                artifact = tasks.getByName("sourceJar")
            )
            afterEvaluate {
                from(components["release"])
            }
        }
    }
}
