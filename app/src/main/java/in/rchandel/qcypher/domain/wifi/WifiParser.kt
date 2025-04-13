package `in`.rchandel.qcypher.domain.wifi

import `in`.rchandel.qcypher.data.model.WifiInfo

interface WifiParser {
    fun parseWifi(text: String) : WifiInfo
}