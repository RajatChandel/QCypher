package `in`.rchandel.qcypher.domain.emailparser

import `in`.rchandel.qcypher.data.model.ParsedEmail

interface EmailParser {
    fun parseEmail(mailtoString: String): ParsedEmail
}