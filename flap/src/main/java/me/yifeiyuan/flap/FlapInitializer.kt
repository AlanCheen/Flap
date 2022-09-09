package me.yifeiyuan.flap

import android.content.Context
import me.yifeiyuan.flap.delegate.AdapterDelegate
import me.yifeiyuan.flap.delegate.FallbackAdapterDelegate
import me.yifeiyuan.flap.hook.AdapterHook
import me.yifeiyuan.flap.service.AdapterService

/**
 * Created by 程序亦非猿 on 2022/9/9.
 */

fun initFlap(initBlock: FlapGlobalConfigBuilder.() -> Unit) {
    val globalConfig = FlapGlobalConfigBuilder().apply(initBlock).build()
    Flap.init(globalConfig)
}

/**
 * @param inflateWithApplicationContext 是否使用 application context 来创建 Component
 * @param fallbackAdapterDelegate 全局默认兜底的 AdapterDelegate
 */
class FlapGlobalConfig(
        var context: Context,
        var isDebug: Boolean = false,
        val inflateWithApplicationContext: Boolean = false,
        var fallbackAdapterDelegate: AdapterDelegate<*, *>? = FallbackAdapterDelegate()
)

class FlapGlobalConfigBuilder {

    internal lateinit var context: Context

    internal var isDebug: Boolean = false

    internal var inflateWithApplicationContext: Boolean = false

    internal var globalDefaultAdapterDelegate: AdapterDelegate<*, *> = FallbackAdapterDelegate()

    internal val adapterHooks: MutableList<AdapterHook> = mutableListOf()
    internal val adapterServices: MutableMap<Class<*>, AdapterService> = mutableMapOf()
    internal val adapterDelegates: MutableList<AdapterDelegate<*, *>> = mutableListOf()

    fun inflateWithApplicationContext(enable: Boolean) = apply {
        inflateWithApplicationContext = enable
    }

    fun context(applicationContext: Context) = apply {
        context = applicationContext.applicationContext
    }

    fun debug(debug: Boolean) = apply {
        isDebug = debug
    }

    fun adapterHooks(vararg hooks: AdapterHook) = apply {
        adapterHooks.addAll(hooks)
    }

    fun adapterDelegates(vararg delegates: AdapterDelegate<*, *>) = apply {
        adapterDelegates.addAll(delegates)
    }

    fun <T : AdapterService> adapterService(service: Class<T>) = apply {

    }

    fun fallbackAdapterDelegate(fallbackAdapterDelegate: AdapterDelegate<*, *>) = apply {
        globalDefaultAdapterDelegate = fallbackAdapterDelegate
    }

    fun build(): FlapGlobalConfig {
        if (context == null) {
            throw  NullPointerException("context can't be null")
        }
        return FlapGlobalConfig(context, isDebug, inflateWithApplicationContext, globalDefaultAdapterDelegate)
    }
}