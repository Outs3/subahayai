plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.google.devtools.ksp) apply false
//    `maven-publish`
}

allprojects {
    if (name.startsWith("sbhyi")) {
        group = ConfigData.GROUP_NAME
        version = Versions.SBHYI_VERSION
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "19"
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