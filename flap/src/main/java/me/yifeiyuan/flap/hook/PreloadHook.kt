package me.yifeiyuan.flap.hook

import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.FlapDebug
import me.yifeiyuan.flap.delegate.AdapterDelegate
import me.yifeiyuan.flap.ext.OnAdapterDataChangedObserver
import java.util.concurrent.atomic.AtomicBoolean

/**
 *  RecyclerView 预取功能检测，会检测 RecyclerView 的滑动状态，当滑动到 position >= itemCount-1-offset 时会触发预加载
 *
 *  举例：
 *  1. offset = 0 时，滑动到最底部最后一个可见时触发
 *  2. offset = 1 时，最后第二个可见时触发
 *
 *  例如 itemCount=20 ，offset=4，那么当滑动到 position=15 的位置会触发预加载机制，可以在此时加载跟多数据。
 *
 *  注意：
 *  1. 每个 PreloadHook 只能绑定一个 Adapter，不可复用。
 *  2. 当预取后出现异常情况，例如加载更多请求失败，需要手动调用 setPreloadComplete() ，否则不会进行下一次预取检测。
 *
 *  @param minItemCount 触发预加载时 Adapter.itemCount 的最小数量要求，可以防止数量不够一页的时候也触发预加载，建议设置成 pageSize
 *  @param offset 偏移量，默认值 0 表示底部最后一个，1 表示最后第二个，以此类推，一般取值 3~5 会是不错的选择
 *  @param onPreload 触发预取后执行
 *
 *  Created by 程序亦非猿 on 2021/9/28.
 *  @since 2021/9/28
 *  @since 3.0.0
 */
class PreloadHook(private val offset: Int = 0, private val minItemCount: Int = 2, private val direction: Int = SCROLL_DOWN, private val onPreload: () -> Unit) : AdapterHook {

    companion object {
        private const val TAG = "PreloadHook"

        /**
         * 向底部滚动，手指往上滑动
         */
        const val SCROLL_DOWN = 0

        /**
         * 向顶部滚动，手指往下滑动
         */
        const val SCROLL_UP = 1
    }

    init {
        if (offset < 0) {
            throw IllegalArgumentException("offset must be >= 0")
        }

        if (minItemCount < 0) {
            throw IllegalArgumentException("minItemCount must be >= 0")
        }
    }

    private val loading = AtomicBoolean(false)

    private var registered = false

    var preloadEnable = true

    /**
     * 当数据变化就自动重置
     */
    private val observer = object : OnAdapterDataChangedObserver() {
        override fun onAdapterDataChanged() {
            setPreloadComplete()
        }
    }

    override fun onCreateViewHolderStart(adapter: RecyclerView.Adapter<*>, delegate: AdapterDelegate<*, *>, viewType: Int) {
        if (!registered) {
            registered = true
            adapter.registerAdapterDataObserver(observer)
        }
    }

    private var prePosition = -1

    override fun onBindViewHolderEnd(adapter: RecyclerView.Adapter<*>, delegate: AdapterDelegate<*, *>, component: Component<*>, data: Any, position: Int, payloads: MutableList<Any>) {
        val itemCount = adapter.itemCount
        if (preloadEnable && !loading.get()) {
            if (direction == SCROLL_DOWN && prePosition < position) {
                if (itemCount >= minItemCount && position + offset >= itemCount - 1) {
                    loading.set(true)
                    onPreload()
                    FlapDebug.d(TAG, "滑动到「底部」触发预加载，当前 position = $position，itemCount = $itemCount")
                }
            } else if (direction == SCROLL_UP && prePosition > position) {
                if (itemCount >= minItemCount && position - offset <= 0) {
                    loading.set(true)
                    onPreload()
                    FlapDebug.d(TAG, "滑动到「顶部」触发预加载，当前 position = $position，itemCount = $itemCount")
                }
            }
        }

        prePosition = position
    }

    fun setPreloadComplete() {
        loading.set(false)
    }

}