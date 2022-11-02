package me.yifeiyuan.flap.animation

import android.animation.Animator
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flap.delegate.AdapterDelegate
import me.yifeiyuan.flap.hook.AdapterHook

/**
 * Adapter Animation ，在完成 bind 后执行动画
 *
 * Created by 程序亦非猿 on 2022/8/29.
 *
 * @since 3.0.7
 */
abstract class BaseAdapterAnimation : AdapterHook {

    var duration: Long = 300L
    var isFirstOnly: Boolean = true
    var interpolator: Interpolator = LinearInterpolator()
    private var lastPosition = -1

    override fun onBindViewHolderEnd(adapter: RecyclerView.Adapter<*>, component: Component<*>, data: Any, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolderEnd(adapter, component, data, position, payloads)
        val adapterPosition = component.adapterPosition
        if (!isFirstOnly || adapterPosition > lastPosition) {
            val animator = createAnimator(component.itemView, component, data, position)
            animator?.let {
                it.duration = duration
                it.interpolator = interpolator
                it.start()
            }
            lastPosition = adapterPosition
        } else {
            clear(component.itemView)
        }
    }

    abstract fun createAnimator(view: View, component: Component<*>, data: Any, position: Int): Animator?

    private fun clear(v: View) {
        v.apply {
            alpha = 1f
            scaleY = 1f
            scaleX = 1f
            translationY = 0f
            translationX = 0f
            rotation = 0f
            rotationY = 0f
            rotationX = 0f
            pivotY = v.measuredHeight / 2f
            pivotX = v.measuredWidth / 2f
            animate().setInterpolator(null).startDelay = 0
        }
    }

    fun reset() {
        lastPosition = -1
    }

    fun attachToAdapter(flapAdapter: FlapAdapter) {
        flapAdapter.registerAdapterHook(this)
    }
}