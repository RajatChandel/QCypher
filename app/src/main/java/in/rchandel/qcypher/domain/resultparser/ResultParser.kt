package `in`.rchandel.qcypher.domain.resultparser

import `in`.rchandel.qcypher.data.model.ParsedQRResult
import `in`.rchandel.qcypher.data.model.QRResult

interface ResultParser {
    fun parseQrResult(qrResult: QRResult) : ParsedQRResult
}