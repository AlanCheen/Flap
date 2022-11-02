package me.yifeiyuan.flap.hook

import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.FlapDebug
import me.yifeiyuan.flap.delegate.AdapterDelegate

/**
 *
 * LoggingHook 是一个调试阶段的日志打印帮助类，可以帮助开发调试。
 *
 * 请保证只在 Debug 包中使用。
 *
 * @param enableLog 是否打印日志
 *
 * Created by 程序亦非猿 on 2021/12/27.
 * @since 2021/12/27
 * @since 3.0.0
 */
class LoggingHook(private val enableLog: Boolean = true, private val printTrace: Boolean = false) : AdapterHook {

    companion object {
        private const val TAG = "LoggingHook"

        class TraceException : RuntimeException("|只用于 LoggingHook 打印日志，不是真实异常|")
    }

    override fun onCreateViewHolderStart(adapter: RecyclerView.Adapter<*>, delegate: AdapterDelegate<*, *>, viewType: Int) {
        if (enableLog) {
            FlapDebug.d(TAG, "onCreateViewHolderStart() called with: adapter = $adapter, delegate = $delegate, viewType = $viewType")
            if (printTrace) {
                FlapDebug.e(TAG, "onCreateViewHolderStart: ", TraceException())
            }
        }
    }

    override fun onCreateViewHolderEnd(adapter: RecyclerView.Adapter<*>, delegate: AdapterDelegate<*, *>, viewType: Int, component: Component<*>) {
        if (enableLog) {
            FlapDebug.d(TAG, "onCreateViewHolderEnd() called with: adapter = $adapter, delegate = $delegate, viewType = $viewType, component = $component")
        }
    }

    override fun onBindViewHolderStart(adapter: RecyclerView.Adapter<*>, delegate: AdapterDelegate<*, *>, component: Component<*>, data: Any, position: Int, payloads: MutableList<Any>) {
        if (enableLog) {
            FlapDebug.d(TAG, "onBindViewHolderStart() called with: adapter = $adapter, delegate = $delegate, component = $component, data = $data, position = $position, payloads = $payloads")

            if (printTrace) {
                FlapDebug.e(TAG, "onBindViewHolderStart: ", TraceException())
            }
        }
    }

    override fun onBindViewHolderEnd(adapter: RecyclerView.Adapter<*>, delegate: AdapterDelegate<*, *>, component: Component<*>, data: Any, position: Int, payloads: MutableList<Any>) {
        if (enableLog) {
            FlapDebug.d(TAG, "onBindViewHolderEnd() called with: adapter = $adapter, delegate = $delegate, component = $component, data = $data, position = $position, payloads = $payloads")
            if (payloads.isNotEmpty()) {
                //强调一下 payloads 的情况
                FlapDebug.d(TAG, "onBindViewHolderEnd: >>>> payloads 不为空 <<<<")
            }
        }
    }

    override fun onViewAttachedToWindow(adapter: RecyclerView.Adapter<*>, delegate: AdapterDelegate<*, *>, component: Component<*>) {
        if (enableLog) {
            FlapDebug.d(TAG, "onViewAttachedToWindow() called with: adapter = $adapter, delegate = $delegate, component = $component")
            if (printTrace) {
                FlapDebug.e(TAG, "onViewAttachedToWindow: ", TraceException())
            }
        }
    }

    override fun onViewDetachedFromWindow(adapter: RecyclerView.Adapter<*>, delegate: AdapterDelegate<*, *>, component: Component<*>) {
        if (enableLog) {
            FlapDebug.d(TAG, "onViewDetachedFromWindow() called with: adapter = $adapter, delegate = $delegate, component = $component")
            if (printTrace) {
                FlapDebug.e(TAG, "onViewDetachedFromWindow: ", TraceException())
            }
        }
    }

    override fun onAttachedToRecyclerView(adapter: RecyclerView.Adapter<*>, recyclerView: RecyclerView) {
        if (enableLog) {
            FlapDebug.d(TAG, "onAttachedToRecyclerView() called with: adapter = $adapter, recyclerView = $recyclerView")
        }
    }

    override fun onDetachedFromRecyclerView(adapter: RecyclerView.Adapter<*>, recyclerView: RecyclerView) {
        if (enableLog) {
            FlapDebug.d(TAG, "onDetachedFromRecyclerView() called with: adapter = $adapter, recyclerView = $recyclerView")
        }
    }
}