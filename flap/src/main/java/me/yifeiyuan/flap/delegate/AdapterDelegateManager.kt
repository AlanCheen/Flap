package me.yifeiyuan.flap.delegate

/**
 * Created by 程序亦非猿 on 2022/9/5.
 */
class AdapterDelegateManager :IAdapterDelegateManager{
    override val adapterDelegates: MutableList<AdapterDelegate<*, *>> = mutableListOf()
}