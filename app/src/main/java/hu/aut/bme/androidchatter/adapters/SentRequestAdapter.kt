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
import kotlinx.android.synthetic.main.item_sent_request.view.*

class SentRequestAdapter(options: FirestoreRecyclerOptions<Request>) : FirestoreRecyclerAdapter<Request, SentRequestAdapter.SentRequestViewHolder>(options) {
    var sentRequestActionListener: SentRequestAdapter.SentRequestActionListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SentRequestViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_sent_request, parent, false)
        return SentRequestViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SentRequestViewHolder, position: Int, model: Request) {
        holder.tvReceiverName.text = model.receiverName
        holder.request = model
    }

    inner class SentRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvReceiverName: TextView = itemView.tvReceiverName
        val ibCancel: ImageButton = itemView.ibCancel

        var request: Request? = null

        init {
            ibCancel.setOnClickListener {
                sentRequestActionListener?.onCanceled(request!!)
            }
        }
    }

    interface SentRequestActionListener {
        fun onCanceled(request: Request)
    }
}