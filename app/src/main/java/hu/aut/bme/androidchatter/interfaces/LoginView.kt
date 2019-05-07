package hu.aut.bme.androidchatter.interfaces

interface LoginView {
    fun setEmailError()
    fun setPasswordError()
    fun showLoginError()
    fun onSuccessfulLogin()
}