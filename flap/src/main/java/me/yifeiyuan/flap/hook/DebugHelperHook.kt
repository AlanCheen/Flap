package me.yifeiyuan.flap.hook

import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flap.FlapDebug
import me.yifeiyuan.flap.delegate.AdapterDelegate

private const val TAG = "DebugHelperHook"

/**
 *
 * DebugHelperHook 是一个调试阶段的帮助类，可以帮助开发调试。
 *
 * 请保证只在 Debug 包中使用。
 *
 * 1. 在关键流程输出日志
 *
 * @param enableLog 是否打印日志
 * @param highlightWhenBind 是否高亮？TODO
 *
 * Created by 程序亦非猿 on 2021/12/27.
 * @since 2021/12/27
 * @since 3.0.0
 */
class DebugHelperHook(private val enableLog: Boolean = true, private val highlightWhenBind: Boolean = false) : AdapterHook {

    override fun onCreateViewHolderStart(adapter: FlapAdapter, delegate: AdapterDelegate<*, *>?, viewType: Int) {
        if (enableLog) {
            FlapDebug.d(TAG, "${delegate?.javaClass?.simpleName} 创建组件开始: adapter = $adapter, delegate = $delegate, viewType = $viewType")
        }
    }

    override fun onCreateViewHolderEnd(adapter: FlapAdapter, delegate: AdapterDelegate<*, *>?, viewType: Int, component: Component<*>) {
        if (enableLog) {
            FlapDebug.d(TAG, "${delegate?.javaClass?.simpleName} 创建组件完成 : component=$component")
        }
    }

    override fun onBindViewHolderStart(adapter: FlapAdapter, delegate: AdapterDelegate<*, *>, component: Component<*>, data: Any, position: Int, payloads: MutableList<Any>) {
        if (enableLog) {
            FlapDebug.d(TAG, "${delegate.javaClass.simpleName} 绑定组件开始 : adapter = $adapter, delegate = $delegate, component = $component, data = $data, position = $position, payloads = $payloads")
        }

        if (highlightWhenBind) {
            // TODO: 2022/7/21 高亮 提示一下 这个 Component 执行 onBind?
        }
    }

    override fun onBindViewHolderEnd(adapter: FlapAdapter, delegate: AdapterDelegate<*, *>, component: Component<*>, data: Any, position: Int, payloads: MutableList<Any>) {
        if (enableLog) {
            FlapDebug.d(TAG, "${delegate.javaClass.simpleName} 绑定组件完成 : adapter = $adapter, delegate = $delegate, component = $component, data = $data, position = $position, payloads = $payloads")
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