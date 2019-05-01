package hu.aut.bme.androidchatter.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import hu.aut.bme.androidchatter.R
import hu.aut.bme.androidchatter.adapters.ReceivedRequestAdapter
import hu.aut.bme.androidchatter.models.Request
import kotlinx.android.synthetic.main.fragment_list.*

class ReceivedRequestsListFragment : Fragment(), ReceivedRequestAdapter.ReceivedRequestActionListener {
    private lateinit var adapter: ReceivedRequestAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()

        val query = db.collection(Request.COLLECTION_NAME)
            .whereEqualTo(Request.RECEIVER_ID, auth.currentUser!!.uid)
            .orderBy(Request.SENDER_NAME)

        val options = FirestoreRecyclerOptions.Builder<Request>()
            .setQuery(query, Request::class.java)
            .build()

        adapter = ReceivedRequestAdapter(options)
        adapter.receivedRequestActionListener = this

        recyclerView.emptyView = tvEmptyList
        recyclerView.emptyMessage = getString(R.string.no_incoming_requests)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    override fun onRequestAccepted(request: Request) {
        Toast.makeText(context, getString(R.string.request_accepted), Toast.LENGTH_SHORT).show()
    }

    override fun onRequestRejected(request: Request) {
        val db = FirebaseFirestore.getInstance()
        db.collection(Request.COLLECTION_NAME).document(request.requestId!!).delete().addOnSuccessListener {
            Toast.makeText(context, getString(R.string.request_rejected), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}