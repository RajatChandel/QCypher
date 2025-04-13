package `in`.rchandel.qcypher.domain.upiparser

import `in`.rchandel.qcypher.data.model.UPIInfo

interface UpiParser {
    fun parseUpi(raw: String): UPIInfo
}