package me.yifeiyuan.flap.skeleton

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import java.lang.IllegalArgumentException

/**
 *
 * 实现 Skeleton（骨架屏） 效果
 *
 * Created by 程序亦非猿 on 2022/8/11.
 *
 * @since 3.0.1
 */
class Skeleton : RecyclerView.AdapterDataObserver() {

    private lateinit var targetRecyclerView: RecyclerView
    private lateinit var targetAdapter: RecyclerView.Adapter<*>

    private var skeletonAdapter: SkeletonAdapter = SkeletonAdapter()

    var isShowing = false

    var autoHide = false

    fun bind(recyclerView: RecyclerView): Skeleton {
        targetRecyclerView = recyclerView
        targetRecyclerView.adapter?.let {
            adapter(it)
        }
        return this
    }

    fun adapter(adapter: RecyclerView.Adapter<*>): Skeleton {
        targetAdapter = adapter
        return this
    }

    fun layout(@LayoutRes layoutRes: Int): Skeleton {
        skeletonAdapter.skeletonLayoutRes = layoutRes
        return this
    }

    fun layouts(layouts: ((position: Int) -> Int)): Skeleton {
        skeletonAdapter.multiSkeletonLayoutRes = layouts
        return this
    }

    fun skeletonCount(count: Int): Skeleton {
        skeletonAdapter.skeletonItemCount = count
        return this
    }

    fun show(): Skeleton {
        if (skeletonAdapter.skeletonItemCount < 0) {
            throw IllegalArgumentException("skeletonCount 不能小于 0！")
        }
        if (skeletonAdapter.skeletonLayoutRes <= 0 && skeletonAdapter.multiSkeletonLayoutRes == null) {
            throw IllegalArgumentException("未设置 skeletonLayoutRes 和 multiSkeletonLayoutRes ！")
        }

        if (targetRecyclerView.visibility != View.VISIBLE) {
            targetRecyclerView.visibility = View.VISIBLE
        }
        targetRecyclerView.adapter = skeletonAdapter

        isShowing = true
        return this
    }

    fun autoHide(autoHide: Boolean): Skeleton {
        this.autoHide = autoHide
        if (autoHide) {
            targetAdapter.registerAdapterDataObserver(this)
        }
        return this
    }

    fun hide(): Skeleton {
        if (targetRecyclerView.visibility != View.VISIBLE) {
            targetRecyclerView.visibility = View.VISIBLE
        }
        targetRecyclerView.adapter = targetAdapter

        isShowing = false
        return this
    }

    override fun onChanged() {
        super.onChanged()
        handleDataChange()
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
        super.onItemRangeChanged(positionStart, itemCount)
        handleDataChange()
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
        super.onItemRangeChanged(positionStart, itemCount, payload)
        handleDataChange()
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        super.onItemRangeInserted(positionStart, itemCount)
        handleDataChange()
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        super.onItemRangeRemoved(positionStart, itemCount)
        handleDataChange()
    }

    override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
        super.onItemRangeMoved(fromPosition, toPosition, itemCount)
        handleDataChange()
    }

    override fun onStateRestorationPolicyChanged() {
        super.onStateRestorationPolicyChanged()
        handleDataChange()
    }

    private fun handleDataChange() {
        if (autoHide && isShowing) {
            hide()
        }
    }
}