dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        jcenter() // Warning: this repository is going to shut down soon
        maven(url = "https://maven.aliyun.com/repository/public")
        maven(url = "https://maven.aliyun.com/repository/google")
        maven(url = "https://maven.aliyun.com/repository/gradle-plugin")
        maven(url = "https://jitpack.io")
        mavenLocal()
    }
}
rootProject.name = "subahayai"
include(":sbhyi-kts")
include(":sbhyi-akts")
include(":sbhyi-acore")
include(":sbhyi-databinding")
include(":sbhyi-compose")
include(":demo-compose")
include(":demo-databinding")