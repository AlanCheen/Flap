package me.yifeiyuan.flap.hook

import androidx.annotation.RestrictTo

/**
 * Manage all the AdapterHooks
 *
 * AdapterHook 管理者的抽象
 * @see me.yifeiyuan.flap.FlapRegistry
 * @see AdapterHookManager
 *
 * Created by 程序亦非猿 on 2022/9/5.
 * @since 3.1.0
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
interface IAdapterHookManager {

    val adapterHooks: MutableList<AdapterHook>

    fun registerAdapterHook(adapterHook: AdapterHook) {
        if (!adapterHooks.contains(adapterHook)) {
            adapterHooks.add(adapterHook)
        }
    }

    fun registerAdapterHook(index: Int, adapterHook: AdapterHook) {
        if (!adapterHooks.contains(adapterHook)) {
            adapterHooks.add(index, adapterHook)
        }
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