package me.yifeiyuan.flap.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.*
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flap.R
import me.yifeiyuan.flap.ext.DiffModel
import me.yifeiyuan.flap.ext.EmptyViewHelper
import me.yifeiyuan.flap.ext.FlapDiffAdapter

/**
 * TODO 待测试
 *
 * 封装了 Flap 的 RecyclerView，未测试，暂时请不要使用。
 *
 * Created by 程序亦非猿 on 2021/9/22.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 2020/9/22
 * @since 3.0
 */
open class FlapRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : RecyclerView(context, attrs, defStyleAttr), LifecycleObserver {

    companion object {
        private const val TAG = "FlapRecyclerView"

        const val LAYOUT_TYPE_LINEAR = 0
        const val LAYOUT_TYPE_GRID = 1
        const val LAYOUT_TYPE_STAGGERED = 2
    }

    private var flapAdapter: FlapAdapter? = null

    private val emptyViewHelper: EmptyViewHelper

    /**
     * 是否在 detach 的时候回收复用 View
     * 默认情况下 RecyclerView 不会回收
     */
    private var recycleChildrenOnDetach = true

    init {

        if (context is LifecycleOwner) {
            context.lifecycle.addObserver(this)
        }

        val array = context.obtainStyledAttributes(attrs, R.styleable.FlapRecyclerView)

        val layoutType = array.getInteger(R.styleable.FlapRecyclerView_flapLayoutManager, LAYOUT_TYPE_LINEAR)

        val isDiffEnable = array.getBoolean(R.styleable.FlapRecyclerView_flapDiffEnable, true)

        recycleChildrenOnDetach = array.getBoolean(R.styleable.FlapRecyclerView_flapRecycleChildrenOnDetach, true)

        array.recycle()

        //如果没有设置 LayoutManager，则设置 FlapXXXLM 作为默认
        if (layoutManager == null) {
            layoutManager = when (layoutType) {
                LAYOUT_TYPE_GRID -> FlapGridLayoutManager(context, attrs, defStyleAttr, 0)
                LAYOUT_TYPE_STAGGERED -> FlapStaggeredGridLayoutManager(context, attrs, defStyleAttr, 0)
                else -> FlapLinearLayoutManager(context, attrs, defStyleAttr, 0)
            }
        }

        //默认禁用 change 动画，以解决闪屏问题
        if (itemAnimator is SimpleItemAnimator) {
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }

        emptyViewHelper = EmptyViewHelper(this)

        flapAdapter = if (isDiffEnable) FlapDiffAdapter<DiffModel>() else FlapAdapter()

        setAdapter(flapAdapter)
    }

    /**
     *
     * @param adapter null or FlapAdapter
     */
    override fun setAdapter(adapter: Adapter<*>?) {
        if (adapter == null) {
            this.flapAdapter = null
            super.setAdapter(this.flapAdapter)
        } else if (adapter is FlapAdapter) {
            this.flapAdapter = adapter
            emptyViewHelper.attachAdapter(adapter)
            super.setAdapter(this.flapAdapter)
        } else {
            throw IllegalArgumentException("the new adapter is not a FlapAdapter")
        }
    }

    /**
     *
     * 在设置 LayoutManager 时处理 recycleChildrenOnDetach 属性
     */
    override fun setLayoutManager(layout: LayoutManager?) {
        super.setLayoutManager(layout)
        when (layout) {
            is LinearLayoutManager -> {
                layout.recycleChildrenOnDetach = recycleChildrenOnDetach
            }

            is GridLayoutManager -> {
                layout.recycleChildrenOnDetach = recycleChildrenOnDetach
            }

            is StaggeredGridLayoutManager -> {
            }
        }
    }

    fun setData(data: MutableList<Any>) {
        flapAdapter?.setData(data)
    }

    /**
     * 手动切换 null,可以保证 Adapter#onDetachedFromRecyclerView 被调用
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(owner: LifecycleOwner) {
        flapAdapter?.let {
            emptyViewHelper.detachAdapter(it)
        }
        setAdapter(null)
        owner.lifecycle.removeObserver(this)
    }

    /**
     * 滚动到顶部
     */
    fun scrollToTop() = scrollToPosition(0, 0)

    /**
     * 滚动列表
     */
    fun scrollToPosition(position: Int, offset: Int = 0) = when (layoutManager) {
        is LinearLayoutManager -> {
            (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(position, offset)
        }
        is StaggeredGridLayoutManager -> {
            (layoutManager as StaggeredGridLayoutManager).scrollToPositionWithOffset(position, offset)
        }
        else -> {
            layoutManager?.scrollToPosition(position)
        }
    }

    fun doOnPrefetch(offset: Int=0, minItemCount: Int=1, onPrefetch: () -> Unit){
        flapAdapter?.doOnPrefetch(offset, minItemCount, onPrefetch)
    }

    fun setPrefetchEnable(enable: Boolean) {
        flapAdapter?.setPrefetchEnable(enable)
    }

    fun setPrefetchComplete() {
        flapAdapter?.setPrefetchComplete()
    }

    var emptyView :View? = null
        set(value) {
            emptyViewHelper.emptyView = value
            field = value
        }

//    fun setEmptyView(view: View) {
//        emptyViewHelper.emptyView = view
//    }

}