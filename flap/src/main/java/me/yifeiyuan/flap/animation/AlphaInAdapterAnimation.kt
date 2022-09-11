package me.yifeiyuan.flap.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import me.yifeiyuan.flap.Component

/**
 * Created by 程序亦非猿 on 2022/8/29.
 *
 * @since 3.0.7
 */
class AlphaInAdapterAnimation(private var startAlpha: Float = 0f) : BaseAdapterAnimation() {

    override fun createAnimator(view: View, component: Component<*>, data: Any, position: Int): Animator {
        return ObjectAnimator.ofFloat(view, "alpha", startAlpha, 1f)
    }

}