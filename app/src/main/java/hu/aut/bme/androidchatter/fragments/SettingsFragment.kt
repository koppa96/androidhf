package hu.aut.bme.androidchatter.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import hu.aut.bme.androidchatter.MainActivity
import hu.aut.bme.androidchatter.R
import hu.aut.bme.androidchatter.databinding.FragmentSettingsBinding
import hu.aut.bme.androidchatter.models.User
import hu.aut.bme.androidchatter.viewmodels.SettingsViewModel
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {
    private lateinit var prefs: SharedPreferences

    companion object {
        val THEME_KEY = "theme"
        val THEME_DARK = "dark"
        val THEME_LIGHT = "light"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentSettingsBinding>(inflater, R.layout.fragment_settings, container, false)
        binding.viewModel = SettingsViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = PreferenceManager.getDefaultSharedPreferences(activity!!.applicationContext)
        when (prefs.getString(THEME_KEY, THEME_DARK)) {
            THEME_DARK -> btnDark.isChecked = true
            THEME_LIGHT -> btnLight.isChecked = true
            else -> throw UnsupportedOperationException("Unknown theme name")
        }

        btnChangePassword.loadingView = passwordProgress
        btnChangeEmail.loadingView = emailProgress
        btnChangeUsername.loadingView = usernameProgress

        btnSaveTheme.setOnClickListener(::changeTheme)
    }

    fun changeTheme(view: View) {
        val selectedTheme = when (radioGroup.checkedRadioButtonId) {
            R.id.btnDark -> THEME_DARK
            R.id.btnLight -> THEME_LIGHT
            else -> throw UnsupportedOperationException("Unknown theme.")
        }

        if (selectedTheme != prefs.getString(THEME_KEY, THEME_DARK)) {
            val editor = prefs.edit()
            editor.putString(THEME_KEY, selectedTheme)
            editor.apply()

            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }
}