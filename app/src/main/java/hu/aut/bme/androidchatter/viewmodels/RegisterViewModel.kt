package hu.aut.bme.androidchatter.viewmodels

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.view.View
import com.android.databinding.library.baseAdapters.BR
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import hu.aut.bme.androidchatter.interfaces.RegisterView
import hu.aut.bme.androidchatter.models.User
import hu.aut.bme.androidchatter.views.LoadingButton

class RegisterViewModel(private val registerView: RegisterView) : BaseObservable() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    @Bindable
    var username : String = ""
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.username)
            }
        }

    @Bindable
    var email : String = ""
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.email)
            }
        }

    @Bindable
    var password : String = ""
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.password)
            }
        }

    @Bindable
    var confirmPassword : String = ""
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.confirmPassword)
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
                        registerView.setUsernameTakenError()
                        button?.stopLoadingAnimation()
                        return@addOnCompleteListener
                    }
                }

                createUser(button)

            } else {
                registerView.showUserCreationError()
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
                            registerView.onSuccessfulRegister()
                        } else {
                            registerView.showUserCreationError()
                            button?.stopLoadingAnimation()
                        }
                    }
                } else {
                    registerView.showUserCreationError()
                    button?.stopLoadingAnimation()
                }
            }
    }

    fun validateForm() : Boolean {
        if (username.isBlank()) {
            registerView.setUsernameError()
            return false
        }

        if (email.isBlank()) {
            registerView.setEmailError()
            return false
        }

        if (password.isBlank()) {
            registerView.setPasswordError()
            return false
        }

        if (confirmPassword.isBlank()) {
            registerView.setConfirmPasswordError()
            return false
        }

        if (password != confirmPassword) {
            registerView.setNotMatchingPasswordsError()
            return false
        }

        return true
    }
}