package uk.co.verifymyage.sdk

class VmaCustomer (id: String, firstName: String, lastName: String, email: String, phone: String) {
    val id: String
    val firstName: String
    val lastName: String
    val email: String
    val phone: String

    init {
        this.id = id
        this.firstName = firstName
        this.lastName = lastName
        this.email = email
        this.phone = phone
    }

    fun toJsonString (): String {
        return "{ \"customer\": { \"id\": \"123\", \"first_name\": \"First Name\", \"last_name\": \"Last Name\", \"email\": \"user@email.com\", \"phone\": \"+44070000000\", \"postcode\": \"a000aa\", \"address1\": \"Name Street\", \"address2\": \"\", \"city\": \"London\", \"country\": \"GB\"  } }"
    }
}