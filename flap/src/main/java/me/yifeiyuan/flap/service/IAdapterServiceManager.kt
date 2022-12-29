package me.yifeiyuan.flap.service

import androidx.annotation.RestrictTo

/**
 * IAdapterServiceManager is for managing all the AdapterServices.
 *
 * You can register an AdapterService  in Application or Activity or Fragment.
 *
 * And get the AdapterService when binding Component by calling {getAdapterService} or {Component#callService}
 *
 * @see me.yifeiyuan.flap.FlapRegistry
 * @see AdapterServiceManager
 *
 * AdapterService 管理，可以注册或获取 AdapterService
 *
 * 可以在 Activity 中注册 AdapterService，在 Component 中使用。
 *
 * Created by 程序亦非猿 on 2022/8/18.
 * @since 3.0.3
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
interface IAdapterServiceManager {

    val adapterServices: MutableMap<Class<*>, AdapterService>

    /**
     * 注册 AdapterService，并反射调用无参构造器进行实例化
     */
    fun <T : AdapterService> registerAdapterService(serviceClass: Class<T>) {
        try {
            val service = serviceClass.getConstructor().newInstance()
            adapterServices[serviceClass] = service
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 注册多个 AdapterService，并反射调用无参构造器进行实例化
     */
    fun <T : AdapterService> registerAdapterServices(vararg serviceClasses: Class<T>) {
        try {
            serviceClasses.forEach {
                val service = it.getConstructor().newInstance()
                adapterServices[it] = service
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     *
     * 注册 AdapterService 实例
     */
    fun <T : AdapterService> registerAdapterService(serviceClass: Class<T>, service: T) {
        adapterServices[serviceClass] = service
    }

    /**
     * Return an AdapterService instance of the Given service class.
     * If no AdapterService is found it returns null.
     *
     * 获取 AdapterService
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : AdapterService> getAdapterService(serviceClass: Class<T>): T? {
        return adapterServices[serviceClass] as? T
    }

}