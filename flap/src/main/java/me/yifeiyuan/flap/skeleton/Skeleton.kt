package me.yifeiyuan.flap.skeleton

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flap.ext.EmptyViewHelper
import me.yifeiyuan.flap.ext.OnAdapterDataChangedObserver
import java.lang.IllegalArgumentException

/**
 *
 * 实现 Skeleton（骨架屏） 效果
 *
 * @see layout 设置单个骨架屏布局资源
 * @see layouts 多个布局资源
 * @see shimmer 开启闪闪发光效果
 * @see autoHide 自动隐藏
 * @see onlyOnce 设置只展示一次
 *
 * Created by 程序亦非猿 on 2022/8/11.
 *
 * @since 3.0.1
 */
class Skeleton : OnAdapterDataChangedObserver() {

    private lateinit var targetRecyclerView: RecyclerView
    private lateinit var targetAdapter: RecyclerView.Adapter<*>

    private var skeletonAdapter: SkeletonAdapter = SkeletonAdapter()

    /**
     * 是否正在展示
     */
    var isShowing = false

    /**
     * 是否自动隐藏，如果是，则会在数据变化时自动隐藏
     */
    var autoHide = false

    /**
     * 是否只展示一次骨架屏
     */
    var onlyOnce = false

    /**
     * 是否已经展示过一次
     */
    var hasShown = false

    /**
     * EmptyViewHelper 基于 Adapter 而 Skeleton 会动态切换 Adapter，导致 EmptyViewHelper 判断失败；
     * 所以如果使用了 EmptyViewHelper 就一定要设置；
     */
    var emptyViewHelper: EmptyViewHelper? = null

    fun bind(recyclerView: RecyclerView) = apply {
        targetRecyclerView = recyclerView
        targetRecyclerView.adapter?.let {
            adapter(it)
        }
    }

    fun adapter(adapter: RecyclerView.Adapter<*>) = apply {
        targetAdapter = adapter
    }

    fun layout(@LayoutRes layoutRes: Int) = apply {
        skeletonAdapter.skeletonLayoutRes = layoutRes

    }

    fun layouts(layouts: ((position: Int) -> Int)) = apply {
        skeletonAdapter.multiSkeletonLayoutRes = layouts

    }

    fun count(count: Int) = apply {
        skeletonAdapter.skeletonItemCount = count

    }

    fun shimmer(enable: Boolean) = apply {
        skeletonAdapter.shimmer = enable

    }

    fun autoHide(autoHide: Boolean) = apply {
        this.autoHide = autoHide
        if (autoHide) {
            targetAdapter.registerAdapterDataObserver(this)
        }

    }

    fun withEmptyViewHelper(emptyViewHelper: EmptyViewHelper) = apply {
        this.emptyViewHelper = emptyViewHelper

    }

    /**只展示一次*/
    fun onlyOnce(onlyOnce: Boolean) = apply {
        this.onlyOnce = onlyOnce
    }

    fun show() = apply {
        if (skeletonAdapter.skeletonItemCount < 0) {
            throw IllegalArgumentException("skeletonCount 不能小于 0！")
        }
        if (skeletonAdapter.skeletonLayoutRes <= 0 && skeletonAdapter.multiSkeletonLayoutRes == null) {
            throw IllegalArgumentException("未设置 skeletonLayoutRes 和 multiSkeletonLayoutRes ！")
        }

        if (onlyOnce && hasShown) {
            return this
        }

        if (targetRecyclerView.visibility != View.VISIBLE) {
            targetRecyclerView.visibility = View.VISIBLE
        }

        emptyViewHelper?.detachAdapter(targetAdapter)

        targetRecyclerView.adapter = skeletonAdapter

        emptyViewHelper?.attachAdapter(skeletonAdapter, true)

        isShowing = true
        hasShown = true
        return this
    }

    fun hide() = apply {
        if (targetRecyclerView.visibility != View.VISIBLE) {
            targetRecyclerView.visibility = View.VISIBLE
        }

        emptyViewHelper?.detachAdapter(skeletonAdapter)

        targetRecyclerView.adapter = targetAdapter

        emptyViewHelper?.attachAdapter(targetAdapter)

        isShowing = false
    }

    override fun onAdapterDataChanged() {
        if (autoHide && isShowing) {
            hide()
        }
    }
}