package hu.aut.bme.androidchatter.viewmodels

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.view.View
import com.android.databinding.library.baseAdapters.BR
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import hu.aut.bme.androidchatter.models.Chat
import hu.aut.bme.androidchatter.models.Message
import hu.aut.bme.androidchatter.models.User

class ChatViewModel(private val chat: Chat) : BaseObservable() {
    private val db = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser!!

    @Bindable
    var content: String = ""
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.content)
            }
        }

    fun sendButtonClicked(view: View) {
        if (content.isBlank()) {
            return
        }

        val message = Message(
            messageId = db.collection(Message.COLLECTION_NAME).document().id,
            chatId = chat.chatId,
            senderId = currentUser.uid,
            content = content
        )

        db.collection(Message.COLLECTION_NAME).document(message.messageId!!).set(message).addOnSuccessListener {
            val updateData = HashMap<String, Any?>()
            updateData[Chat.LAST_MESSAGE_SENT_BY] = currentUser.uid
            updateData[Chat.LAST_MESSAGE] = message.content
            updateData[Chat.TIMESTAMP] = FieldValue.serverTimestamp()
            db.collection(User.COLLECTION_NAME).document(chat.user1Id!!).collection(Chat.COLLECTION_NAME).document(chat.chatId!!).update(updateData)
            db.collection(User.COLLECTION_NAME).document(chat.user2Id!!).collection(Chat.COLLECTION_NAME).document(chat.chatId!!).update(updateData)
        }

        content = ""
    }
}