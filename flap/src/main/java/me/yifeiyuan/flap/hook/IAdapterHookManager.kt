package me.yifeiyuan.flap.hook

/**
 * AdapterHook 管理者的抽象
 * @see AdapterHookManager
 *
 * Created by 程序亦非猿 on 2022/9/5.
 * @since 3.1.0
 */
internal interface IAdapterHookManager {

    val adapterHooks: MutableList<AdapterHook>

    fun registerAdapterHook(adapterHook: AdapterHook) {
        adapterHooks.add(adapterHook)
    }

    fun registerAdapterHook(index: Int, adapterHook: AdapterHook) {
        adapterHooks.add(index, adapterHook)
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