package me.yifeiyuan.flap.view

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
import java.util.*

/**
 * 封装了 Flap 的 RecyclerView，未测试，暂时请不要使用。
 * todo
 *
 * Created by 程序亦非猿 on 2021/9/22.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 2020/9/22
 * @since 3.0
 */
open class FlapRecyclerView : RecyclerView {

    private lateinit var adapter: FlapAdapter

    private val dataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            super.onChanged()
            checkEmptyStatus()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            checkEmptyStatus()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            super.onItemRangeRemoved(positionStart, itemCount)
            checkEmptyStatus()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            super.onItemRangeChanged(positionStart, itemCount, payload)
            checkEmptyStatus()
        }
    }

    private val lifecycleObserver = object : LifecycleObserver {

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            /**
             * 手动切换 null,可以保证 Adapter#onDetachedFromRecyclerView 被调用
             */
            setAdapter(null)
            if (context is LifecycleOwner) {
                (context as LifecycleOwner).lifecycle.removeObserver(this)
            }
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
        adapter = FlapAdapter()
        setAdapter(adapter)

        if (context is LifecycleOwner) {
            context.lifecycle.addObserver(lifecycleObserver)
        }
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        this.adapter = adapter as FlapAdapter
        adapter.registerAdapterDataObserver(dataObserver)
        super.setAdapter(this.adapter)
    }

    override fun setLayoutManager(layout: LayoutManager?) {
        super.setLayoutManager(layout)
        when (layout) {
            is LinearLayoutManager -> {
                layout.recycleChildrenOnDetach = true
            }

            is GridLayoutManager ->{
                layout.recycleChildrenOnDetach = true
            }

            is StaggeredGridLayoutManager ->{
            }
        }
    }

    fun setData(data: MutableList<Any>) {
        adapter.setData(data)
    }

//    fun setLiveData(liveData: LiveData<MutableList<Any>>) {
//
//    }

    var emptyView: View? = null
        set(value) {
            field = value
            checkEmptyStatus()
        }

    private fun checkEmptyStatus() {
        if (adapter.itemCount == 0) {
            emptyView?.let {
                emptyView?.visibility = VISIBLE
                this.visibility = GONE
            }
        } else {
            emptyView?.let {
                emptyView?.visibility = GONE
                this.visibility = VISIBLE
            }
        }
    }
}