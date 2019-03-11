package hu.aut.bme.androidchatter.fragments

import android.app.Activity
import android.content.Intent

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import hu.aut.bme.androidchatter.MainActivity
import hu.aut.bme.androidchatter.R
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnRegister.setOnClickListener {
            val username = etUsername.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()

            if (password != confirmPassword) {
                etPassword.text.clear()
                etConfirmPassword.text.clear()
                etPassword.error = getString(R.string.passwords_not_matching)
                etConfirmPassword.error = getString(R.string.passwords_not_matching)

                return@setOnClickListener
            }

            register(username, email, password)
        }
    }

    private fun register(username: String, email: String, password: String) {
        val mAuth = FirebaseAuth.getInstance()

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() {
                if(it.isSuccessful) {
                    val profileUpdate = UserProfileChangeRequest.Builder()
                                            .setDisplayName(username)
                                            .build()
                    mAuth.currentUser?.updateProfile(profileUpdate)

                    val intent = Intent(activity, MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                } else {
                    Toast.makeText(activity, getString(R.string.failed_to_create_user), Toast.LENGTH_SHORT).show()
                }
            }
    }
}