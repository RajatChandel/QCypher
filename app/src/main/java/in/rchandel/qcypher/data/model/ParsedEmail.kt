package `in`.rchandel.qcypher.data.model

data class ParsedEmail(
    val emailAddress: String,
    val subject: String?,
    val body: String?
)