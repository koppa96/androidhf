package hu.aut.bme.androidchatter.models

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ServerTimestamp
import java.io.Serializable
import java.util.*

data class Chat (
    var chatId: String? = null,
    var user1Id: String? = null,
    var user1Name: String? = null,
    var user2Id: String? = null,
    var user2Name: String? = null,
    var lastMessageSentBy: String? = null,
    var lastMessage: String? = null,
    @ServerTimestamp var timestamp: Date? = null
) : Serializable {
    companion object {
        val COLLECTION_NAME = "Chats"
        val CHAT_ID = "chatId"
        val USER1_ID = "user1Id"
        val USER1_NAME = "user1Name"
        val USER2_ID = "user2Id"
        val USER2_NAME = "user2Name"
        val LAST_MESSAGE_SENT_BY = "lastMessageSentBy"
        val LAST_MESSAGE = "lastMessage"
        val TIMESTAMP = "timestamp"
    }
}