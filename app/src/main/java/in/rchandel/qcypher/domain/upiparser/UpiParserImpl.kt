package `in`.rchandel.qcypher.domain.upiparser

import `in`.rchandel.qcypher.data.model.UPIInfo
import javax.inject.Inject

class UpiParserImpl @Inject constructor() : UpiParser {
    override fun parseUpi(raw: String): UPIInfo {
        val upiRegex = "upi://pay\\?pa=([^&]*)&pn=([^&]*)&am=([^&]*)&cu=([^&]*)&url=([^&]*)".toRegex()
        val matchResult = upiRegex.find(raw)

        return if (matchResult != null) {
            UPIInfo(
                payeeAddress = matchResult.groupValues[1],
                payeeName = matchResult.groupValues[2],
                amount = matchResult.groupValues[3].toDouble(),
                currency = matchResult.groupValues[4],
                url = matchResult.groupValues[5]
            )
        } else {
            UPIInfo()
        }
    }
}