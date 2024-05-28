plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    id("kotlin-parcelize")
    `maven-publish`
}

android {
    namespace = "com.outs.core.android.compose"
    compileSdk = ConfigData.COMPILE_SDK_VERSION

    defaultConfig {
        minSdk = ConfigData.MIN_SDK_VERSION

        vectorDrawables {
            useSupportLibrary = true
        }
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
        compose = true
    }
}

dependencies {
    // Import the Compose BOM
    implementation(platform(libs.androidx.compose.bom))

    api(project(":${ConfigData.MODULE_ACORE}"))
    // Compose UI
    api(libs.androidx.compose.ui.tooling)
    // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
    api(libs.androidx.compose.foundation)
    // Material Design
    api(libs.androidx.material)
    api(libs.material3)
    // Material design icons
    api(libs.androidx.material.icons.core)
    api(libs.androidx.material.icons.extended)
    // Integration with observables
    api(libs.androidx.runtime.livedata)
    api(libs.androidx.runtime.rxjava2)

    //jetpack
    api(libs.androidx.activity.compose)
    api(libs.androidx.constraintlayout.compose)
    api(libs.androidx.navigation.compose)
    api(libs.androidx.paging.compose)

    //compose accompanist
    api(libs.accompanist.permissions)
    api(libs.accompanist.swiperefresh)
    api(libs.accompanist.flowlayout)
    api(libs.accompanist.webview)

    //coil
    api(libs.coil.compose)

    testImplementation(libs.junit)
    testImplementation(libs.androidx.core)
    testImplementation(libs.mockito.core)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

tasks.register("sourceJar", Jar::class) {
    from(android.sourceSets["main"].java.srcDirs)
    archiveClassifier.convention("sources").set("sources")
}

publishing {
    publications {
        register<MavenPublication>("release") {
            applyArtifact(artifactId = ConfigData.MODULE_COMPOSE)
            afterEvaluate {
                from(components["release"])
            }
        }
    }
}