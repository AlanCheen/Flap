package me.yifeiyuan.flap.hook

/**
 * Created by 程序亦非猿 on 2022/9/5.
 *
 * @since 3.1.0
 */
class AdapterHookManager : IAdapterHookManager {

    override val adapterHooks: MutableList<AdapterHook> = mutableListOf()

}