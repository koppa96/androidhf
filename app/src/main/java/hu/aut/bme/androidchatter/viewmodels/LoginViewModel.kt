package hu.aut.bme.androidchatter.viewmodels

import android.content.Context
import android.databinding.BaseObservable
import android.databinding.Bindable
import android.view.View
import com.android.databinding.library.baseAdapters.BR
import com.google.firebase.auth.FirebaseAuth
import hu.aut.bme.androidchatter.interfaces.LoginView
import kotlin.math.log

class LoginViewModel(private val loginView : LoginView) : BaseObservable() {
    private val auth = FirebaseAuth.getInstance()

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

    fun loginButtonClicked(view: View) {
        if (email.isBlank()) {
            loginView.setEmailError()
            return
        }

        if (password.isBlank()) {
            loginView.setPasswordError()
            return
        }

        loginView.startLoadingAnimation()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                loginView.onSuccessfulLogin()
            } else {
                loginView.stopLoadingAnimation()
                loginView.showLoginError()
            }
        }
    }
}