package me.yifeiyuan.flap.service

/**
 *
 * 提供注册和发现 FlapService 的能力
 *
 * @see me.yifeiyuan.flap.FlapAdapter.registerService
 * @see me.yifeiyuan.flap.FlapAdapter.getService
 *
 * Created by 程序亦非猿 on 2022/8/16.
 * @since 3.0.3
 */
internal class ServiceManager {

    private val services = mutableMapOf<Class<*>, FlapService>()

    /**
     * 有名字的 Service
     */
    private val namedServices = mutableMapOf<String, FlapService>()

    fun <T : FlapService> registerService(clazz: Class<T>) {
        try {
            val service = clazz.getConstructor().newInstance()
            services[clazz] = service
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun <T : FlapService> registerService(serviceClass: Class<T>, service: T) {
        services[serviceClass] = service
    }

    fun <T : FlapService> getService(serviceClass: Class<T>): T? {
        return services[serviceClass] as? T
    }

    fun <T : FlapService> registerService(serviceName: String, clazz: Class<T>) {
        try {
            val service = clazz.getConstructor().newInstance()
            namedServices[serviceName] = service
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun <T : FlapService> registerService(serviceName: String, service: T) {
        namedServices[serviceName] = service
    }

    fun <T : FlapService> getService(serviceName: String): T? {
        return namedServices[serviceName] as? T
    }
}