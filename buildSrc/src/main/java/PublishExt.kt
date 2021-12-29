import org.gradle.api.component.SoftwareComponent
import org.gradle.api.publish.maven.MavenPublication

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/12/28 17:22
 * desc:
 */
fun MavenPublication.of(
    from: SoftwareComponent,
    groupId: String = ConfigData.GROUP_NAME,
    artifactId: String,
    version: String = Versions.SBHYI_VERSION,
    artifact: Any? = null
) = apply {
    from(from)
    artifact?.let(::artifact)
    this.groupId = groupId
    this.artifactId = artifactId
    this.version = version
}