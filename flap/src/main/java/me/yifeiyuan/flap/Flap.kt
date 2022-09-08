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
object Flap : ComponentCallbacks2, IFlapManager by FlapManager() {

    /**
     * 是否使用 application context 来创建 Component
     *
     * @see FlapAdapter.onCreateViewHolder
     */
    var inflateWithApplicationContext = false

    internal val globalComponentPool = ComponentPool()

    internal var globalDefaultAdapterDelegate: AdapterDelegate<*, *>? = FallbackAdapterDelegate()

    var applicationContext: Context? = null

    override fun onTrimMemory(level: Int) {
        globalComponentPool.onTrimMemory(level)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        globalComponentPool.onConfigurationChanged(newConfig)
    }

    override fun onLowMemory() {
        globalComponentPool.onLowMemory()
    }

    fun init(context: Context) {
        applicationContext = context.applicationContext
        applicationContext?.registerComponentCallbacks(this)
    }

    fun setDebug(debug: Boolean) {
        FlapDebug.isDebug = debug
    }

}