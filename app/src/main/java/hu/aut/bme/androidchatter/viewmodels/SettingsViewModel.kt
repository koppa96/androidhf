package hu.aut.bme.androidchatter.viewmodels

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.view.View
import android.view.animation.AnimationUtils
import com.android.databinding.library.baseAdapters.BR
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import hu.aut.bme.androidchatter.R
import hu.aut.bme.androidchatter.models.Chat
import hu.aut.bme.androidchatter.models.User
import kotlinx.android.synthetic.main.fragment_login.*

class SettingsViewModel : BaseObservable() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    @Bindable
    var username: String = ""
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.username)
            }
        }

    fun passwordChangeButtonClicked(view: View) {

    }

    fun emailChangeButtonClicked(view: View) {

    }

    fun usernameChangeButtonClicked(view: View) {
        val username = this.username
        this.username = ""
        db.collection(User.COLLECTION_NAME).whereEqualTo(User.NAME, username).get().addOnSuccessListener {
            if (!it.isEmpty) {
                return@addOnSuccessListener
            }

            val profileUpdate = UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build()

            auth.currentUser!!.updateProfile(profileUpdate)

            db.collection(User.COLLECTION_NAME).document(auth.currentUser!!.uid).update(User.NAME, username)
            db.collection(User.COLLECTION_NAME).get().addOnSuccessListener {
                it.documents.forEach {
                    val uid = it[User.UID].toString()
                    db.collection(User.COLLECTION_NAME)
                        .document(uid)
                        .collection(Chat.COLLECTION_NAME)
                        .whereEqualTo(Chat.USER1_ID, auth.currentUser!!.uid)
                        .get()
                        .addOnSuccessListener {
                            it.documents.forEach {
                                it.reference.update(Chat.USER1_NAME, username)
                            }
                        }

                    db.collection(User.COLLECTION_NAME)
                        .document(uid)
                        .collection(Chat.COLLECTION_NAME)
                        .whereEqualTo(Chat.USER2_ID, auth.currentUser!!.uid)
                        .get()
                        .addOnSuccessListener {
                            it.documents.forEach {
                                it.reference.update(Chat.USER2_NAME, username)
                            }
                        }
                }
            }
        }
    }
}