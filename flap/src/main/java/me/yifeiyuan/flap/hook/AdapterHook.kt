package me.yifeiyuan.flap.hook

import me.yifeiyuan.flap.delegate.AdapterDelegate
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.FlapAdapter

/**
 *
 * AdapterHook 是在 Adapter 留的 hooks ,方便以解耦的方式做一些类似 AOP 的功能。
 *
 * @see ApmHook 一个简易的 APM 工具
 * @see PrefetchDetectorHook  实现预取检测功能
 *
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
     * @param adapter 正在创建组件的 adapter
     * @param delegate 创建组件的代理
     * @param viewType
     */
    fun onCreateViewHolderStart(
            adapter: FlapAdapter,
            delegate: AdapterDelegate<*, *>?,
            viewType: Int
    ) {
    }

    /**
     * 在组件创建完毕后调用
     *
     * @param adapter  正在创建组件的 adapter
     * @param delegate 创建组件的代理
     * @param viewType
     * @param component 被创建出来的组件
     */
    fun onCreateViewHolderEnd(
            adapter: FlapAdapter,
            delegate: AdapterDelegate<*, *>?,
            viewType: Int,
            component: Component<*>
    ) {
    }

    /**
     * 在绑定组件之前调用
     *
     * @param adapter
     * @param delegate 绑定组件的代理
     * @param component
     * @param position
     * @param data
     * @param payloads
     */
    fun onBindViewHolderStart(
            adapter: FlapAdapter,
            delegate: AdapterDelegate<*, *>,
            component: Component<*>,
            data: Any,
            position: Int,
            payloads: MutableList<Any>
    ) {
    }

    /**
     * 在组件绑定完毕后回调
     *
     * @param adapter
     * @param delegate 绑定组件的代理
     * @param component
     * @param position
     * @param data
     * @param payloads
     */
    fun onBindViewHolderEnd(
            adapter: FlapAdapter,
            delegate: AdapterDelegate<*, *>,
            component: Component<*>,
            data: Any,
            position: Int,
            payloads: MutableList<Any>
    ) {
    }
}

fun <T : AdapterHook> T.attachTo(adapter: FlapAdapter): T {
    adapter.registerAdapterHook(this)
    return this
}