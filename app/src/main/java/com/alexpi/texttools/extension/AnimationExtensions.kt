package com.alexpi.texttools.extension

import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation


fun View.expand() {
    val animation = expandAction(this)
    startAnimation(animation)
}

private fun expandAction(view: View): Animation {
    view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    val actualHeight: Int = view.measuredHeight
    view.layoutParams.height = 0
    view.visibility = View.VISIBLE
    val animation: Animation = object : Animation() {
        override fun applyTransformation(
            interpolatedTime: Float,
            t: Transformation?
        ) {
            view.layoutParams.height =
                if (interpolatedTime == 1f) ViewGroup.LayoutParams.WRAP_CONTENT else (actualHeight * interpolatedTime).toInt()
            view.requestLayout()
        }
    }
    animation.duration = (actualHeight / (view.context.resources.displayMetrics.density*1.5f)).toLong()
    view.startAnimation(animation)
    return animation
}

fun View.collapse() {
    val actualHeight: Int = measuredHeight
    val animation: Animation = object : Animation() {
        override fun applyTransformation(
            interpolatedTime: Float,
            t: Transformation?
        ) {
            if (interpolatedTime == 1f) {
                visibility = View.GONE
            } else {
                layoutParams.height =
                    actualHeight - (actualHeight * interpolatedTime).toInt()
                requestLayout()
            }
        }
    }
    animation.duration =
        (actualHeight / (this.context.resources.displayMetrics.density*1.5f)).toLong()
    this.startAnimation(animation)
}