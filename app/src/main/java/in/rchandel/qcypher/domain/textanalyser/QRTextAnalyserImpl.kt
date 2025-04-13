package `in`.rchandel.qcypher.domain.textanalyser

import android.util.Patterns
import `in`.rchandel.qcypher.data.model.QRResult
import `in`.rchandel.qcypher.data.model.QRType
import javax.inject.Inject

class QRTextAnalyserImpl @Inject constructor() : TextAnalyser {
    override fun classifyText(text: String): QRResult {
        val trimmed = text.trim()

        return when {
            // URL
            Patterns.WEB_URL.matcher(trimmed).matches() -> QRResult(trimmed, QRType.URL)

            // Phone number
            Patterns.PHONE.matcher(trimmed).matches() -> QRResult(trimmed, QRType.PHONE)

            // Email
            Patterns.EMAIL_ADDRESS.matcher(trimmed).matches() -> QRResult(trimmed, QRType.EMAIL)

            // WiFi config
            trimmed.startsWith("WIFI:") -> QRResult(trimmed, QRType.WIFI)

            // vCard or MECARD contact
            trimmed.startsWith("BEGIN:VCARD") || trimmed.startsWith("MECARD:") ->
                QRResult(trimmed, QRType.CONTACT)

            // Fallback to plain text
            else -> QRResult(trimmed, QRType.TEXT)
        }
    }
}