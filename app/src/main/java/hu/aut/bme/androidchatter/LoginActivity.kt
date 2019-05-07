package hu.aut.bme.androidchatter

import android.content.Context
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.WindowManager
import hu.aut.bme.androidchatter.adapters.LoginViewPagerAdapter
import hu.aut.bme.androidchatter.fragments.SettingsFragment
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        when (prefs.getString(SettingsFragment.THEME_KEY, SettingsFragment.THEME_DARK)) {
            SettingsFragment.THEME_DARK -> setTheme(R.style.AppTheme)
            SettingsFragment.THEME_LIGHT -> setTheme(R.style.AppThemeLight)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginViewPager.adapter = LoginViewPagerAdapter(supportFragmentManager)
    }
}
