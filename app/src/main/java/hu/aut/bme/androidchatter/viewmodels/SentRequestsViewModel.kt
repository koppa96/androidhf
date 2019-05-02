package hu.aut.bme.androidchatter.viewmodels

import android.content.Context
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import hu.aut.bme.androidchatter.R
import hu.aut.bme.androidchatter.adapters.SentRequestAdapter
import hu.aut.bme.androidchatter.models.Request

class SentRequestsViewModel(private val context: Context) : SentRequestAdapter.SentRequestActionListener {
    override fun onCanceled(request: Request) {
        val db = FirebaseFirestore.getInstance()
        db.collection(Request.COLLECTION_NAME).document(request.requestId!!).delete().addOnSuccessListener {
            Toast.makeText(context, context.getString(R.string.request_canceled), Toast.LENGTH_SHORT).show()
        }
    }
}