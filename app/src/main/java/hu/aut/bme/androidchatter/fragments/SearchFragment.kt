package hu.aut.bme.androidchatter.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import hu.aut.bme.androidchatter.R
import hu.aut.bme.androidchatter.adapters.UserAdapter
import hu.aut.bme.androidchatter.models.Request
import hu.aut.bme.androidchatter.models.User
import kotlinx.android.synthetic.main.fragment_list.*

class SearchFragment : Fragment(), UserAdapter.UserClickListener {
    private lateinit var adapter: FirestoreRecyclerAdapter<User, UserAdapter.UserViewHolder>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = FirebaseFirestore.getInstance()
        val query = db.collection("Users").orderBy("name")
        val options = FirestoreRecyclerOptions.Builder<User>()
            .setQuery(query, User::class.java)
            .build()

        val userAdapter = UserAdapter(options)
        userAdapter.userClickListener = this
        adapter = userAdapter

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onUserClicked(user: User) {
        val db = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser!!
        if (currentUser.uid == user.uid) {
            Toast.makeText(context, getString(R.string.self_request_error), Toast.LENGTH_SHORT).show()
            return
        }

        db.collection("Requests")
            .whereEqualTo("senderId", currentUser.uid)
            .whereEqualTo("receiverId", user.uid)
            .get()
            .addOnSuccessListener {
                it?.let {
                    if (!it.isEmpty) {
                        Toast.makeText(context, "You've already sent a request to ${user.name}", Toast.LENGTH_SHORT)
                            .show()
                        return@addOnSuccessListener
                    }
                }

                db.collection("Requests")
                    .whereEqualTo("senderId", user.uid)
                    .whereEqualTo("receiverId", currentUser.uid)
                    .get()
                    .addOnSuccessListener {
                        it?.let {
                            if (!it.isEmpty) {
                                Toast.makeText(context, "${user.name} has already sent you a request", Toast.LENGTH_SHORT).show()
                                return@addOnSuccessListener
                            }
                        }

                        AlertDialog.Builder(activity!!)
                            .setTitle(getString(R.string.confirm_action))
                            .setMessage(getString(R.string.confirm_request_send_text, user.name))
                            .setCancelable(true)
                            .setNegativeButton(getString(R.string.no), null)
                            .setPositiveButton(getString(R.string.yes)) { dialogInterface: DialogInterface, i: Int ->
                                sendRequest(user)
                            }
                            .create()
                            .show()
                    }

            }
    }

    fun sendRequest(user: User) {
        val db = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser!!

        val request = Request(
            requestId = db.collection("Requests").document().id,
            senderId = currentUser.uid,
            senderName = currentUser.displayName,
            receiverId = user.uid,
            receiverName = user.name
        )

        db.collection("Requests").document(request.requestId!!).set(request).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, getString(R.string.request_sent), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, getString(R.string.failed_to_send_request), Toast.LENGTH_SHORT).show()
            }
        }
    }
}