package me.yifeiyuan.flap

import android.content.ComponentCallbacks2
import android.content.res.Configuration
import me.yifeiyuan.flap.extensions.AdapterHook
import me.yifeiyuan.flap.extensions.AdapterDelegateApm
import me.yifeiyuan.flap.extensions.ComponentPool

/**
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 *
 * Created by 程序亦非猿 on 2021/9/22.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 2020/9/22
 * @since 3.0
 */
object Flap : ComponentCallbacks2 {

    internal val globalComponentPool = ComponentPool()

    internal val adapterDelegates: MutableList<AdapterDelegate<*, *>> = mutableListOf()

    internal var defaultAdapterDelegate: AdapterDelegate<*, *>? = DefaultAdapterDelegate()

    internal val hooks: MutableList<AdapterHook> = mutableListOf<AdapterHook>().apply {
        add(AdapterDelegateApm())
    }

    @JvmStatic
    fun registerAdapterHook(adapterHook: AdapterHook) {
        hooks.add(adapterHook)
    }

    @JvmStatic
    fun unRegisterAdapterHook(adapterHook: AdapterHook) {
        hooks.remove(adapterHook)
    }

    @JvmStatic
    fun registerAdapterDelegate(adapterDelegate: AdapterDelegate<*, *>) {
        adapterDelegates.add(adapterDelegate)
    }

    @JvmStatic
    fun unRegisterAdapterDelegate(adapterDelegate: AdapterDelegate<*, *>) {
        adapterDelegates.remove(adapterDelegate)
    }

    override fun onTrimMemory(level: Int) {
        globalComponentPool.onTrimMemory(level)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        globalComponentPool.onConfigurationChanged(newConfig)
    }

    override fun onLowMemory() {
        globalComponentPool.onLowMemory()
    }

}