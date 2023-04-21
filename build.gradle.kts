plugins {
    id("com.android.application") version "8.1.0-beta01" apply false
    id("com.android.library") version "8.1.0-beta01" apply false
    id("org.jetbrains.kotlin.android") version Versions.KOTLIN_VERSION apply false
    id("org.jetbrains.kotlin.jvm") version Versions.KOTLIN_VERSION apply false
    id("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false
//    id("maven-publish")
}

allprojects {
    if (name.startsWith("sbhyi")) {
        group = ConfigData.GROUP_NAME
        version = Versions.SBHYI_VERSION
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

//    val compatibilityVersion = JavaVersion.VERSION_17

//    extensions.configure<JavaPluginExtension>("java") {
//        println("[LogByOu]java - $name")
//        sourceCompatibility = compatibilityVersion
//        targetCompatibility = compatibilityVersion
//    }

//    extensions.findByType<com.android.build.gradle.internal.dsl.BaseAppModuleExtension>()?.apply {
//            println("[LogByOu]android - $name")
//            compileOptions {
//                sourceCompatibility = compatibilityVersion
//                targetCompatibility = compatibilityVersion
//            }
//
//            extensions.findByName("kotlinOptions")
//                ?.closureOf<org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions> {
//                    println("[LogByOu]kotlinOptions - ${name}")
//                    jvmTarget = "17"
//                }
//        }

}

//afterEvaluate {
//    subprojects {
//        println("[LogByOu][subprojects]${this@subprojects.name}")
//        if (name.startsWith("sbhyi")) {
//            publishing {
//                publications {
//                    components.forEach {
//                        println("[LogByOu][components] $name:${it.name}")
//                    }
//                    try {
//                        val from = components["release"]
//                        create<MavenPublication>("release")
//                            .of(
//                                from = from,
//                                artifactId = name,
//                                artifact = tasks.getByName("sourceJar")
//                            )
//                    } catch (e: Throwable) {
//                        val from = components["java"]
//                        create<MavenPublication>("release").of(from, artifactId = name)
//                    }
//                }
//            }
//        }
//    }
//}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}