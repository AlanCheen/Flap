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
import me.yifeiyuan.flap.differ.IDiffer
import me.yifeiyuan.flap.differ.FlapDifferAdapter

/**
 * TODO 待测试
 *
 * 封装了 Flap 的 RecyclerView，未测试，暂时请不要使用。
 *
 * FlapRecyclerView 和 Flap 的 Adapters 和 LayoutManagers 是深度绑定的。
 *
 * Created by 程序亦非猿 on 2021/9/22.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 2020/9/22
 * @since TODO 
 */
open class FlapRecyclerView
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : RecyclerView(context, attrs, defStyleAttr), LifecycleObserver {

    companion object {
        private const val TAG = "FlapRecyclerView"

        const val LAYOUT_TYPE_LINEAR = 0
        const val LAYOUT_TYPE_GRID = 1
        const val LAYOUT_TYPE_STAGGERED = 2

        const val DEFAULT_LAYOUT_TYPE = LAYOUT_TYPE_LINEAR
    }

    /**
     * RecyclerView.VERTICAL
     * RecyclerView.HORIZONTAL
     */
    var orientation: Int = VERTICAL
        set(value) {
            if (value != HORIZONTAL && value != VERTICAL) {
                throw IllegalArgumentException()
            }
            field = value
            when (layoutManager) {
                is LinearLayoutManager -> {
                    (layoutManager as LinearLayoutManager).orientation = value
                }
                is GridLayoutManager -> {
                    (layoutManager as GridLayoutManager).orientation = value
                }
                is StaggeredGridLayoutManager -> {
                    (layoutManager as StaggeredGridLayoutManager).orientation = value
                }
            }
        }

    private var flapAdapter: FlapAdapter? = null

    /**
     * 是否在 detach 的时候回收复用 View
     * 默认情况下 RecyclerView 不会回收
     */
    private var recycleChildrenOnDetach = true

    private var supportsChangeAnimations = true

    var emptyView: View? = null
        set(value) {
            flapAdapter?.setEmptyView(value)
            field = value
        }

    init {

        if (context is LifecycleOwner) {
            context.lifecycle.addObserver(this)
        }

        val flapTypedArray = context.obtainStyledAttributes(attrs, R.styleable.FlapRecyclerView)

        val layoutType = flapTypedArray.getInt(R.styleable.FlapRecyclerView_flapLayoutManager, DEFAULT_LAYOUT_TYPE)

        val useDifferAdapter = flapTypedArray.getBoolean(R.styleable.FlapRecyclerView_flapUseDifferAdapter, true)

        recycleChildrenOnDetach = flapTypedArray.getBoolean(R.styleable.FlapRecyclerView_flapRecycleChildrenOnDetach, true)

        supportsChangeAnimations = flapTypedArray.getBoolean(R.styleable.FlapRecyclerView_flapSupportsChangeAnimations, false)

        flapTypedArray.recycle()

        //获取 orientation ，类似的 spanCount 等也可以这么获取
        val rvTypedArray = context.obtainStyledAttributes(attrs, androidx.recyclerview.R.styleable.RecyclerView)

        val rvOrientation = rvTypedArray.getInt(androidx.recyclerview.R.styleable.RecyclerView_android_orientation, HORIZONTAL)
//        val rvSpanCount = rvTypedArray.getInt(androidx.recyclerview.R.styleable.RecyclerView_spanCount, 2)
//        val reverseLayout = rvTypedArray.getBoolean(androidx.recyclerview.R.styleable.RecyclerView_reverseLayout, false)
//        val stackFromEnd = rvTypedArray.getBoolean(androidx.recyclerview.R.styleable.RecyclerView_stackFromEnd, false)

        orientation = rvOrientation
        rvTypedArray.recycle()

        //如果没有设置 LayoutManager，则设置 FlapLinearLayoutManager 作为默认
        if (layoutManager == null) {
            layoutManager = when (layoutType) {
                LAYOUT_TYPE_GRID -> FlapGridLayoutManager(context, attrs, defStyleAttr, 0)
                LAYOUT_TYPE_STAGGERED -> FlapStaggeredGridLayoutManager(context, attrs, defStyleAttr, 0)
                else -> FlapLinearLayoutManager(context, attrs, defStyleAttr, 0)
            }
        }

        //默认禁用 change 动画，以解决闪屏问题
        if (itemAnimator is SimpleItemAnimator) {
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = supportsChangeAnimations
        }

        flapAdapter = if (useDifferAdapter) FlapDifferAdapter<IDiffer>() else FlapAdapter()

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
            super.setAdapter(this.flapAdapter)
        } else {
            throw IllegalArgumentException("setAdapter 只支持 Flap 内置的 Adapter！")
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

    override fun setItemAnimator(animator: ItemAnimator?) {
        super.setItemAnimator(animator)
        if (animator is SimpleItemAnimator) {
            animator.supportsChangeAnimations = supportsChangeAnimations
        }
    }

    /**
     * 手动切换 null,可以保证 Adapter#onDetachedFromRecyclerView 被调用
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(owner: LifecycleOwner) {
        owner.lifecycle.removeObserver(this)
//        flapAdapter?.onDetachedFromRecyclerView(this)
        setAdapter(null)
    }

    /**
     * 滚动到顶部
     */
    fun scrollToTop() = scrollToPosition(0, 0)

    fun scrollToBottom() = run {
        if (flapAdapter != null && layoutManager != null) {
            // TODO: 2022/7/20 find bottom
        }
    }

    /**
     * 滚动列表到指定位置
     *
     * @param position
     * @param offset
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

    fun smoothScrollToPosition(position: Int){
//        when(layoutManager){
//            is LinearLayoutManager->{
//                (layoutManager as LinearLayoutManager).smoothScrollToPosition()
//            }
//        }
    }

    /**
     * 设置预加载触发时的行为
     */
    fun doOnPrefetch(offset: Int = 0, minItemCount: Int = 2, onPrefetch: () -> Unit) {
        flapAdapter?.doOnPreload(offset, minItemCount, onPrefetch)
    }

    /**
     * 设置是否开启预加载
     */
    fun setPrefetchEnable(enable: Boolean) {
        flapAdapter?.setPreloadEnable(enable)
    }

    /**
     * 设置一次预加载行为完成
     */
    fun setPrefetchComplete() {
        flapAdapter?.setPreloadComplete()
    }
}