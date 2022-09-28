package me.yifeiyuan.flap

import android.content.Context
import me.yifeiyuan.flap.delegate.AdapterDelegate
import me.yifeiyuan.flap.delegate.AdapterDelegateManager
import me.yifeiyuan.flap.delegate.FallbackAdapterDelegate
import me.yifeiyuan.flap.delegate.IAdapterDelegateManager
import me.yifeiyuan.flap.hook.AdapterHookManager
import me.yifeiyuan.flap.hook.IAdapterHookManager
import me.yifeiyuan.flap.service.AdapterServiceManager
import me.yifeiyuan.flap.service.IAdapterServiceManager

/**
 * Flap 存放全局的配置，会应用于所有的 FlapAdapter 实例
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
object Flap : IAdapterHookManager by AdapterHookManager(), IAdapterDelegateManager by AdapterDelegateManager(), IAdapterServiceManager by AdapterServiceManager() {

    /**
     * 是否使用 application context 来创建 Component
     */
    var inflateWithApplicationContext = false

    internal var globalFallbackAdapterDelegate: AdapterDelegate<*, *> = FallbackAdapterDelegate()

    fun withContext(context: Context) = apply {
        context.applicationContext
    }

    /**
     * 设置全局的 FallbackAdapterDelegate
     */
    fun withFallbackAdapterDelegate(fallbackAdapterDelegate: AdapterDelegate<*, *>) = apply {
        globalFallbackAdapterDelegate = fallbackAdapterDelegate
    }

    fun setDebug(debug: Boolean) = apply {
        FlapDebug.isDebug = debug
    }

}