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
import hu.aut.bme.androidchatter.viewmodels.UserListViewModel
import kotlinx.android.synthetic.main.fragment_list.*

class SearchFragment : Fragment() {
    private lateinit var adapter: FirestoreRecyclerAdapter<User, UserAdapter.UserViewHolder>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = FirebaseFirestore.getInstance()
        val query = db.collection(User.COLLECTION_NAME).orderBy(User.NAME)
        val options = FirestoreRecyclerOptions.Builder<User>()
            .setQuery(query, User::class.java)
            .build()

        val userAdapter = UserAdapter(options)
        userAdapter.userClickListener = UserListViewModel(context!!)
        adapter = userAdapter

        recyclerView.emptyView = tvEmptyList
        recyclerView.emptyMessage = getString(R.string.no_users)
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
}