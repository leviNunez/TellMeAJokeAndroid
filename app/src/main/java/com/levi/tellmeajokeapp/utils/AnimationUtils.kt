package com.levi.tellmeajokeapp.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.animation.doOnEnd
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

private const val DEFAULT_ANIMATION_DURATION = 500L
/**
An animation that scales up a view 4 times its original size.
*/
fun scaleUp(view: View): ObjectAnimator {
    val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 4f)
    val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 4f)

    return ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY)
        .apply {
            duration = DEFAULT_ANIMATION_DURATION
            doOnEnd {
                view.scaleX = 1f
                view.scaleY = 1f
            }
            start()
        }
}

/**
An animation that scales up a view 4 times its size and then scales it back down
to its original size.
*/
fun scaleDown(view: View): ObjectAnimator {
    view.scaleX = 4f
    view.scaleY = 4f
    val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f)
    val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f)

    return ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY).apply {
        duration = DEFAULT_ANIMATION_DURATION
        start()
    }
}


/**
An animation that makes a view invisible at the beginning and then gradually increases its
visibility until it is fully visible.
 */
fun fadeViewIn(
    view: View
): ObjectAnimator {
    return ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f).apply {
        duration = DEFAULT_ANIMATION_DURATION
        start()
    }
}

/**
An animation that makes a view fade out gradually.
 */
fun fadeViewOut(view: View): ObjectAnimator {
    return ObjectAnimator.ofFloat(view, View.ALPHA, 0f).apply {
        duration = DEFAULT_ANIMATION_DURATION
        doOnEnd { view.alpha = 1F }
        start()
    }
}

/**
 An animation that makes a view grow and shrink repeatedly.
 * */
fun scaleViewUpAndDown(
    view: View,
    context: Context,
): ObjectAnimator {
    val orientation = context.resources.configuration.orientation
    val containerHeight = context.resources.displayMetrics.heightPixels
    val scale = if (orientation == ORIENTATION_LANDSCAPE) {
        (containerHeight / 250).toFloat()
    } else {
        (containerHeight / 400).toFloat()
    }
    val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, scale)
    val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, scale)

    return ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY)
        .apply {
            duration = 500L
            repeatCount = 3
            repeatMode = ObjectAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
}

/**
An animation that makes a view move up and rotate 360 degrees simulating a backflip.
 */
fun moveAndRotateView(view: View): AnimatorSet {
    val containerH = (view.parent as ViewGroup).height
    val translateValue = -(containerH / 7).toFloat()

    val translator = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, translateValue).apply {
        duration = 500L
        repeatMode = ObjectAnimator.REVERSE
        repeatCount = 1
    }

    val rotator = ObjectAnimator.ofFloat(view, View.ROTATION, -360f, 0f).apply {
        duration = 800L
    }

    return AnimatorSet().apply {
        playTogether(translator, rotator)
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