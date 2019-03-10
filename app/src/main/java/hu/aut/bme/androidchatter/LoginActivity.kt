package hu.aut.bme.androidchatter

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import hu.aut.bme.androidchatter.adapters.LoginViewPagerAdapter
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginViewPager.adapter = LoginViewPagerAdapter(supportFragmentManager)
    }
}
