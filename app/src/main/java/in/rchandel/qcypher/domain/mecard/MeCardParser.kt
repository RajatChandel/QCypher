package `in`.rchandel.qcypher.domain.mecard

import `in`.rchandel.qcypher.data.model.ContactInfo

interface MeCardParser {
    fun parseMeCard(raw: String) : ContactInfo
}