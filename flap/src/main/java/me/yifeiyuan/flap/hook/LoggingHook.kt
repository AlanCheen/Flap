package me.yifeiyuan.flap.hook

import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flap.FlapDebug
import me.yifeiyuan.flap.delegate.AdapterDelegate

private const val TAG = "LoggingHook"

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
class LoggingHook(private val enableLog: Boolean = true) : AdapterHook {

    override fun onCreateViewHolderStart(adapter: FlapAdapter, delegate: AdapterDelegate<*, *>, viewType: Int) {
        if (enableLog) {
            FlapDebug.d(TAG, "onCreateViewHolderStart() called with: adapter = $adapter, delegate = $delegate, viewType = $viewType")
        }
    }

    override fun onCreateViewHolderEnd(adapter: FlapAdapter, delegate: AdapterDelegate<*, *>, viewType: Int, component: Component<*>) {
        if (enableLog) {
            FlapDebug.d(TAG, "onCreateViewHolderEnd() called with: adapter = $adapter, delegate = $delegate, viewType = $viewType, component = $component")
        }
    }

    override fun onBindViewHolderStart(adapter: FlapAdapter, delegate: AdapterDelegate<*, *>, component: Component<*>, data: Any, position: Int, payloads: MutableList<Any>) {
        if (enableLog) {
            FlapDebug.d(TAG, "onBindViewHolderStart() called with: adapter = $adapter, delegate = $delegate, component = $component, data = $data, position = $position, payloads = $payloads")
        }
    }

    override fun onBindViewHolderEnd(adapter: FlapAdapter, delegate: AdapterDelegate<*, *>, component: Component<*>, data: Any, position: Int, payloads: MutableList<Any>) {
        if (enableLog) {
            FlapDebug.d(TAG, "onBindViewHolderEnd() called with: adapter = $adapter, delegate = $delegate, component = $component, data = $data, position = $position, payloads = $payloads")
            if (payloads.isNotEmpty()) {
                //强调一下 payloads 的情况
                FlapDebug.d(TAG, "onBindViewHolderEnd: >>>> payloads 不为空 <<<<")
            }
        }
    }

    override fun onViewAttachedToWindow(adapter: FlapAdapter, delegate: AdapterDelegate<*, *>, component: Component<*>) {
        if (enableLog) {
            FlapDebug.d(TAG, "onViewAttachedToWindow() called with: adapter = $adapter, delegate = $delegate, component = $component")
        }
    }

    override fun onViewDetachedFromWindow(adapter: FlapAdapter, delegate: AdapterDelegate<*, *>, component: Component<*>) {
        if (enableLog) {
            FlapDebug.d(TAG, "onViewDetachedFromWindow() called with: adapter = $adapter, delegate = $delegate, component = $component")
        }
    }
}