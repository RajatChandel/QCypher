package `in`.rchandel.qcypher.domain.wifi

import `in`.rchandel.qcypher.data.model.WifiInfo
import javax.inject.Inject

class WifiParserImpl @Inject constructor() : WifiParser {
    override fun parseWifi(text: String): WifiInfo {
        val ssid = Regex("S:([^;]*)").find(text)?.groupValues?.get(1) ?: ""
        val password = Regex("P:([^;]*)").find(text)?.groupValues?.get(1) ?: ""
        val type = Regex("T:([^;]*)").find(text)?.groupValues?.get(1) ?: ""

        return WifiInfo(ssid = ssid, password = password, encryptionType = type)
    }
}