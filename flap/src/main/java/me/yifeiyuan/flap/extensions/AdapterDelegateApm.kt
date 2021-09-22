package me.yifeiyuan.flap.extensions

import android.os.SystemClock
import me.yifeiyuan.flap.AdapterDelegate
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.FlapDebug

/**
 * 组件性能监控
 *
 *
 * Created by 程序亦非猿 on 2021/9/22.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 2020/9/22
 * @since 3.0
 */
open class AdapterDelegateApm : AdapterHook {

    companion object {
        private const val TAG = "DelegateApm"
    }

    private var createStartTime: Long = 0
    private var bindStartTime: Long = 0

    override fun onCreateViewHolderStart(delegate: AdapterDelegate<*, *>?, viewType: Int) {
        FlapDebug.d(
                TAG,
                "${delegate?.javaClass?.simpleName} 开始创建 Component : viewType = $viewType"
        )
        createStartTime = SystemClock.uptimeMillis()
    }

    override fun onCreateViewHolderEnd(
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
    }

    override fun onBindViewHolderStart(
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
    }

    /**
     * 创建组件的耗时阈值，超过阈值会有警告。
     * 默认 20 毫秒
     *
     * @return 毫秒
     */
    protected fun getCreateCostThreshold(): Long {
        return 20
    }

    /**
     * 绑定组件的耗时阈值，超过阈值会有警告。
     * 默认 5 毫秒
     *
     * @return 毫秒
     */
    protected fun getBindCostThreshold(): Long {
        return 5
    }

}