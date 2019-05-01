package hu.aut.bme.androidchatter.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import hu.aut.bme.androidchatter.R
import hu.aut.bme.androidchatter.adapters.SentRequestAdapter
import hu.aut.bme.androidchatter.models.Request

import kotlinx.android.synthetic.main.fragment_list.*

class SentRequestsListFragment : Fragment(), SentRequestAdapter.SentRequestActionListener {
    private lateinit var adapter: SentRequestAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()

        val query = db.collection("Requests")
            .whereEqualTo("senderId", auth.currentUser!!.uid)
            .orderBy("receiverName")

        val options = FirestoreRecyclerOptions.Builder<Request>()
            .setQuery(query, Request::class.java)
            .build()

        adapter = SentRequestAdapter(options)
        adapter.sentRequestActionListener = this

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    override fun onCanceled(request: Request) {
        val db = FirebaseFirestore.getInstance()
        db.collection("Requests").document(request.requestId!!).delete().addOnSuccessListener {
            Toast.makeText(context, "Request canceled", Toast.LENGTH_SHORT).show()
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