plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-parcelize")
    `maven-publish`
}

android {
    namespace = "com.outs.core.android"
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
    publishing {
//        multipleVariants(ConfigData.MODULE_ACORE) {
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
    api(project(":${ConfigData.MODULE_AKTS}"))

    //multidex
    api(libs.androidx.multidex)

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
                artifactId = ConfigData.MODULE_ACORE,
//                artifact = tasks.getByName("sourceJar")
            )
            afterEvaluate {
                from(components["release"])
            }
        }
    }
}
