package hu.aut.bme.androidchatter.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import hu.aut.bme.androidchatter.MainActivity
import hu.aut.bme.androidchatter.R
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnLogin.setOnClickListener {
            val mAuth = FirebaseAuth.getInstance()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            var error : Boolean = false;

            if (email.isBlank()) {
                etEmail.error = getString(R.string.field_must_be_filled)
                error = true
            }

            if (password.isBlank()) {
                etPassword.error = getString(R.string.field_must_be_filled)
                error = true
            }

            if (error) {
                return@setOnClickListener
            }

            val scaleDown = AnimationUtils.loadAnimation(context, R.anim.scale_down)
            btnLogin.startAnimation(scaleDown)
            btnLogin.isEnabled = false

            loginLoading.visibility = View.VISIBLE

            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(activity, MainActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    } else {
                        Toast.makeText(activity, "Login unsuccessful", Toast.LENGTH_SHORT).show()

                        loginLoading.visibility = View.GONE

                        val scaleUp = AnimationUtils.loadAnimation(context, R.anim.scale_up)
                        btnLogin.startAnimation(scaleUp)
                        btnLogin.isEnabled = true
                    }
            }
        }
    }
}