package hu.aut.bme.androidchatter.interfaces

import android.content.Context

interface FormView {
    fun onSuccessfulSend()
    fun getContext(): Context?
}