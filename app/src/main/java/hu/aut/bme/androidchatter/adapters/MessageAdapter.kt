package hu.aut.bme.androidchatter.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import hu.aut.bme.androidchatter.R
import hu.aut.bme.androidchatter.models.Message
import java.lang.IllegalArgumentException

class MessageAdapter(options: FirestoreRecyclerOptions<Message>) : FirestoreRecyclerAdapter<Message, MessageAdapter.MessageViewHolder>(options) {
    private val currentUser = FirebaseAuth.getInstance().currentUser!!

    override fun getItemViewType(position: Int): Int {
        if (getItem(position).senderId == currentUser.uid) {
            return 0
        }

        return 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return when (viewType) {
            0 -> {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_sent_message, parent, false)
                MessageViewHolder(itemView)
            }

            1 -> {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_received_message, parent, false)
                MessageViewHolder(itemView)
            }

            else -> throw IllegalArgumentException("Unknown view type.")
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int, model: Message) {
        holder.tvMessageContent.text = model.content
        holder.message = model
    }

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMessageContent = itemView.findViewById<TextView>(R.id.tvMessageContent)

        lateinit var message: Message
    }
}