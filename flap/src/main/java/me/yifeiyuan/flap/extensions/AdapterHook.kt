package me.yifeiyuan.flap.extensions

import me.yifeiyuan.flap.AdapterDelegate
import me.yifeiyuan.flap.Component

/**
 * Created by 程序亦非猿 on 2021/9/22.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 2020/9/22
 * @since 3.0
 */
interface AdapterHook {

    /**
     * 在创建组件前调用
     *
     * @param delegate 组件代理
     */
    fun onCreateViewHolderStart(delegate: AdapterDelegate<*, *>?, viewType: Int)

    /**
     * 在组件创建完毕后调用
     *
     * @param delegate
     * @param component
     */
    fun onCreateViewHolderEnd(
            delegate: AdapterDelegate<*, *>?,
            viewType: Int,
            component: Component<*>
    )

    /**
     * 在绑定组件之前调用
     *
     * @param component
     * @param position
     * @param data
     */
    fun onBindViewHolderStart(
            delegate: AdapterDelegate<*, *>,
            component: Component<*>,
            data: Any,
            position: Int,
            payloads: MutableList<Any>
    )

    /**
     * 在组件绑定完毕后回调
     *
     * @param component
     * @param position
     * @param data
     */
    fun onBindViewHolderEnd(
            delegate: AdapterDelegate<*, *>,
            component: Component<*>,
            data: Any,
            position: Int,
            payloads: MutableList<Any>
    )
}