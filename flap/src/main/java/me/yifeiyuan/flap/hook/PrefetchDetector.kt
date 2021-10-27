package me.yifeiyuan.flap.hook

import android.util.Log
import androidx.annotation.IntRange
import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flap.AdapterDelegate
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.FlapAdapter
import java.util.concurrent.atomic.AtomicBoolean

/**
 *  RecyclerView 预取功能检测，会检测 RecyclerView 的滑动状态，当滑动到 position >= itemCount-1-offset 时会触发预加载
 *
 *  举例：
 *  1. offset = 0 时，滑动到最底部最后一个可见时触发
 *  2. offset = 1 时，最后第二个可见时触发
 *
 *  例如 itemCount=20 ，offset=4，那么当滑动到 position=15 的位置会触发预加载机制，并调用 onPrefetch() ，可以在此时加载跟多数据。
 *
 *  注意：
 *  1. 每个 PrefetchDetector 只能绑定一个 Adapter，不可复用。
 *  2. 当预取后出现异常情况，例如加载更多请求失败，需要手动调用 setPrefetchComplete() ，否则不会进行下一次预取检测。
 *
 *  Created by 程序亦非猿 on 2021/9/28.
 *
 *  @param offset 偏移量，默认值 0 表示最后一个，1 表示最后第二个，以此类推，一般取值 3~5 会是不错的选择
 *  @param onPrefetch 触发预取后执行
 */
class PrefetchDetector(private val offset: Int = 0, private val onPrefetch: () -> Unit) : AdapterHook {

    companion object {
        private const val TAG = "PrefetchDetector"
    }

    init {
        if (offset < 0) {
            throw IllegalArgumentException("offset must be >= 0")
        }
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

        if (itemCount > 0 && position + offset >= itemCount - 1) {
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