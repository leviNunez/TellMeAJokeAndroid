package com.levi.tellmeajokeapp.util

import android.animation.*
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnRepeat
import androidx.core.animation.doOnResume
import androidx.core.animation.doOnStart
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
An animation that makes a view disappear gradually and grow in size, and then reappear and shrink
back to its original size.
 */
inline fun fadeAndScaleView(
    view: View,
    crossinline onStart: () -> Unit = {},
    crossinline onRepeat: () -> Unit = {},
    crossinline onEnd: () -> Unit = {}
): ObjectAnimator {
    val containerH = (view.parent as ViewGroup).height
    val scaleValue = (containerH / 300).toFloat()
    val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, scaleValue)
    val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, scaleValue)
    val alphaValue = PropertyValuesHolder.ofFloat(View.ALPHA, 0f)

    return ObjectAnimator.ofPropertyValuesHolder(view, alphaValue, scaleX, scaleY)
        .apply {
            duration = 600L
            repeatCount = 1
            repeatMode = ObjectAnimator.REVERSE

            doOnStart { onStart() }

            doOnRepeat { onRepeat() }

            doOnEnd { onEnd() }

            start()
        }
}

/**
An animation that makes a view invisible at the beginning and then gradually increases its
visibility until the view is fully visible.
 */
inline fun fadeViewIn(
    view: View,
    crossinline onStart: () -> Unit = {},
    crossinline onEnd: () -> Unit = {}
): ObjectAnimator {
    return ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f).apply {
        duration = 600L

        doOnStart { onStart() }

        doOnEnd { onEnd() }

        start()
    }
}

/**
An animation that makes a view disappear gradually.
 */
inline fun fadeViewOut(
    view: View,
    crossinline onStart: () -> Unit = {},
    crossinline onEnd: () -> Unit = {}
): ObjectAnimator {
    return ObjectAnimator.ofFloat(view, View.ALPHA, 0f).apply {
        duration = 600L

        doOnStart { onStart() }

        doOnEnd { onEnd() }

        start()
    }
}

/**
An animation that makes a view grow and shrink repeatedly.
 */
inline fun scaleViewUpAndDown(
    view: View,
    crossinline onStart: () -> Unit = {},
    crossinline onRepeat: () -> Unit = {},
    crossinline onEnd: () -> Unit = {}
): ObjectAnimator {
    val containerH = (view.parent as ViewGroup).height
    val scaleValue = (containerH / 300).toFloat()
    val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, scaleValue)
    val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, scaleValue)
    return ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY)
        .apply {
            duration = 500L
            repeatCount = 3
            repeatMode = ObjectAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator()

            doOnStart { onStart() }

            doOnRepeat { onRepeat() }

            doOnEnd { onEnd() }

            start()
        }
}

/**
An animation that makes a view move up and rotate 360 degrees simulating a backflip.
 */
inline fun translateAndRotateView(
    view: View,
    crossinline onStart: () -> Unit = {},
    crossinline onRepeat: () -> Unit = {},
    crossinline onEnd: () -> Unit = {}
): AnimatorSet {
    val containerH = (view.parent as ViewGroup).height
    val translateValue = -(containerH / 10).toFloat()
    val animDuration = 500L

    val translator = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, translateValue).apply {
        duration = animDuration
        repeatMode = ObjectAnimator.REVERSE
        repeatCount = 1
        interpolator = AccelerateInterpolator(1f)
    }

    val rotator = ObjectAnimator.ofFloat(view, View.ROTATION, -360f, 0f).apply {
        startDelay = 100
        duration = animDuration
        interpolator = AccelerateInterpolator(1f)
    }

    return AnimatorSet().apply {
        playTogether(translator, rotator)

        doOnStart { onStart() }

        doOnRepeat { onRepeat() }

        doOnEnd { onEnd() }

        start()
    }
}

suspend fun Animator.awaitEnd() = suspendCancellableCoroutine { cont ->
    cont.invokeOnCancellation { cancel() }

    addListener(object : AnimatorListenerAdapter() {
        private var endedSuccessfully = true

        override fun onAnimationCancel(animation: Animator) {
            endedSuccessfully = false
        }

        override fun onAnimationEnd(animation: Animator) {
            animation.removeListener(this)

            if (cont.isActive) {
                if (endedSuccessfully) {
                    cont.resume(Unit)
                } else {
                    cont.cancel()
                }
            }
        }
    })
}