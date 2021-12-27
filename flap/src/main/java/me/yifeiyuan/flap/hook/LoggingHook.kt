package me.yifeiyuan.flap.hook

import me.yifeiyuan.flap.delegate.AdapterDelegate
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flap.FlapDebug

/**
 * Created by 程序亦非猿 on 2021/12/27.
 */
class LoggingHook : AdapterHook {

    companion object{
        private const val TAG = "Logging"
    }
    override fun onCreateViewHolderStart(adapter: FlapAdapter, delegate: AdapterDelegate<*, *>?, viewType: Int) {
        FlapDebug.d(TAG, "onCreateViewHolderStart() called with: adapter = $adapter, delegate = $delegate, viewType = $viewType")
    }

    override fun onCreateViewHolderEnd(adapter: FlapAdapter, delegate: AdapterDelegate<*, *>?, viewType: Int, component: Component<*>) {
        FlapDebug.d(TAG, "onCreateViewHolderEnd() called with: adapter = $adapter, delegate = $delegate, viewType = $viewType, component = $component")
    }

    override fun onBindViewHolderStart(adapter: FlapAdapter, delegate: AdapterDelegate<*, *>, component: Component<*>, data: Any, position: Int, payloads: MutableList<Any>) {
        FlapDebug.d(TAG, "onBindViewHolderStart() called with: adapter = $adapter, delegate = $delegate, component = $component, data = $data, position = $position, payloads = $payloads")
    }

    override fun onBindViewHolderEnd(adapter: FlapAdapter, delegate: AdapterDelegate<*, *>, component: Component<*>, data: Any, position: Int, payloads: MutableList<Any>) {
        FlapDebug.d(TAG, "onBindViewHolderEnd() called with: adapter = $adapter, delegate = $delegate, component = $component, data = $data, position = $position, payloads = $payloads")
    }
}