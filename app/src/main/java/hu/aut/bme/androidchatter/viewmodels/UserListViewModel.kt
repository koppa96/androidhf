package hu.aut.bme.androidchatter.viewmodels

import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import hu.aut.bme.androidchatter.R
import hu.aut.bme.androidchatter.adapters.UserAdapter
import hu.aut.bme.androidchatter.models.Chat
import hu.aut.bme.androidchatter.models.Request
import hu.aut.bme.androidchatter.models.User

class UserListViewModel(private val context: Context) : UserAdapter.UserClickListener {
    override fun onUserClicked(user: User) {
        val db = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser!!
        if (currentUser.uid == user.uid) {
            Toast.makeText(context, context.getString(R.string.self_request_error), Toast.LENGTH_SHORT).show()
            return
        }

        db.collection(Request.COLLECTION_NAME)
            .whereEqualTo(Request.SENDER_ID, currentUser.uid)
            .whereEqualTo(Request.RECEIVER_ID, user.uid)
            .get()
            .addOnSuccessListener {
                it?.let {
                    if (!it.isEmpty) {
                        Toast.makeText(context, context.getString(R.string.already_sent_request, user.name), Toast.LENGTH_SHORT)
                            .show()
                        return@addOnSuccessListener
                    }
                }

                db.collection(Request.COLLECTION_NAME)
                    .whereEqualTo(Request.SENDER_ID, user.uid)
                    .whereEqualTo(Request.RECEIVER_ID, currentUser.uid)
                    .get()
                    .addOnSuccessListener {
                        it?.let {
                            if (!it.isEmpty) {
                                Toast.makeText(context, context.getString(R.string.already_received_request, user.name), Toast.LENGTH_SHORT).show()
                                return@addOnSuccessListener
                            }
                        }

                        db.collection(User.COLLECTION_NAME)
                            .document(currentUser.uid)
                            .collection(Chat.COLLECTION_NAME)
                            .get()
                            .addOnSuccessListener {
                                it.documents.forEach {
                                    val chat = it.toObject(Chat::class.java)!!
                                    if (chat.user1Id == user.uid || chat.user2Id == user.uid) {
                                        Toast.makeText(context, "You are already chatting with ${user.name}", Toast.LENGTH_SHORT).show()
                                        return@addOnSuccessListener
                                    }
                                }

                                AlertDialog.Builder(context)
                                    .setTitle(context.getString(R.string.confirm_action))
                                    .setMessage(context.getString(R.string.confirm_request_send_text, user.name))
                                    .setCancelable(true)
                                    .setNegativeButton(context.getString(R.string.no), null)
                                    .setPositiveButton(context.getString(R.string.yes)) { dialogInterface: DialogInterface, i: Int ->
                                        sendRequest(user)
                                    }
                                    .create()
                                    .show()
                            }


                    }

            }
    }

    fun sendRequest(user: User) {
        val db = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser!!

        val request = Request(
            requestId = db.collection(Request.COLLECTION_NAME).document().id,
            senderId = currentUser.uid,
            senderName = currentUser.displayName,
            receiverId = user.uid,
            receiverName = user.name
        )

        db.collection(Request.COLLECTION_NAME).document(request.requestId!!).set(request).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, context.getString(R.string.request_sent), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, context.getString(R.string.failed_to_send_request), Toast.LENGTH_SHORT).show()
            }
        }
    }
}