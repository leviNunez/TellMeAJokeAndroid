package com.levi.tellmeajokeapp.util

import android.animation.*
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator

fun fadeViewIn(view: View, animDuration: Long = 500, animDelay: Long = 0) {
    view.alpha = 0f
    view.visibility = View.VISIBLE

    val fader = ObjectAnimator.ofFloat(view, View.ALPHA, 1f).apply {
        startDelay = animDelay
        duration = animDuration
        interpolator = AccelerateDecelerateInterpolator()
        disableWhileAnimating(view)
    }

    fader.start()
}

fun fadeViewOut(view: View, animDuration: Long = 500, animDelay: Long = 0) {
    val fader = ObjectAnimator.ofFloat(view, View.ALPHA, 0f).apply {
        startDelay = animDelay
        duration = animDuration
        addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationStart(animation: Animator) {
                view.isEnabled = false
            }

            override fun onAnimationEnd(animation: Animator) {
                view.isEnabled = true
                view.visibility = View.GONE
                view.alpha = 1f
            }
        })
    }

    fader.start()
}

fun scaleViewUpAndDown(
    view: View,
    animDuration: Long = 500,
    repeat: Int = 1,
    animDelay: Long = 0,
) {
    val containerH = (view.parent as ViewGroup).height
    val scaleValue = (containerH / 300).toFloat()
    val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, scaleValue)
    val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, scaleValue)
    val scaler = ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY).apply {
        startDelay = animDelay
        repeatCount = repeat
        duration = animDuration
        repeatMode = ObjectAnimator.REVERSE
        interpolator = AccelerateDecelerateInterpolator()
    }

    scaler.start()
}

fun translateAndRotateView(
    view: View,
    animDuration: Long = 500,
    animDelay: Long = 0
) {
    val containerH = (view.parent as ViewGroup).height
    val translateValue = (containerH / 10).toFloat()
    val animInterpolator = AccelerateInterpolator(1f)

    val translator = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, -translateValue).apply {
        startDelay = animDelay
        duration = animDuration
        repeatMode = ObjectAnimator.REVERSE
        repeatCount = 1
        interpolator = animInterpolator
        disableWhileAnimating(view)
    }

    val rotator = ObjectAnimator.ofFloat(view, View.ROTATION, -360f, 0f).apply {
        startDelay = 100
        duration = animDuration
        interpolator = animInterpolator
    }

    val set = AnimatorSet()
    set.playTogether(translator, rotator)
    set.start()
}

private fun ObjectAnimator.disableWhileAnimating(view: View) {
    addListener(object : AnimatorListenerAdapter() {

        override fun onAnimationStart(animation: Animator) {
            view.isEnabled = false
        }

        override fun onAnimationEnd(animation: Animator) {
            view.isEnabled = true
        }
    })
}