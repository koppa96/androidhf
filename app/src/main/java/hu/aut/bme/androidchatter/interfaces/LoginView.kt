package hu.aut.bme.androidchatter.interfaces

interface LoginView {
    fun setEmailError()
    fun setPasswordError()
    fun startLoadingAnimation()
    fun stopLoadingAnimation()
    fun showLoginError()
    fun onSuccessfulLogin()
}