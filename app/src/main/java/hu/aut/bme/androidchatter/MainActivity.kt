package hu.aut.bme.androidchatter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.BottomNavigationView
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.google.firebase.auth.FirebaseAuth
import hu.aut.bme.androidchatter.fragments.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private var onSettinsFragment : Boolean = false

    private val bottomNavigationListener = BottomNavigationView.OnNavigationItemSelectedListener {
        navigateFragmentByMenuId(it.itemId)

        return@OnNavigationItemSelectedListener true
    }

    private fun navigateFragmentByMenuId(id : Int) {
        when (id) {
            R.id.navChats -> supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, ChatsFragment())
                .commit()
            R.id.navRequests -> supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, RequestsFragment())
                .commit()
            R.id.navSearch -> supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, SearchFragment())
                .commit()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        when (prefs.getString(SettingsFragment.THEME_KEY, SettingsFragment.THEME_DARK)) {
            SettingsFragment.THEME_DARK -> setTheme(R.style.AppThemeActionbar)
            SettingsFragment.THEME_LIGHT -> setTheme(R.style.AppThemeActionbarLight)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()

        val user = mAuth?.currentUser
        if (user == null) {
            navigateToLogin()
        }

        setSupportActionBar(toolbar)
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavigationListener)

        bottomNavigationView.selectedItemId = R.id.navChats
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.logOut -> {
                mAuth?.signOut()
                navigateToLogin()
            }

            R.id.settings -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, SettingsFragment())
                    .commit()

                val animation = AnimationUtils.loadAnimation(this, R.anim.shrink)

                animation.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationRepeat(animation: Animation?) {
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        bottomNavigationView.visibility = View.GONE
                    }

                    override fun onAnimationStart(animation: Animation?) {
                    }
                })

                bottomNavigationView.startAnimation(animation)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                supportActionBar?.setDisplayShowHomeEnabled(true)
                onSettinsFragment = true
            }

            android.R.id.home -> navigateBackFromSettings()
        }

        return true
    }

    private fun navigateBackFromSettings() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.grow)
        bottomNavigationView.startAnimation(animation)
        bottomNavigationView.visibility = View.VISIBLE

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)

        navigateFragmentByMenuId(bottomNavigationView.selectedItemId)
        onSettinsFragment = false
    }

    override fun onBackPressed() {
        if (onSettinsFragment) {
            navigateBackFromSettings()
        } else {
            super.onBackPressed()
        }
    }
}
