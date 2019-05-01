package hu.aut.bme.androidchatter.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import hu.aut.bme.androidchatter.fragments.ReceivedRequestsListFragment
import hu.aut.bme.androidchatter.fragments.SentRequestsListFragment

class RequestsViewPagerAdapter(fm : FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> SentRequestsListFragment()
            1 -> ReceivedRequestsListFragment()
            else -> throw IllegalArgumentException("No such fragment.")
        }
    }

    override fun getCount() = 2

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Sent requests"
            1 -> "Received requests"
            else -> null
        }
    }
}