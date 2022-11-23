package me.yifeiyuan.flap

import android.content.Context
import me.yifeiyuan.flap.delegate.*
import me.yifeiyuan.flap.delegate.AdapterDelegateManager
import me.yifeiyuan.flap.delegate.DefaultFallbackAdapterDelegate
import me.yifeiyuan.flap.delegate.IAdapterDelegateManager
import me.yifeiyuan.flap.hook.AdapterHook
import me.yifeiyuan.flap.hook.AdapterHookManager
import me.yifeiyuan.flap.hook.IAdapterHookManager
import me.yifeiyuan.flap.service.AdapterService
import me.yifeiyuan.flap.service.AdapterServiceManager
import me.yifeiyuan.flap.service.IAdapterServiceManager

/**
 * FlapInitializer 是 Flap 的初始化器，存放全局的配置，会应用于所有的 FlapAdapter 实例。
 *
 * - AdapterDelegate
 * - AdapterHook
 * - AdapterService
 *
 * Created by 程序亦非猿 on 2021/9/22.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 2020/9/22
 * @since 3.0.0
 */
object FlapInitializer : Registry {

    override val adapterDelegates: MutableList<AdapterDelegate<*, *>> = mutableListOf()
    override val adapterHooks: MutableList<AdapterHook> = mutableListOf()
    override val adapterServices: MutableMap<Class<*>, AdapterService> = mutableMapOf()

    /**
     * 是否使用 application context 来创建 Component
     */
    var inflateWithApplicationContext = false

    internal var globalFallbackAdapterDelegate: FallbackAdapterDelegate = DefaultFallbackAdapterDelegate()

    fun withContext(context: Context) = apply {
        context.applicationContext
    }

    /**
     * 设置全局的 FallbackAdapterDelegate
     */
    fun withFallbackAdapterDelegate(fallbackAdapterDelegate: FallbackAdapterDelegate) = apply {
        globalFallbackAdapterDelegate = fallbackAdapterDelegate
    }

    fun setDebug(debug: Boolean) = apply {
        FlapDebug.isDebug = debug
    }

}