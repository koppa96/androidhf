package hu.aut.bme.androidchatter.viewmodels

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.view.View
import com.android.databinding.library.baseAdapters.BR
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import hu.aut.bme.androidchatter.models.Chat
import hu.aut.bme.androidchatter.models.Message

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
            senderName = currentUser.displayName,
            content = content
        )

        db.collection(Message.COLLECTION_NAME).document(message.messageId!!).set(message)

        content = ""
    }
}