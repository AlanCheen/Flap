package me.yifeiyuan.flap.ext

import android.content.ComponentCallbacks2
import android.content.res.Configuration
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import me.yifeiyuan.flap.FlapDebug

private const val TAG = "FlapComponentPool"

/**
 * A global RecycledViewPool that can be shared among RecyclerViews , which is enabled by default.
 *
 * todo 考虑下 是否需要实现 ComponentCallbacks2
 *
 *
 * @see me.yifeiyuan.flap.FlapAdapter.useComponentPool
 *
 * Created by 程序亦非猿 on 2021/9/22.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 2020/9/22
 * @since 3.0.0
 */
open class FlapComponentPool : RecycledViewPool(), ComponentCallbacks2 {

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

    override fun onTrimMemory(level: Int) {
        when (level) {
            ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL,
            ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW,
            ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE -> clear()
            ComponentCallbacks2.TRIM_MEMORY_BACKGROUND,
            ComponentCallbacks2.TRIM_MEMORY_COMPLETE,
            ComponentCallbacks2.TRIM_MEMORY_MODERATE,
            ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN -> {
            }
            else -> {
            }
        }
        FlapDebug.d(TAG, "onTrimMemory() called with: level = $level")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {}

    override fun onLowMemory() {
        clear()
    }

    override fun clear() {
        super.clear()
        FlapDebug.d("ComponentPool", "ComponentPool 执行清理缓存")
    }
}