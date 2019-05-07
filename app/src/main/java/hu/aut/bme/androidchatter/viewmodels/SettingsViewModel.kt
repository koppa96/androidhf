package hu.aut.bme.androidchatter.viewmodels

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.support.design.widget.Snackbar
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.android.databinding.library.baseAdapters.BR
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import hu.aut.bme.androidchatter.R
import hu.aut.bme.androidchatter.models.Chat
import hu.aut.bme.androidchatter.models.User
import hu.aut.bme.androidchatter.views.LoadingButton
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

    @Bindable
    var oldPassword: String = ""
        set (value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.oldPassword)
            }
        }

    @Bindable
    var newPassword: String = ""
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.newPassword)
            }
        }

    @Bindable
    var confirmPassword: String = ""
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.confirmPassword)
            }
        }

    @Bindable
    var email: String = ""
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.email)
            }
        }

    @Bindable
    var emailConfirmationPassword: String = ""
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.emailConfirmationPassword)
            }
        }

    fun passwordChangeButtonClicked(view: View) {
        val button: LoadingButton? = if (view is LoadingButton) view else null

        if (newPassword != confirmPassword) {
            newPassword = ""
            confirmPassword = ""
            return
        }

        val user = auth.currentUser!!
        val credential = EmailAuthProvider.getCredential(user.email!!, oldPassword)

        button?.startLoadingAnimation()
        user.reauthenticate(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                user.updatePassword(newPassword).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(view.context, "Successfully changed password.", Toast.LENGTH_SHORT).show()
                        oldPassword = ""
                        newPassword = ""
                        confirmPassword = ""
                    } else {
                        Toast.makeText(view.context, "Failed to change password.", Toast.LENGTH_SHORT).show()
                    }

                    button?.stopLoadingAnimation()
                }
            } else {
                Toast.makeText(view.context, "Failed to authenticate.", Toast.LENGTH_SHORT).show()
                button?.stopLoadingAnimation()
            }
        }
    }

    fun emailChangeButtonClicked(view: View) {
        val button: LoadingButton? = if (view is LoadingButton) view else null

        val user = auth.currentUser!!
        val credential = EmailAuthProvider.getCredential(user.email!!, emailConfirmationPassword)

        button?.startLoadingAnimation()
        user.reauthenticate(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                user.updateEmail(email).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(view.context, "Successfully changed email.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(view.context, "Failed to change email.", Toast.LENGTH_SHORT).show()
                    }

                    email = ""
                    emailConfirmationPassword = ""
                    button?.stopLoadingAnimation()
                }
            } else {
                Toast.makeText(view.context, "Incorrect password.", Toast.LENGTH_SHORT).show()
                button?.stopLoadingAnimation()

                email = ""
                emailConfirmationPassword = ""
            }
        }
    }

    fun usernameChangeButtonClicked(view: View) {
        val button: LoadingButton? = if (view is LoadingButton) view else null

        val username = this.username
        this.username = ""

        button?.startLoadingAnimation()
        db.collection(User.COLLECTION_NAME).whereEqualTo(User.NAME, username).get().addOnSuccessListener {
            if (!it.isEmpty) {
                Toast.makeText(view.context, "Username already taken.", Toast.LENGTH_SHORT).show()
                button?.stopLoadingAnimation()
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

                button?.stopLoadingAnimation()
            }
        }
    }
}