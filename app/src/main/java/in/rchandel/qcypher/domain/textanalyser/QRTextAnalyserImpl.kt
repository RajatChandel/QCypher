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
            trimmed.startsWith("tel:") || Patterns.PHONE.matcher(trimmed).matches() -> QRResult(trimmed, QRType.PHONE)

            trimmed.startsWith("upi://") -> QRResult(trimmed, QRType.UPI)

            // Email
            Patterns.EMAIL_ADDRESS.matcher(trimmed).matches() -> QRResult(trimmed, QRType.EMAIL)

            trimmed.trim().startsWith("mailto:") -> {
                val email = trimmed.trim().removePrefix("mailto:").split("?")[0] // Remove parameters if any
                if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    QRResult(trimmed, QRType.EMAIL)
                } else {
                    QRResult(trimmed, QRType.TEXT) // Fallback if email is invalid
                }
            }

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