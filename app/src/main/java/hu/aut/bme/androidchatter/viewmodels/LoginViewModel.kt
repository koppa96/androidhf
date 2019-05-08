package hu.aut.bme.androidchatter.viewmodels

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.view.View
import android.widget.Toast
import com.android.databinding.library.baseAdapters.BR
import com.google.firebase.auth.FirebaseAuth
import hu.aut.bme.androidchatter.R
import hu.aut.bme.androidchatter.interfaces.FormView
import hu.aut.bme.androidchatter.views.LoadingButton

class LoginViewModel(private val loginView : FormView) : BaseObservable() {
    private val auth = FirebaseAuth.getInstance()
    private val context = loginView.getContext()!!

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

    fun loginButtonClicked(view: View) {
        if (email.isBlank()) {
            emailError = context.getString(R.string.field_must_be_filled)
            return
        }

        if (password.isBlank()) {
            passwordError = context.getString(R.string.field_must_be_filled)
            return
        }

        val button: LoadingButton? = if (view is LoadingButton) view as LoadingButton else null
        button?.startLoadingAnimation()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                loginView.onSuccessfulSend()
            } else {
                button?.stopLoadingAnimation()
                Toast.makeText(context, R.string.wrong_email_or_password, Toast.LENGTH_SHORT).show()
            }
        }
    }
}