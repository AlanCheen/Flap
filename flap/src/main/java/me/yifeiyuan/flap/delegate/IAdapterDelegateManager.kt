package me.yifeiyuan.flap.delegate

/**
 * Manage all the AdapterDelegates.
 *
 * AdapterDelegate 管理者的抽象
 *
 * @see AdapterDelegateManager
 *
 * Created by 程序亦非猿 on 2022/9/5.
 * @since 3.1.0
 */
interface IAdapterDelegateManager {

    val adapterDelegates: MutableList<AdapterDelegate<*, *>>

    /**
     * 注册单个 AdapterDelegate
     */
    fun registerAdapterDelegate(adapterDelegate: AdapterDelegate<*, *>) {
        this.adapterDelegates.add(adapterDelegate)
    }

    /**
     * 注册单个 AdapterDelegate，并指定 index
     */
    fun registerAdapterDelegate(index: Int, adapterDelegate: AdapterDelegate<*, *>) {
        this.adapterDelegates.add(index, adapterDelegate)
    }

    /**
     * 注册多个 AdapterDelegate
     */
    fun registerAdapterDelegates(vararg adapterDelegates: AdapterDelegate<*, *>) {
        this.adapterDelegates.addAll(adapterDelegates)
    }

    /**
     * 注册多个 AdapterDelegate
     */
    fun registerAdapterDelegates(index: Int,vararg adapterDelegates: AdapterDelegate<*, *>) {
        this.adapterDelegates.addAll(index, adapterDelegates.toList())
    }

    /**
     * 注销单个 AdapterDelegate
     */
    fun unregisterAdapterDelegate(adapterDelegate: AdapterDelegate<*, *>) {
        this.adapterDelegates.remove(adapterDelegate)
    }

    /**
     * 注销所有 AdapterDelegate
     */
    fun clearAdapterDelegates() {
        adapterDelegates.clear()
    }
}