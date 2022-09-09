package me.yifeiyuan.flap.hook

import androidx.annotation.NonNull

/**
 * AdapterHook 管理者的抽象
 * @see AdapterHookManager
 *
 * Created by 程序亦非猿 on 2022/9/5.
 * @since 3.1.0
 */
interface IAdapterHookManager {

    val adapterHooks: MutableList<AdapterHook>

    fun registerAdapterHook(adapterHook: AdapterHook) {
        adapterHooks.add(adapterHook)
    }

    fun registerAdapterHooks(vararg adapterHooks: AdapterHook) {
        this.adapterHooks.addAll(adapterHooks)
    }

    fun unregisterAdapterHook(adapterHook: AdapterHook) {
        adapterHooks.remove(adapterHook)
    }

    fun clearAdapterHooks() {
        adapterHooks.clear()
    }
}