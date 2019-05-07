package hu.aut.bme.androidchatter.fragments

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import hu.aut.bme.androidchatter.MainActivity
import hu.aut.bme.androidchatter.R
import hu.aut.bme.androidchatter.databinding.FragmentRegisterBinding
import hu.aut.bme.androidchatter.interfaces.RegisterView
import hu.aut.bme.androidchatter.viewmodels.RegisterViewModel
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : Fragment(), RegisterView {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentRegisterBinding>(inflater, R.layout.fragment_register, container, false)
        binding.viewModel = RegisterViewModel(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnRegister.loadingView = registerLoading
    }

    override fun setPasswordError() {
        etPassword.error = getString(R.string.field_must_be_filled)
    }

    override fun setEmailError() {
        etEmail.error = getString(R.string.field_must_be_filled)
    }

    override fun setConfirmPasswordError() {
        etEmail.error = getString(R.string.field_must_be_filled)
    }

    override fun setNotMatchingPasswordsError() {
        etPassword.error = getString(R.string.passwords_not_matching)
        etConfirmPassword.error = getString(R.string.passwords_not_matching)
    }

    override fun onSuccessfulRegister() {
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    override fun setUsernameTakenError() {
        etUsername.error = getString(R.string.username_taken)
    }

    override fun showUserCreationError() {
        Toast.makeText(activity, R.string.failed_to_create_user, Toast.LENGTH_SHORT).show()
    }

    override fun setUsernameError() {
        etUsername.error = getString(R.string.field_must_be_filled)
    }
}