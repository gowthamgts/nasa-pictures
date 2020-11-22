package dev.gowtham.nasapictures.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View

fun View.slideIn(fromTop: Boolean) {
    this.apply {
        alpha = 0f
        visibility = View.VISIBLE

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        animate()
            .alpha(1f)
            .translationYBy(if (fromTop) 50F else -50F)
            .setDuration(resources.getInteger(android.R.integer.config_shortAnimTime).toLong())
            .setListener(null)
    }
}

fun View.slideOut(fromTop: Boolean) {
    this.apply {
        animate()
            .alpha(0f)
            .translationYBy(if (fromTop) 50F else -50F)
            .setDuration(resources.getInteger(android.R.integer.config_mediumAnimTime).toLong())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    this@slideOut.visibility = View.INVISIBLE
                }
            })
    }
}
