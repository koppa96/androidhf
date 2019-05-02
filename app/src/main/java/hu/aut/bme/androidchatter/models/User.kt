package hu.aut.bme.androidchatter.models

data class User(
    var uid: String? = null,
    var name: String? = null
) {
    companion object {
        val COLLECTION_NAME = "Users"
        val UID = "uid"
        val NAME = "name"
    }
}