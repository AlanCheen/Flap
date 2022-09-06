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
@Suppress("UNCHECKED_CAST")
internal class AdapterServiceManager : IAdapterServiceManager {

    override val adapterServices: MutableMap<Class<*>, AdapterService> = mutableMapOf()
    override val namedAdapterServices: MutableMap<String, AdapterService> = mutableMapOf()

    override fun <T : AdapterService> registerAdapterService(serviceClass: Class<T>) {
        try {
            val service = serviceClass.getConstructor().newInstance()
            adapterServices[serviceClass] = service
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun <T : AdapterService> registerAdapterService(serviceClass: Class<T>, service: T) {
        adapterServices[serviceClass] = service
    }

    override fun <T : AdapterService> getAdapterService(serviceClass: Class<T>): T? {
        return adapterServices[serviceClass] as? T
    }

    override fun <T : AdapterService> registerAdapterService(serviceName: String, serviceClass: Class<T>) {
        try {
            val service = serviceClass.getConstructor().newInstance()
            namedAdapterServices[serviceName] = service
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun <T : AdapterService> registerAdapterService(serviceName: String, service: T) {
        namedAdapterServices[serviceName] = service
    }

    override fun <T : AdapterService> getAdapterService(serviceName: String): T? {
        return namedAdapterServices[serviceName] as? T
    }
}