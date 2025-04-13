package `in`.rchandel.qcypher.data.model

data class ContactInfo(
    val name: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val organization: String? = null,
    val title: String? = null,
    val address: String? = null,
    val phoneList: List<String>? = null // Optional if you want to show all numbers
)