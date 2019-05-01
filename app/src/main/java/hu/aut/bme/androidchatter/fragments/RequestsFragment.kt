package hu.aut.bme.androidchatter.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hu.aut.bme.androidchatter.R
import hu.aut.bme.androidchatter.adapters.RequestsViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_requests.*

class RequestsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_requests, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager.adapter = RequestsViewPagerAdapter(childFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
    }
}