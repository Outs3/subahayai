plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.google.devtools.ksp) apply false
//    `maven-publish`
}

allprojects {
    if (name.startsWith("sbhyi")) {
        group = ConfigData.GROUP_NAME
        version = Versions.SBHYI_VERSION
    }
//    tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask::class.java).configureEach {
//        compilerOptions {
//            apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_0)
//            languageVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_0)
//        }
//    }
//    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
//        kotlinOptions.jvmTarget = "21"
//    }

}
