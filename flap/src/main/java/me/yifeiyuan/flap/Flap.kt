package me.yifeiyuan.flap

import android.content.ComponentCallbacks2
import android.content.Context
import android.content.res.Configuration
import me.yifeiyuan.flap.delegate.AdapterDelegate
import me.yifeiyuan.flap.delegate.AdapterDelegateManager
import me.yifeiyuan.flap.delegate.FallbackAdapterDelegate
import me.yifeiyuan.flap.delegate.IAdapterDelegateManager
import me.yifeiyuan.flap.hook.AdapterHookManager
import me.yifeiyuan.flap.hook.IAdapterHookManager
import me.yifeiyuan.flap.pool.ComponentPool
import me.yifeiyuan.flap.service.AdapterServiceManager
import me.yifeiyuan.flap.service.IAdapterServiceManager

/**
 * Flap 存放全局的配置
 *
 * Created by 程序亦非猿 on 2021/9/22.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 2020/9/22
 * @since 3.0.0
 */
object Flap : ComponentCallbacks2, IAdapterHookManager by AdapterHookManager(), IAdapterDelegateManager by AdapterDelegateManager(), IAdapterServiceManager by AdapterServiceManager() {

    /**
     * 是否使用 application context 来创建 Component
     *
     * @see FlapAdapter.onCreateViewHolder
     */
    var inflateWithApplicationContext = false

    internal val globalComponentPool = ComponentPool()

    internal var globalDefaultAdapterDelegate: AdapterDelegate<*, *>? = FallbackAdapterDelegate()

    override fun onTrimMemory(level: Int) {
        globalComponentPool.onTrimMemory(level)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        globalComponentPool.onConfigurationChanged(newConfig)
    }

    override fun onLowMemory() {
        globalComponentPool.onLowMemory()
    }

    fun withContext(context: Context) = apply {
        context.applicationContext.registerComponentCallbacks(this)
    }

    fun withFallbackAdapterDelegate(fallbackAdapterDelegate: AdapterDelegate<*, *>) = apply {
        globalDefaultAdapterDelegate = fallbackAdapterDelegate
    }

    fun setDebug(debug: Boolean) = apply {
        FlapDebug.isDebug = debug
    }

}