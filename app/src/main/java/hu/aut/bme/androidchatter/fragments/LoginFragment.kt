package hu.aut.bme.androidchatter.fragments

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import hu.aut.bme.androidchatter.MainActivity
import hu.aut.bme.androidchatter.R
import hu.aut.bme.androidchatter.databinding.FragmentLoginBinding
import hu.aut.bme.androidchatter.interfaces.FormView
import hu.aut.bme.androidchatter.viewmodels.LoginViewModel
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment(), FormView {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentLoginBinding>(inflater, R.layout.fragment_login, container, false)
        binding.viewModel = LoginViewModel(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnLogin.loadingView = loginLoading
    }

    override fun onSuccessfulSend() {
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }
}