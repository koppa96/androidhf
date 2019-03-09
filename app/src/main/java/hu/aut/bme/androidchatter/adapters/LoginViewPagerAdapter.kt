package hu.aut.bme.androidchatter.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import hu.aut.bme.androidchatter.fragments.LoginFragment
import hu.aut.bme.androidchatter.fragments.RegisterFragment

class LoginViewPagerAdapter(val fm : FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(p0: Int): Fragment {
        return when (p0) {
            0 -> LoginFragment()
            1 -> RegisterFragment()
            else -> throw IllegalArgumentException("No such fragment.")
        }
    }

    override fun getCount() = 2
}