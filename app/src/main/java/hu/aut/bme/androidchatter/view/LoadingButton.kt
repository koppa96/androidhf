package hu.aut.bme.androidchatter.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import hu.aut.bme.androidchatter.R

class LoadingButton : Button {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    var loadingView: View? = null

    fun startLoadingAnimation() {
        val scaleDown = AnimationUtils.loadAnimation(context, R.anim.scale_down)
        startAnimation(scaleDown)
        isEnabled = false

        loadingView?.visibility = View.VISIBLE
    }

    fun stopLoadingAnimation() {
        loadingView?.visibility = View.GONE

        val scaleUp = AnimationUtils.loadAnimation(context, R.anim.scale_up)
        startAnimation(scaleUp)
        isEnabled = true
    }
}