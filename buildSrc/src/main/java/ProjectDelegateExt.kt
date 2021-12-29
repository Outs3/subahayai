import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.support.delegates.ProjectDelegate

fun ProjectDelegate.configurationByArtifact(
    configurationName: String = "default",
    filePath: String
) = let {
    configurations.maybeCreate(configurationName)
    artifacts.add(configurationName, file(filePath))
}

fun ProjectDelegate.taskSourcesJar(
    name: String = "sourceJar",
    classifier: String = "sources",
    from: Any
) = tasks.register(name, Jar::class) {
    from(from)
    archiveClassifier.convention(classifier).set(classifier)
}