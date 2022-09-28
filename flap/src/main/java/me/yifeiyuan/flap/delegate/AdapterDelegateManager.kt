package me.yifeiyuan.flap.delegate

/**
 * Created by 程序亦非猿 on 2022/9/5.
 *
 * @since 3.1.0
 */
internal class AdapterDelegateManager : IAdapterDelegateManager {
    override val adapterDelegates: MutableList<AdapterDelegate<*, *>> = mutableListOf()
}