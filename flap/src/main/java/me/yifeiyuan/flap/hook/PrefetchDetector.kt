package me.yifeiyuan.flap.hook

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flap.AdapterDelegate
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.FlapAdapter
import java.util.concurrent.atomic.AtomicBoolean

/**
 *  RecyclerView 预取功能检测，当滑动到 position >= itemCount-offset 时会触发预加载
 *
 *  例如 itemCount=21 ，offset=3，那么当滑动到 position=18 的位置会触发预加载机制，并调用 onPrefetch() ，可以在此时加载跟多数据。
 *
 *  注意：
 *  1. 每个 PrefetchDetector 只能绑定一个 Adapter，不可复用。
 *  2. 当预取后出现异常情况，例如加载更多请求失败，需要手动调用 setPrefetchComplete() ，否则不会进行下一次预取检测。
 *
 *  Created by 程序亦非猿 on 2021/9/28.
 *
 *  @param offset 偏移量，决定滑动到哪个 position 触发预取，默认滑动到底部最后一个组件可见时触发
 *  @param onPrefetch 触发预取后执行
 */
class PrefetchDetector(private val offset: Int = 0, private val onPrefetch: () -> Unit) : AdapterHook {

    companion object{
        private const val TAG = "PrefetchDetector"
    }

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

        if (itemCount > 0 && position + offset >= itemCount) {
            if (fetching.get()) {
                return
            }
            fetching.set(true)
            onPrefetch()
            Log.d(TAG, "触发预加载，当前 position = $position，itemCount = $itemCount")
        }
    }

    fun setPrefetchComplete() {
        fetching.set(false)
    }
}