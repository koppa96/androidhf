package hu.aut.bme.androidchatter.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import hu.aut.bme.androidchatter.R
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {
    private lateinit var user : FirebaseUser

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        user = FirebaseAuth.getInstance().currentUser!!
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnChangeUsername.setOnClickListener {
            val dbRef = FirebaseDatabase.getInstance().reference.child("Users").child(user.uid)
            val newUsername = etNewUsername.text.toString()

            if (newUsername.isNotBlank()) {
                dbRef.setValue(newUsername).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(activity, "Username changed successfully", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                etNewUsername.error = getString(R.string.field_must_be_filled)
            }
        }
    }
}