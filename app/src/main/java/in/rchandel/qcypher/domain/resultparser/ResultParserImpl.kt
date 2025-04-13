package `in`.rchandel.qcypher.domain.resultparser

import `in`.rchandel.qcypher.data.model.ParsedQRResult
import `in`.rchandel.qcypher.data.model.QRResult
import `in`.rchandel.qcypher.data.model.QRType
import `in`.rchandel.qcypher.di.DefaultMeCardParser
import `in`.rchandel.qcypher.di.DefaultWifiParser
import `in`.rchandel.qcypher.domain.mecard.MeCardParser
import `in`.rchandel.qcypher.domain.wifi.WifiParser
import javax.inject.Inject

class ResultParserImpl @Inject constructor(
    @DefaultWifiParser private var wifiParser: WifiParser,
    @DefaultMeCardParser private var meCardParser: MeCardParser
) : ResultParser{

    override fun parseQrResult(qrResult: QRResult): ParsedQRResult {
        return when (qrResult.type) {
            QRType.WIFI -> ParsedQRResult(QRType.WIFI, wifiParser.parseWifi(qrResult.rawValue))
            QRType.CONTACT -> ParsedQRResult(QRType.CONTACT, meCardParser.parseMeCard(qrResult.rawValue))
            QRType.URL -> ParsedQRResult(QRType.URL, qrResult.rawValue)
            QRType.PHONE -> ParsedQRResult(QRType.PHONE, qrResult.rawValue)
            QRType.EMAIL -> ParsedQRResult(QRType.EMAIL, qrResult.rawValue)
            else -> ParsedQRResult(QRType.TEXT, qrResult.rawValue)
        }
    }
}