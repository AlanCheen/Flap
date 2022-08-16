package me.yifeiyuan.flap.pool

import android.content.ComponentCallbacks2
import android.content.res.Configuration
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import me.yifeiyuan.flap.FlapDebug

private const val TAG = "ComponentPool"

/**
 * 自定义 RecycledViewPool
 *
 * 一个实现了 ComponentCallbacks2 的 ComponentPool，可以在内存不足的时候清理缓存
 * 要使用的话，需要先注册到 Application
 *
 * @see me.yifeiyuan.flap.Flap.init
 * @see me.yifeiyuan.flap.FlapAdapter.useGlobalComponentPool
 *
 * Created by 程序亦非猿 on 2021/9/22.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 2020/9/22
 * @since 3.0.1
 */
open class ComponentPool : RecycledViewPool(), ComponentCallbacks2 {

    override fun setMaxRecycledViews(viewType: Int, max: Int) {
        super.setMaxRecycledViews(viewType, max)
        FlapDebug.d(TAG, "setMaxRecycledViews() called with: viewType = $viewType, max = $max")
    }

    override fun getRecycledViewCount(viewType: Int): Int {
        val result = super.getRecycledViewCount(viewType)
        FlapDebug.d(TAG, "getRecycledViewCount() called with: viewType = $viewType,result=$result")
        return result
    }

    override fun getRecycledView(viewType: Int): RecyclerView.ViewHolder? {
        val result = super.getRecycledView(viewType)
        FlapDebug.d(TAG, "getRecycledView() called with: viewType = $viewType,result=$result")
        return result
    }

    override fun putRecycledView(scrap: RecyclerView.ViewHolder) {
        super.putRecycledView(scrap)
        FlapDebug.d(TAG, "putRecycledView() called with: scrap = $scrap")
    }

    //参考 Glide 的 MemoryCache 实现
    override fun onTrimMemory(level: Int) {
        when (level) {
            in ComponentCallbacks2.TRIM_MEMORY_BACKGROUND..ComponentCallbacks2.TRIM_MEMORY_COMPLETE -> {
                clear()
            }
            else -> {
            }
        }
        FlapDebug.d(TAG, "onTrimMemory() called with: level = $level")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {}

    override fun onLowMemory() {
        FlapDebug.d(TAG, "onLowMemory: ")
        clear()
    }

    override fun clear() {
        super.clear()
        FlapDebug.d("ComponentPool", "ComponentPool 执行清理缓存")
    }
}