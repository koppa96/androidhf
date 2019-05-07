package hu.aut.bme.androidchatter.interfaces

interface RegisterView {
    fun setUsernameError()
    fun setPasswordError()
    fun setEmailError()
    fun setConfirmPasswordError()
    fun setNotMatchingPasswordsError()
    fun onSuccessfulRegister()
    fun setUsernameTakenError()
    fun showUserCreationError()
}