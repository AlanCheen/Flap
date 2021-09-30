package me.yifeiyuan.flap.hook

import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flap.AdapterDelegate
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.FlapAdapter
import java.util.concurrent.atomic.AtomicBoolean

/**
 *  RecyclerView 预取功能检测
 *
 *  Created by 程序亦非猿 on 2021/9/28.
 *
 *  @param offset 偏移量，决定何时触发预取机制，默认滑动到底部最后一个触发
 *  @param onPrefetch 触发预取后执行
 */
class PrefetchDetector(private val offset: Int = 1, private val onPrefetch: () -> Unit) : AdapterHook {

    private val fetching = AtomicBoolean(false)

    private var registered = false

    var prefetchEnable = true

    //reset when data changed
    private val observer = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            fetching.set(false)
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            fetching.set(false)
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            fetching.set(false)
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            fetching.set(false)
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            fetching.set(false)
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            fetching.set(false)
        }
    }

    override fun onCreateViewHolderStart(adapter: FlapAdapter, delegate: AdapterDelegate<*, *>?, viewType: Int) {
        if (!registered) {
            registered = true
            adapter.registerAdapterDataObserver(observer)
        }
    }

    override fun onBindViewHolderEnd(adapter: FlapAdapter, delegate: AdapterDelegate<*, *>, component: Component<*>, data: Any, position: Int, payloads: MutableList<Any>) {
        if (!prefetchEnable) {
            return
        }
        val itemCount = adapter.itemCount

        if (position + offset >= itemCount) {
            considerPrefetch()
        }
    }

    private fun considerPrefetch() {
        if (fetching.get()) {
            return
        }
        fetching.set(true)
        onPrefetch()
    }

    fun setPrefetchComplete() {
        fetching.set(false)
    }
}