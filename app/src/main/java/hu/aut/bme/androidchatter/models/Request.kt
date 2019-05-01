package hu.aut.bme.androidchatter.models

data class Request (
    var requestId: String? = null,
    var senderId: String? = null,
    var senderName: String? = null,
    var receiverId: String? = null,
    var receiverName: String? = null
) {
    companion object {
        val COLLECTION_NAME = "Requests"
        val REQUEST_ID = "requestId"
        val SENDER_ID = "senderId"
        val RECEIVER_ID = "receiverId"
        val SENDER_NAME = "senderName"
        val RECEIVER_NAME = "receiverName"
    }
}