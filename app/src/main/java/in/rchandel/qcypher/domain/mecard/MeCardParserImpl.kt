package `in`.rchandel.qcypher.domain.mecard

import `in`.rchandel.qcypher.data.model.ContactInfo
import javax.inject.Inject

class MeCardParserImpl @Inject constructor() : MeCardParser {
    override fun parseMeCard(raw: String): ContactInfo {
        val lines = raw.lines()

        var name: String? = null
        var fullName: String? = null
        val phones = mutableListOf<String>()
        var email: String? = null
        var organization: String? = null
        var title: String? = null
        var address: String? = null

        for (line in lines) {
            when {
                line.startsWith("N:") -> {
                    name = line.removePrefix("N:").replace(";", " ")
                }

                line.startsWith("FN:") -> {
                    fullName = line.removePrefix("FN:")
                }

                line.startsWith("TEL") -> {
                    val phone = line.substringAfter(":")
                    phones.add(phone)
                }

                line.startsWith("EMAIL:") -> {
                    email = line.removePrefix("EMAIL:")
                }

                line.startsWith("ORG:") -> {
                    organization = line.removePrefix("ORG:")
                }

                line.startsWith("TITLE:") -> {
                    title = line.removePrefix("TITLE:")
                }

                line.startsWith("ADR:") -> {
                    address = line.removePrefix("ADR:").replace(";", " ")
                }
            }
        }

        return ContactInfo(
            name = fullName ?: name,
            phone = phones.firstOrNull(),         // Take first number for backward compatibility
            email = email,
            organization = organization,
            title = title,
            address = address,
            phoneList = phones                    // Optional: add full list if your model supports
        )
    }
}