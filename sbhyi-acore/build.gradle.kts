plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    `maven-publish`
}

android {
    namespace = "com.outs.core.android"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    publishing {
        multipleVariants(ConfigData.MODULE_ACORE) {
            includeBuildTypeValues("release")
            withSourcesJar()
            withJavadocJar()
        }
//        singleVariant("release") {
//            withSourcesJar()
//            withJavadocJar()
//        }
    }
}

dependencies {
    api(project(":${ConfigData.MODULE_AKTS}"))

    //multidex
    api("androidx.multidex:multidex:2.0.1")

    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.test:core:1.4.0")
    testImplementation("org.mockito:mockito-core:3.3.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}

//tasks.register("sourceJar", Jar::class) {
//    from(android.sourceSets["main"].java.srcDirs)
//    archiveClassifier.convention("sources").set("sources")
//}

//afterEvaluate {
//    publishing {
//        publications {
//            // Creates a Maven publication called "release".
//            create<MavenPublication>("release")
//                .of(
//                    from = components["release"],
//                    artifactId = ConfigData.MODULE_ACORE,
//                    artifact = tasks.getByName("sourceJar")
//                )
//        }
//    }
//}
