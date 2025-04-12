package `in`.rchandel.qcypher.domain

import `in`.rchandel.qcypher.data.model.QRResult

interface TextAnalyser {
    fun classifyText(text: String) : QRResult
}