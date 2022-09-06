package me.yifeiyuan.flap.service

/**
 * Created by 程序亦非猿 on 2022/8/18.
 *
 * @since 3.0.3
 */
interface IAdapterServiceManager {

    val adapterServices: MutableMap<Class<*>, AdapterService>

    /**
     * 有名字的 Service
     */
    val namedAdapterServices: MutableMap<String, AdapterService>

    fun <T : AdapterService> registerAdapterService(serviceClass: Class<T>)

    fun <T : AdapterService> registerAdapterService(serviceClass: Class<T>, service: T)

    fun <T : AdapterService> getAdapterService(serviceClass: Class<T>): T?

    fun <T : AdapterService> registerAdapterService(serviceName: String, serviceClass: Class<T>)

    fun <T : AdapterService> registerAdapterService(serviceName: String, service: T)

    fun <T : AdapterService> getAdapterService(serviceName: String): T?
}