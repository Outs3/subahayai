plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    `maven-publish`
}

android {
    namespace = "com.outs.core.android.compose"
    compileSdk = ConfigData.COMPILE_SDK_VERSION

    defaultConfig {
        minSdk = ConfigData.MIN_SDK_VERSION
        targetSdk = ConfigData.TARGET_SDK_VERSION

        vectorDrawables {
            useSupportLibrary = true
        }
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
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
    api(project(":${ConfigData.MODULE_ACORE}"))
    // Compose UI
    api("androidx.compose.ui:ui:${Versions.COMPOSE_VERSION}")
    // Tooling support (Previews, etc.)
    api("androidx.compose.ui:ui-tooling:${Versions.COMPOSE_VERSION}")
    // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
    api("androidx.compose.foundation:foundation:${Versions.COMPOSE_VERSION}")
    // Material Design
    api("androidx.compose.material:material:${Versions.COMPOSE_VERSION}")
    api("androidx.compose.material3:material3:1.0.0-alpha12")
    // Material design icons
    api("androidx.compose.material:material-icons-core:${Versions.COMPOSE_VERSION}")
    api("androidx.compose.material:material-icons-extended:${Versions.COMPOSE_VERSION}")
    // Integration with observables
    api("androidx.compose.runtime:runtime-livedata:${Versions.COMPOSE_VERSION}")
    api("androidx.compose.runtime:runtime-rxjava2:${Versions.COMPOSE_VERSION}")

    //jetpack
    api("androidx.activity:activity-compose:1.4.0")
    api("androidx.constraintlayout:constraintlayout-compose:1.0.0")
    api("androidx.navigation:navigation-compose:2.4.1")
    api("androidx.paging:paging-compose:1.0.0-alpha14")

    //compose accompanist
    api("com.google.accompanist:accompanist-permissions:${Versions.COMPOSE_ACCOMPANIST_VERSION}")
    api("com.google.accompanist:accompanist-swiperefresh:${Versions.COMPOSE_ACCOMPANIST_VERSION}")
    api("com.google.accompanist:accompanist-flowlayout:${Versions.COMPOSE_ACCOMPANIST_VERSION}")
    api("com.google.accompanist:accompanist-webview:${Versions.COMPOSE_ACCOMPANIST_VERSION}")

    //coil
    api("io.coil-kt:coil-compose:2.1.0")

    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.test:core:1.4.0")
    testImplementation("org.mockito:mockito-core:4.6.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${Versions.COMPOSE_VERSION}")
    debugImplementation("androidx.compose.ui:ui-tooling:${Versions.COMPOSE_VERSION}")
    debugImplementation("androidx.compose.ui:ui-test-manifest:${Versions.COMPOSE_VERSION}")
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