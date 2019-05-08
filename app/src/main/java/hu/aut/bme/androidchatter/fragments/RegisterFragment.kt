package hu.aut.bme.androidchatter.fragments

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hu.aut.bme.androidchatter.MainActivity
import hu.aut.bme.androidchatter.R
import hu.aut.bme.androidchatter.databinding.FragmentRegisterBinding
import hu.aut.bme.androidchatter.interfaces.FormView
import hu.aut.bme.androidchatter.viewmodels.RegisterViewModel
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : Fragment(), FormView {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentRegisterBinding>(inflater, R.layout.fragment_register, container, false)
        binding.viewModel = RegisterViewModel(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnRegister.loadingView = registerLoading
    }

    override fun onSuccessfulSend() {
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

}