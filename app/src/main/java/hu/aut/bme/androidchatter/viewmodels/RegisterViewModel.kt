package hu.aut.bme.androidchatter.viewmodels

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.view.View
import com.android.databinding.library.baseAdapters.BR
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import hu.aut.bme.androidchatter.interfaces.RegisterView

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

        registerView.startLoadingAnimation()
        db.collection("Users").whereEqualTo("name", username).get().addOnCompleteListener {
            if (it.isSuccessful) {
                it.result?.let {
                    if (!it.isEmpty) {
                        registerView.setUsernameTakenError()
                        registerView.stopLoadingAnimation()
                        return@addOnCompleteListener
                    }
                }

                createUser()

            } else {
                registerView.showUserCreationError()
                registerView.stopLoadingAnimation()
            }
        }
    }

    private fun createUser() {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    val uid = auth.currentUser!!.uid

                    val userData = HashMap<String, Any>()
                    userData["name"] = username

                    db.collection("Users").document(uid).set(userData).addOnCompleteListener {
                        if (it.isSuccessful) {
                            registerView.onSuccessfulRegister()
                        } else {
                            registerView.showUserCreationError()
                            registerView.stopLoadingAnimation()
                        }
                    }
                } else {
                    registerView.showUserCreationError()
                    registerView.stopLoadingAnimation()
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