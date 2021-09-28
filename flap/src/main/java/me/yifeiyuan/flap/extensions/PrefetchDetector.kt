package me.yifeiyuan.flap.extensions

import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flap.AdapterDelegate
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.FlapAdapter
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by 程序亦非猿 on 2021/9/28.
 */
class PrefetchDetector(var offset: Int = 1, val onPrefetch: () -> Unit) : AdapterHook {

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
    }

    override fun onCreateViewHolderStart(adapter: FlapAdapter, delegate: AdapterDelegate<*, *>?, viewType: Int) {
        if (!registered) {
            registered = true
            adapter.registerAdapterDataObserver(observer)
        }
    }

    override fun onBindViewHolderStart(adapter: FlapAdapter, delegate: AdapterDelegate<*, *>, component: Component<*>, data: Any, position: Int, payloads: MutableList<Any>) {
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