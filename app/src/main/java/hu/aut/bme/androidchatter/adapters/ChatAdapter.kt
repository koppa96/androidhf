package hu.aut.bme.androidchatter.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import hu.aut.bme.androidchatter.R
import hu.aut.bme.androidchatter.models.Chat
import kotlinx.android.synthetic.main.item_chat.view.*

class ChatAdapter(options: FirestoreRecyclerOptions<Chat>, private val context: Context) : FirestoreRecyclerAdapter<Chat, ChatAdapter.ChatViewHolder>(options) {
    private val auth = FirebaseAuth.getInstance()
    var chatClickListener: ChatClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int, model: Chat) {
        holder.tvOtherName.text = if (auth.currentUser!!.displayName == model.user1Name) model.user2Name else model.user1Name
        if (model.lastMessage == null || model.lastMessageSentBy == null) {
            holder.tvLastMessage.text = context.getString(R.string.no_messages)
        } else {
            holder.tvLastMessage.text = "${model.lastMessageSentBy}: ${model.lastMessage}"
        }
        holder.chat = model
    }

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvOtherName = itemView.tvOtherName
        val tvLastMessage = itemView.tvLastMessage

        var chat: Chat? = null

        init {
            itemView.setOnClickListener {
                chatClickListener?.onChatClicked(chat!!)
            }
        }
    }

    interface ChatClickListener {
        fun onChatClicked(chat: Chat)
    }
}