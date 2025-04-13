package `in`.rchandel.qcypher.data.model

data class ParsedQRResult(
    val type: QRType,
    val data: Any // Can be String, WifiInfo, ContactInfo, etc.
)