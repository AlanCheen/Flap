package me.yifeiyuan.flap.hook

import android.os.SystemClock
import me.yifeiyuan.flap.delegate.AdapterDelegate
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flap.FlapDebug

/**
 *
 * ApmHook 是一个简易的组件性能监控工具，可以监控组件的创建和数据绑定的耗时。
 *
 * 有如下功能：
 * 1. 监控创建组件耗时，当超过阈值 {createTimeCostThreshold} 时会回调 onCreateAlarm；
 * 2. 监控绑定组件耗时，当超过阈值 {bindTimeCostThreshold} 时会回调 onBindAlarm；
 *
 * @param createTimeCostThreshold 创建组件耗时阈值，默认 20 ms
 * @param bindTimeCostThreshold 绑定组件耗时阈值，默认 3 ms
 *
 * @see onCreateAlarm
 * @see onBindAlarm
 *
 * Created by 程序亦非猿 on 2021/9/22.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 2020/9/22
 * @since 3.0.0
 */
open class ApmHook(private val createTimeCostThreshold: Long = 20, private val bindTimeCostThreshold: Long = 3) : AdapterHook {

    companion object {
        private const val TAG = "APM"
    }

    private var createStartTime: Long = 0
    private var bindStartTime: Long = 0

    override fun onCreateViewHolderStart(adapter: FlapAdapter, delegate: AdapterDelegate<*, *>, viewType: Int) {
        createStartTime = SystemClock.uptimeMillis()
    }

    override fun onCreateViewHolderEnd(
            adapter: FlapAdapter,
            delegate: AdapterDelegate<*, *>,
            viewType: Int,
            component: Component<*>
    ) {
        val endTime = SystemClock.uptimeMillis()
        val cost = endTime - createStartTime
        FlapDebug.d(
                TAG,
                "${delegate?.javaClass?.simpleName} 【创建】组件完成，耗时 $cost (毫秒)，组件为：$component"
        )
        if (cost > createTimeCostThreshold) {
            onCreateAlarm(adapter, delegate, viewType, component, cost)
        }
    }

    /**
     * 当创建耗时太长时警告
     */
    open fun onCreateAlarm(
            adapter: FlapAdapter,
            delegate: AdapterDelegate<*, *>?,
            viewType: Int,
            component: Component<*>, cost: Long) {
        FlapDebug.e(TAG, "组件创建耗时过长，请优化！")
    }

    override fun onBindViewHolderStart(
            adapter: FlapAdapter,
            delegate: AdapterDelegate<*, *>,
            component: Component<*>,
            data: Any,
            position: Int,
            payloads: MutableList<Any>
    ) {
        bindStartTime = SystemClock.uptimeMillis()
    }

    override fun onBindViewHolderEnd(
            adapter: FlapAdapter,
            delegate: AdapterDelegate<*, *>,
            component: Component<*>,
            data: Any,
            position: Int,
            payloads: MutableList<Any>
    ) {
        val endTime = SystemClock.uptimeMillis()
        val cost = endTime - bindStartTime
        FlapDebug.d(
                TAG,
                "${delegate.javaClass.simpleName} 【绑定】组件完成，耗时 $cost (毫秒)，组件为：$component"
        )
        if (cost > bindStartTime) {
            onBindAlarm(adapter, delegate, component, data, position, payloads, cost)
        }
    }

    /**
     * 当绑定耗时太长时警告
     */
    open fun onBindAlarm(
            adapter: FlapAdapter,
            delegate: AdapterDelegate<*, *>,
            component: Component<*>,
            data: Any,
            position: Int,
            payloads: MutableList<Any>, cost: Long) {
        FlapDebug.e(TAG, "组件绑定耗时过长，请优化！")
    }
}