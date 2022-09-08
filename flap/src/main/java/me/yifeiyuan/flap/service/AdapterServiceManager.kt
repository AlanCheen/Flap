package me.yifeiyuan.flap.service

/**
 *
 * 提供注册和发现 AdapterService 的能力
 *
 * @see me.yifeiyuan.flap.FlapAdapter.registerAdapterService
 * @see me.yifeiyuan.flap.FlapAdapter.getAdapterService
 *
 * Created by 程序亦非猿 on 2022/8/16.
 * @since 3.0.3
 */
internal class AdapterServiceManager : IAdapterServiceManager {

    override val adapterServices: MutableMap<Class<*>, AdapterService> = mutableMapOf()

}