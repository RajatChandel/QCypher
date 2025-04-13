package `in`.rchandel.qcypher.domain.emailparser

import `in`.rchandel.qcypher.data.model.ParsedEmail
import javax.inject.Inject

class EmailParserImpl @Inject constructor() : EmailParser {
    override fun parseEmail(mailtoString: String): ParsedEmail {
        // Clean up the input string and remove 'mailto:' if present
        val cleanedString = mailtoString.trim().removePrefix("mailto:")

        // Split the string into email, subject, and body
        val emailParts = cleanedString.split("?", limit = 2) // Split into email and query parameters

        // Extract the email address (first part before ?)
        val emailAddress = emailParts[0]

        // Default values for subject and body
        var subject: String? = null
        var body: String? = null

        // If there are query parameters, extract subject and body
        if (emailParts.size > 1) {
            val queryParams = emailParts[1].split("&")

            // Loop through the query parameters
            for (param in queryParams) {
                val (key, value) = param.split("=", limit = 2).map { it.trim() }

                // Check for subject and body parameters
                when (key) {
                    "subject" -> subject = value
                    "body" -> body = value
                }
            }
        }

        // Return the parsed result
        return ParsedEmail(emailAddress, subject, body)
    }
}