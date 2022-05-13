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
    buildFeatures {
        dataBinding = true
    }
    namespace = "com.outs.core.android.databinding"
}

dependencies {
    api(project(":${ConfigData.MODULE_ACORE}"))

    //refresh-layout-view
    api("io.github.scwang90:refresh-layout-kernel:2.0.5")      //核心必须依赖
    api("io.github.scwang90:refresh-header-classics:2.0.5")    //经典刷新头
    api("io.github.scwang90:refresh-header-material:2.0.5")    //谷歌刷新头
    api("io.github.scwang90:refresh-footer-classics:2.0.5")    //经典加载

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
                    artifactId = ConfigData.MODULE_DBIND,
                    artifact = tasks.getByName("sourceJar")
                )
        }
    }
}
