package hu.aut.bme.androidchatter.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import hu.aut.bme.androidchatter.R
import hu.aut.bme.androidchatter.models.User
import kotlinx.android.synthetic.main.item_user.view.*

class UserAdapter(options : FirestoreRecyclerOptions<User>) : FirestoreRecyclerAdapter<User, UserAdapter.UserViewHolder>(options) {
    var userClickListener : UserClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): UserViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int, model: User) {
        holder.tvUsername.text = model.name
        holder.user = model
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUsername : TextView = itemView.tvUsername

        var user : User? = null

        init {
            itemView.setOnClickListener {
                userClickListener?.onUserClicked(user!!)
            }
        }
    }

    interface UserClickListener {
        fun onUserClicked(user : User)
    }
}