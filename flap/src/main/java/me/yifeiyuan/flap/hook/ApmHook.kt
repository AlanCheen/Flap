package me.yifeiyuan.flap.hook

import android.os.SystemClock
import me.yifeiyuan.flap.delegate.AdapterDelegate
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flap.FlapDebug

/**
 *
 * ApmHook 是一个简易的组件性能监控工具：
 * 1. 监控创建组件耗时，当超过阈值 {createTimeCostThreshold} 时会回调 onCreateAlarm；
 * 2. 监控绑定组件耗时，当超过阈值 {bindTimeCostThreshold} 时会回调 onBindAlarm；
 *
 * Created by 程序亦非猿 on 2021/9/22.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 2020/9/22
 * @since 3.0
 *
 * @param createTimeCostThreshold 创建组件耗时阈值，默认 20 ms
 * @param bindTimeCostThreshold 绑定组件耗时阈值，默认 5 ms
 */
open class ApmHook(private val createTimeCostThreshold: Long = 20, private val bindTimeCostThreshold: Long = 5) : AdapterHook {

    companion object {
        private const val TAG = "ApmHook"
    }

    private var createStartTime: Long = 0
    private var bindStartTime: Long = 0

    override fun onCreateViewHolderStart(adapter: FlapAdapter, delegate: AdapterDelegate<*, *>?, viewType: Int) {
        FlapDebug.d(
                TAG,
                "${delegate?.javaClass?.simpleName} 开始创建 Component : viewType = $viewType"
        )
        createStartTime = SystemClock.uptimeMillis()
    }

    override fun onCreateViewHolderEnd(
            adapter: FlapAdapter,
            delegate: AdapterDelegate<*, *>?,
            viewType: Int,
            component: Component<*>
    ) {
        val endTime = SystemClock.uptimeMillis()
        val cost = endTime - createStartTime
        FlapDebug.d(
                TAG,
                "${delegate?.javaClass?.simpleName} 完成创建: Component = $component，cost = $cost (毫秒)"
        )
        if (cost > createTimeCostThreshold) {
            onCreateAlarm()
        }
    }

    open fun onCreateAlarm() {
        FlapDebug.w(TAG, "组件创建耗时过长，请优化！")
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
        FlapDebug.d(
                TAG,
                "${delegate?.javaClass?.simpleName} 开始绑定 $component :  data = $data, position = $position, payloads = $payloads"
        )
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
                "${delegate?.javaClass?.simpleName} 完成绑定: position = $position, cost = $cost (毫秒)"
        )
        if (cost > bindStartTime) {
            onBindAlarm()
        }
    }

    open fun onBindAlarm() {
        FlapDebug.w(TAG, "组件绑定耗时过长，请优化！")
    }
}