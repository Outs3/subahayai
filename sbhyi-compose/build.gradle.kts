plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-parcelize")
    `maven-publish`
}

android {
    namespace = "com.outs.core.android.compose"
    compileSdk = ConfigData.COMPILE_SDK_VERSION

    defaultConfig {
        minSdk = ConfigData.MIN_SDK_VERSION
//        targetSdk = ConfigData.TARGET_SDK_VERSION

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
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.COMPOSE_VERSION
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    publishing {
//        multipleVariants(ConfigData.MODULE_COMPOSE) {
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
    // Compose UI
    api("androidx.compose.ui:ui-tooling:1.4.1")
    // Tooling support (Previews, etc.)
    api("androidx.compose.ui:ui-tooling:1.4.1")
    // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
    api("androidx.compose.foundation:foundation:1.4.1")
    // Material Design
    api("androidx.compose.material:material:1.4.1")
    api("androidx.compose.material3:material3:1.0.0-alpha12")
    // Material design icons
    api("androidx.compose.material:material-icons-core:1.4.1")
    api("androidx.compose.material:material-icons-extended:1.4.1")
    // Integration with observables
    api("androidx.compose.runtime:runtime-livedata:1.4.1")
    api("androidx.compose.runtime:runtime-rxjava2:1.4.1")

    //jetpack
    api("androidx.activity:activity-compose:1.7.0")
    api("androidx.constraintlayout:constraintlayout-compose:1.0.0")
    api("androidx.navigation:navigation-compose:2.4.1")
    api("androidx.paging:paging-compose:1.0.0-alpha14")

    //compose accompanist
    api("com.google.accompanist:accompanist-permissions:${Versions.COMPOSE_ACCOMPANIST_VERSION}")
    api("com.google.accompanist:accompanist-swiperefresh:${Versions.COMPOSE_ACCOMPANIST_VERSION}")
    api("com.google.accompanist:accompanist-flowlayout:${Versions.COMPOSE_ACCOMPANIST_VERSION}")
    api("com.google.accompanist:accompanist-webview:${Versions.COMPOSE_ACCOMPANIST_VERSION}")

    //coil
    api("io.coil-kt:coil-compose:2.2.2")

    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.test:core:1.5.0")
    testImplementation("org.mockito:mockito-core:4.6.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.4.1")
    debugImplementation("androidx.compose.ui:ui-tooling:1.4.1")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.4.1")
}

tasks.register("sourceJar", Jar::class) {
    from(android.sourceSets["main"].java.srcDirs)
    archiveClassifier.convention("sources").set("sources")
}

publishing {
    publications {
        register<MavenPublication>("release") {
            applyArtifact(
                artifactId = ConfigData.MODULE_COMPOSE,
//                artifact = tasks.getByName("sourceJar")
            )
            afterEvaluate {
                from(components["release"])
            }
        }
    }
}