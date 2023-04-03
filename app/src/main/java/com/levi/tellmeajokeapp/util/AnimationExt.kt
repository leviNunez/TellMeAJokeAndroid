package com.levi.tellmeajokeapp.util

import android.animation.*
import android.view.View

fun ObjectAnimator.disableViewDuringAnimation(view: View) {
    addListener(object : AnimatorListenerAdapter() {

        override fun onAnimationStart(animation: Animator) {
            view.isEnabled = false
        }

        override fun onAnimationEnd(animation: Animator) {
            view.isEnabled = true
        }
    })
}

fun AnimatorSet.disableViewDuringAnimation(view: View) {
    addListener(object : AnimatorListenerAdapter() {

        override fun onAnimationStart(animation: Animator) {
            view.isEnabled = false
        }

        override fun onAnimationEnd(animation: Animator) {
            view.isEnabled = true
        }
    })
}