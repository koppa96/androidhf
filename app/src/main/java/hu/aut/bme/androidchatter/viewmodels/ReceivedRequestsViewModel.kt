package hu.aut.bme.androidchatter.viewmodels

import android.content.Context
import android.widget.Toast
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import hu.aut.bme.androidchatter.R
import hu.aut.bme.androidchatter.adapters.ReceivedRequestAdapter
import hu.aut.bme.androidchatter.models.Chat
import hu.aut.bme.androidchatter.models.Request
import hu.aut.bme.androidchatter.models.User

class ReceivedRequestsViewModel(private val context: Context) : ReceivedRequestAdapter.ReceivedRequestActionListener {
    private var db = FirebaseFirestore.getInstance()

    override fun onRequestAccepted(request: Request) {
        val chat = Chat(
            chatId = db.collection(User.COLLECTION_NAME)
                .document(request.senderId!!)
                .collection(Chat.COLLECTION_NAME)
                .document()
                .id,
            user1Id = request.senderId,
            user1Name = request.senderName,
            user2Id = request.receiverId,
            user2Name = request.receiverName
        )

        db.collection(User.COLLECTION_NAME)
            .document(request.senderId!!)
            .collection(Chat.COLLECTION_NAME)
            .document(chat.chatId!!)
            .set(chat)

        db.collection(User.COLLECTION_NAME)
            .document(request.receiverId!!)
            .collection(Chat.COLLECTION_NAME)
            .document(chat.chatId!!)
            .set(chat)

        db.collection(Request.COLLECTION_NAME)
            .document(request.requestId!!)
            .delete().addOnSuccessListener {
                Toast.makeText(context, context.getString(R.string.request_accepted), Toast.LENGTH_SHORT).show()
            }
    }

    override fun onRequestRejected(request: Request) {
        db.collection(Request.COLLECTION_NAME).document(request.requestId!!).delete().addOnSuccessListener {
            Toast.makeText(context, context.getString(R.string.request_rejected), Toast.LENGTH_SHORT).show()
        }
    }
}