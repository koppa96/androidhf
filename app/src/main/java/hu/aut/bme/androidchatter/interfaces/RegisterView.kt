package hu.aut.bme.androidchatter.interfaces

interface RegisterView {
    fun setUsernameError()
    fun setPasswordError()
    fun setEmailError()
    fun setConfirmPasswordError()
    fun setNotMatchingPasswordsError()
    fun startLoadingAnimation()
    fun stopLoadingAnimation()
    fun onSuccessfulRegister()
    fun setUsernameTakenError()
    fun showUserCreationError()
}