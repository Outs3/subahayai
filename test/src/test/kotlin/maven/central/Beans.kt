package maven.central

import com.google.gson.annotations.SerializedName

data class Page<T>(
    val start: Int,
    val numFound: Int,
    val docs: List<T>
)

data class Repository(
    val id: String,
    @SerializedName("g")
    val groupId: String,
    @SerializedName("a")
    val artifactId: String,
    val latestVersion: String,
    val timestamp: Long,
    val versionCount: Int,
) {
    override fun toString(): String = "$groupId:$artifactId:$latestVersion($versionCount)"
}

data class Response<T>(
    val response: T,
    val responseHeader: ResponseHeader,
    val spellcheck: SpellCheck,
) {
    val isSuccessful get() = 0 == responseHeader.status
    val code get() = responseHeader.status
    val message get() = spellcheck.suggestions.firstOrNull() ?: ""
}

data class ResponseHeader(
    val QTime: Long,
    val status: Int,
    val params: Map<String, Any>
)

data class SpellCheck(
    val suggestions: List<String>
)