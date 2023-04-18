import org.gradle.api.JavaVersion
import org.gradle.api.plugins.JavaPluginExtension


val compatibilityVersion = JavaVersion.VERSION_17

fun JavaPluginExtension.compatibility() {
        sourceCompatibility = compatibilityVersion
        targetCompatibility = compatibilityVersion
}