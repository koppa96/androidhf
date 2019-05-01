package hu.aut.bme.androidchatter.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import hu.aut.bme.androidchatter.R
import hu.aut.bme.androidchatter.models.Request
import kotlinx.android.synthetic.main.item_received_request.view.*

class ReceivedRequestAdapter(private val options: FirestoreRecyclerOptions<Request>) :
    FirestoreRecyclerAdapter<Request, ReceivedRequestAdapter.ReceivedRequestViewHolder>(options) {
    var receivedRequestActionListener: ReceivedRequestActionListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceivedRequestViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_received_request, parent, false)
        return ReceivedRequestViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ReceivedRequestViewHolder, position: Int, model: Request) {
        holder.tvSenderName.text = model.senderName
        holder.request = model
    }

    inner class ReceivedRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSenderName: TextView = itemView.tvSenderName
        val ibDelete: ImageButton = itemView.ibReject
        val ibAccept: ImageButton = itemView.ibAccept

        var request: Request? = null

        init {
            ibAccept.setOnClickListener {
                receivedRequestActionListener?.onRequestAccepted(request!!)
            }

            ibDelete.setOnClickListener {
                receivedRequestActionListener?.onRequestRejected(request!!)
            }
        }
    }

    interface ReceivedRequestActionListener {
        fun onRequestAccepted(request: Request)
        fun onRequestRejected(request: Request)
    }


}