package me.yifeiyuan.flap.animation

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import me.yifeiyuan.flap.Component

/**
 * 放大进入
 *
 * Created by 程序亦非猿 on 2022/8/29.
 *
 * @since 3.0.7
 */
class ScaleInAdapterAnimation(var startScaleX: Float = 0.5f, var startScaleY: Float = 0.5f) : BaseAdapterAnimation() {

    override fun createAnimator(view: View, component: Component<*>, data: Any, position: Int): Animator {
        val set = AnimatorSet()
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", startScaleX, 1f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", startScaleY, 1f)
        set.playTogether(scaleX, scaleY)
        return set
    }

}