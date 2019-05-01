package hu.aut.bme.androidchatter.models

data class Request (
    var requestId: String? = null,
    var senderId: String? = null,
    var senderName: String? = null,
    var receiverId: String? = null,
    var receiverName: String? = null
)