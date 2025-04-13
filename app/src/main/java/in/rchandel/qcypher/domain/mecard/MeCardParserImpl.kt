package `in`.rchandel.qcypher.domain.mecard

import `in`.rchandel.qcypher.data.model.ContactInfo

class MeCardParserImpl : MeCardParser {
    override fun parseMeCard(raw: String): ContactInfo {
        val name = Regex("N:([^;]*)").find(raw)?.groupValues?.get(1)
        val phone = Regex("TEL:([^;]*)").find(raw)?.groupValues?.get(1)
        val email = Regex("EMAIL:([^;]*)").find(raw)?.groupValues?.get(1)

        return ContactInfo(name = name, phone = phone, email = email)
    }
}