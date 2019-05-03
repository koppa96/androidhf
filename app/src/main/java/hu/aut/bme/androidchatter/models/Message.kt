package hu.aut.bme.androidchatter.models

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Message (
    var messageId: String? = null,
    var chatId: String? = null,
    var senderId: String? = null,
    var content: String? = null,
    @ServerTimestamp var timestamp: Date? = null
) {
    companion object {
        val COLLECTION_NAME = "Messages"
        val MESSAGE_ID = "messageId"
        val CHAT_ID = "chatId"
        val SENDER_ID = "senderId"
        val CONTENT = "content"
        val TIMESTAMP = "timestamp"
    }
}