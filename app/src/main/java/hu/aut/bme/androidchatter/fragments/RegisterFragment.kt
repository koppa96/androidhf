package hu.aut.bme.androidchatter.fragments

import android.app.Activity
import android.content.Intent

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import hu.aut.bme.androidchatter.MainActivity
import hu.aut.bme.androidchatter.R
import hu.aut.bme.androidchatter.models.User
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.item_search.*

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

            if (!formFilled()) {
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                etPassword.text.clear()
                etConfirmPassword.text.clear()
                etPassword.error = getString(R.string.passwords_not_matching)
                etConfirmPassword.error = getString(R.string.passwords_not_matching)

                return@setOnClickListener
            }

            startLoadingAnimation()

            register(username, email, password)
        }
    }

    private fun formFilled() : Boolean {
        var correct = true

        if (etUsername.text.isBlank()) {
            etUsername.error = getString(R.string.field_must_be_filled)
            correct = false
        }

        if (etEmail.text.isBlank()) {
            etEmail.error = getString(R.string.field_must_be_filled)
            correct = false
        }

        if (etPassword.text.isBlank()) {
            etPassword.error = getString(R.string.field_must_be_filled)
            correct = false
        }

        if (etConfirmPassword.text.isBlank()) {
            etConfirmPassword.error = getString(R.string.field_must_be_filled)
            correct = false
        }

        return correct
    }

    private fun startLoadingAnimation() {
        val scaleDown = AnimationUtils.loadAnimation(context, R.anim.scale_down)
        btnRegister.startAnimation(scaleDown)
        btnRegister.isEnabled = false
        registerLoading.visibility = View.VISIBLE
    }

    private fun stopLoadingAnimation() {
        registerLoading.visibility = View.GONE

        val scaleUp = AnimationUtils.loadAnimation(context, R.anim.scale_up)
        btnRegister.startAnimation(scaleUp)
        btnRegister.isEnabled = true
    }

    private fun register(username: String, email: String, password: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("Users").whereEqualTo("name", username).get().addOnCompleteListener {
            if (it.isSuccessful) {
                it.result?.let {
                    if (!it.isEmpty) {
                        etUsername.error = "Username already taken!"
                        stopLoadingAnimation()
                        return@addOnCompleteListener
                    }
                }

                val mAuth = FirebaseAuth.getInstance()
                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if(it.isSuccessful) {
                            val uid = mAuth.currentUser!!.uid

                            val userData = HashMap<String, Any>()
                            userData["name"] = username

                            db.collection("Users").document(uid).set(userData).addOnCompleteListener {
                                if (it.isSuccessful) {
                                    val intent = Intent(activity, MainActivity::class.java)
                                    startActivity(intent)
                                    activity?.finish()
                                } else {
                                    Toast.makeText(activity, "Failed to create user record in database.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Toast.makeText(activity, getString(R.string.failed_to_create_user), Toast.LENGTH_SHORT).show()
                            stopLoadingAnimation()
                        }
                    }
            } else {
                Toast.makeText(activity, "Couldn't fetch the users list.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}