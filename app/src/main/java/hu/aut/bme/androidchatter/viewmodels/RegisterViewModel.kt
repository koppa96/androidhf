package hu.aut.bme.androidchatter.viewmodels

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.view.View
import android.widget.Toast
import com.android.databinding.library.baseAdapters.BR
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import hu.aut.bme.androidchatter.R
import hu.aut.bme.androidchatter.interfaces.FormView
import hu.aut.bme.androidchatter.models.User
import hu.aut.bme.androidchatter.views.LoadingButton

class RegisterViewModel(private val registerView: FormView) : BaseObservable() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val context = registerView.getContext()!!

    @Bindable
    var username : String = ""
        set(value) {
            if (field != value) {
                field = value
                usernameError = null
                notifyPropertyChanged(BR.username)
            }
        }

    @Bindable
    var usernameError : String? = null
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.usernameError)
            }
        }

    @Bindable
    var email : String = ""
        set(value) {
            if (field != value) {
                field = value
                emailError = null
                notifyPropertyChanged(BR.email)
            }
        }

    @Bindable
    var emailError : String? = null
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.emailError)
            }
        }

    @Bindable
    var password : String = ""
        set(value) {
            if (field != value) {
                field = value
                passwordError = null
                notifyPropertyChanged(BR.password)
            }
        }

    @Bindable
    var passwordError : String? = null
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.passwordError)
            }
        }

    @Bindable
    var confirmPassword : String = ""
        set(value) {
            if (field != value) {
                field = value
                confirmPasswordError = null
                notifyPropertyChanged(BR.confirmPassword)
            }
        }

    @Bindable
    var confirmPasswordError : String? = null
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.confirmPasswordError)
            }
        }

    fun registerButtonClicked(view: View) {
        if (!validateForm()) {
            return
        }

        val button: LoadingButton? = if (view is LoadingButton) view as LoadingButton else null
        button?.startLoadingAnimation()
        db.collection(User.COLLECTION_NAME).whereEqualTo(User.NAME, username).get().addOnCompleteListener {
            if (it.isSuccessful) {
                it.result?.let {
                    if (!it.isEmpty) {
                        usernameError = context.getString(R.string.username_taken)
                        button?.stopLoadingAnimation()
                        return@addOnCompleteListener
                    }
                }

                createUser(button)

            } else {
                Toast.makeText(context, R.string.failed_to_create_user, Toast.LENGTH_SHORT).show()
                button?.stopLoadingAnimation()
            }
        }
    }

    private fun createUser(button: LoadingButton? = null) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    val profileUpdate = UserProfileChangeRequest.Builder()
                        .setDisplayName(username)
                        .build()

                    auth.currentUser!!.updateProfile(profileUpdate)
                    val uid = auth.currentUser!!.uid

                    val userData = User(name = username, uid = uid)

                    db.collection(User.COLLECTION_NAME).document(uid).set(userData).addOnCompleteListener {
                        if (it.isSuccessful) {
                            registerView.onSuccessfulSend()
                        } else {
                            Toast.makeText(context, R.string.failed_to_create_user, Toast.LENGTH_SHORT).show()
                            button?.stopLoadingAnimation()
                        }
                    }
                } else {
                    emailError = context.getString(R.string.email_taken)
                    button?.stopLoadingAnimation()
                }
            }
    }

    fun validateForm() : Boolean {
        if (username.isBlank()) {
            usernameError = context.getString(R.string.field_must_be_filled)
            return false
        }

        if (email.isBlank()) {
            emailError = context.getString(R.string.field_must_be_filled)
            return false
        }

        if (password.isBlank()) {
            passwordError = context.getString(R.string.field_must_be_filled)
            return false
        }

        if (password.length < 6) {
            passwordError = context.getString(R.string.too_short_password)
            return false
        }

        if (confirmPassword.isBlank()) {
            confirmPasswordError = context.getString(R.string.field_must_be_filled)
            return false
        }

        if (password != confirmPassword) {
            password = ""
            confirmPassword = ""
            passwordError = context.getString(R.string.passwords_not_matching)
            confirmPasswordError = context.getString(R.string.passwords_not_matching)
            return false
        }

        return true
    }
}