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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
//        useIR = true
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
}

dependencies {
    api(project(":${ConfigData.MODULE_ACORE}")) {
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-android-extensions-runtime")
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-parcelize-runtime")
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-reflect")
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib")
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-common")
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jdk7")
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jdk8")
        exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core")
        exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-android")
    }
    api("org.jetbrains.kotlin:kotlin-android-extensions-runtime:${Versions.COMPOSE_KOTLIN_VERSION}")
    api("org.jetbrains.kotlin:kotlin-parcelize-runtime:${Versions.COMPOSE_KOTLIN_VERSION}")
    api("org.jetbrains.kotlin:kotlin-reflect:${Versions.COMPOSE_KOTLIN_VERSION}")
    api("org.jetbrains.kotlin:kotlin-stdlib:${Versions.COMPOSE_KOTLIN_VERSION}")
    api("org.jetbrains.kotlin:kotlin-stdlib-common:${Versions.COMPOSE_KOTLIN_VERSION}")
    api("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.COMPOSE_KOTLIN_VERSION}")
    api("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.COMPOSE_KOTLIN_VERSION}")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COMPOSE_KOTLIN_COROUTINES_VERSION}")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.COMPOSE_KOTLIN_COROUTINES_VERSION}")

    // Compose UI
    api("androidx.compose.ui:ui:${Versions.COMPOSE_VERSION}")
    // Tooling support (Previews, etc.)
    api("androidx.compose.ui:ui-tooling:${Versions.COMPOSE_VERSION}")
    // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
    api("androidx.compose.foundation:foundation:${Versions.COMPOSE_VERSION}")
    // Material Design
    api("androidx.compose.material:material:${Versions.COMPOSE_VERSION}")
    // Material design icons
    api("androidx.compose.material:material-icons-core:${Versions.COMPOSE_VERSION}")
    api("androidx.compose.material:material-icons-extended:${Versions.COMPOSE_VERSION}")
    // Integration with observables
    api("androidx.compose.runtime:runtime-livedata:${Versions.COMPOSE_VERSION}")
    api("androidx.compose.runtime:runtime-rxjava2:${Versions.COMPOSE_VERSION}")

    api("androidx.activity:activity-compose:1.4.0")
    api("androidx.constraintlayout:constraintlayout-compose:1.0.0-rc02")

    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.test:core:1.4.0")
    testImplementation("org.mockito:mockito-core:3.3.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${Versions.COMPOSE_VERSION}")
    debugImplementation("androidx.compose.ui:ui-tooling:${Versions.COMPOSE_VERSION}")
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
                    artifactId = ConfigData.MODULE_COMPOSE,
                    artifact = tasks.getByName("sourceJar")
                )
        }
    }
}
