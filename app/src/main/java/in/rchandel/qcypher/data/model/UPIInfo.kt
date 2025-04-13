package `in`.rchandel.qcypher.data.model

data class UPIInfo(
    val payeeAddress: String? = null,
    val payeeName: String? = null,
    val amount: Double? = null,
    val currency: String? = null,
    val url: String? = null
)