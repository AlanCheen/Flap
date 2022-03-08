package me.yifeiyuan.flap.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flap.R
import me.yifeiyuan.flap.ext.DiffModel
import me.yifeiyuan.flap.ext.FlapDiffAdapter

/**
 * 封装了 Flap 的 RecyclerView，未测试，暂时请不要使用。
 *
 * Created by 程序亦非猿 on 2021/9/22.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 2020/9/22
 * @since 3.0
 */
open class FlapRecyclerView : RecyclerView, LifecycleObserver {

    companion object {
        private const val TAG = "FlapRecyclerView"

        const val LAYOUT_TYPE_LINEAR = 0
        const val LAYOUT_TYPE_GRID = 1
        const val LAYOUT_TYPE_STAGGERED = 2
    }

    private lateinit var adapter: FlapAdapter

    private val dataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            super.onChanged()
            checkEmptyState()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            checkEmptyState()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            super.onItemRangeRemoved(positionStart, itemCount)
            checkEmptyState()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            super.onItemRangeChanged(positionStart, itemCount, payload)
            checkEmptyState()
        }
    }

    constructor(context: Context) : super(context) {
        init(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        init(context, attrs, defStyle)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyle: Int) {

        if (context is LifecycleOwner) {
            context.lifecycle.addObserver(this)
        }

        val array = context.obtainStyledAttributes(attrs, R.styleable.FlapRecyclerView)

        val layoutType = array.getInteger(R.styleable.FlapRecyclerView_flapLayoutType, LAYOUT_TYPE_LINEAR)

        val isDiffEnable = array.getBoolean(R.styleable.FlapRecyclerView_flapDiffEnable, true)

        array.recycle()

        //如果没有设置 LayoutManager，则设置 FlapXXXLM 作为默认
        if (layoutManager == null) {
            layoutManager = when (layoutType) {
                LAYOUT_TYPE_GRID -> FlapGridLayoutManager(context, attrs, defStyle, 0)
                LAYOUT_TYPE_STAGGERED -> FlapStaggeredGridLayoutManager(context, attrs, defStyle, 0)
                else -> FlapLinearLayoutManager(context, attrs, defStyle, 0)
            }
        }

        adapter = if (isDiffEnable) FlapAdapter() else FlapDiffAdapter<DiffModel>()

        setAdapter(adapter)
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        this.adapter = adapter as FlapAdapter
        this.adapter?.registerAdapterDataObserver(dataObserver)
        super.setAdapter(this.adapter)
    }

    override fun setLayoutManager(layout: LayoutManager?) {
        super.setLayoutManager(layout)
        when (layout) {
            is LinearLayoutManager -> {
                layout.recycleChildrenOnDetach = true
            }

            is GridLayoutManager -> {
                layout.recycleChildrenOnDetach = true
            }

            is StaggeredGridLayoutManager -> {
            }
        }
    }

    fun setData(data: MutableList<Any>) {
        adapter.setData(data)
    }

    /**
     * 设置空状态下需要展示的 View
     * 当 adapter.itemCount == 0 时会展示；
     * 当 adapter.itemCount != 0 会隐藏；
     * 注意：emptyView 必须是已经加入到布局中的，是有 parent 的
     */
    var emptyView: View? = null
        set(value) {
            field = value
            checkEmptyState()
        }

    private fun checkEmptyState() {
        emptyView?.let {
            if (adapter.itemCount == 0) {
                it.visibility = VISIBLE
                this.visibility = GONE
            } else {
                it.visibility = GONE
                this.visibility = VISIBLE
            }
        }
    }

    /**
     * 手动切换 null,可以保证 Adapter#onDetachedFromRecyclerView 被调用
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        destroy()
    }

    fun destroy() {
        setAdapter(null)
        if (context is LifecycleOwner) {
            (context as LifecycleOwner).lifecycle.removeObserver(this)
        }
    }

    /**
     * 滚动到顶部
     */
    fun scrollToTop() = scrollTo(0, 0)

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

}