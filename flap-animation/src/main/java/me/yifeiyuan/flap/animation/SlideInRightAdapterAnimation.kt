package me.yifeiyuan.flap.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import me.yifeiyuan.flap.Component

/**
 * 从 right 滑入
 * Created by 程序亦非猿 on 2022/8/29.
 * @since 3.0.7
 */
class SlideInRightAdapterAnimation : BaseAdapterAnimation() {
    override fun createAnimator(view: View, component: Component<*>, data: Any, position: Int): Animator {
        return ObjectAnimator.ofFloat(view, "translationX", view.rootView.width.toFloat(), 0f)
    }
}