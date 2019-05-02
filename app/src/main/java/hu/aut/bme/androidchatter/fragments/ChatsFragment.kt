package hu.aut.bme.androidchatter.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import hu.aut.bme.androidchatter.ChatActivity
import hu.aut.bme.androidchatter.R
import hu.aut.bme.androidchatter.adapters.ChatAdapter
import hu.aut.bme.androidchatter.adapters.ReceivedRequestAdapter
import hu.aut.bme.androidchatter.models.Chat
import hu.aut.bme.androidchatter.models.Request
import hu.aut.bme.androidchatter.models.User
import hu.aut.bme.androidchatter.viewmodels.ReceivedRequestsViewModel
import kotlinx.android.synthetic.main.fragment_list.*

class ChatsFragment : Fragment(), ChatAdapter.ChatClickListener {
    private lateinit var adapter: ChatAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()

        val query = db.collection(User.COLLECTION_NAME)
            .document(auth.currentUser!!.uid)
            .collection(Chat.COLLECTION_NAME)
            .orderBy(Chat.TIMESTAMP)

        val options = FirestoreRecyclerOptions.Builder<Chat>()
            .setQuery(query, Chat::class.java)
            .build()

        adapter = ChatAdapter(options, context!!)
        adapter.chatClickListener = this

        recyclerView.emptyView = tvEmptyList
        recyclerView.emptyMessage = getString(R.string.no_chats)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onChatClicked(chat: Chat) {
        val intent = Intent(context, ChatActivity::class.java)
        intent.putExtra("chat", chat)
        startActivity(intent)
    }
}