package me.yifeiyuan.flap.ext

/**
 * Created by 程序亦非猿 on 2021/11/1.
 *
 * @since 3.0.0
 */
interface ExtraParamsProvider {
    fun getParam(key: String): Any?
}

/**
 * @since 3.0.4
 */
internal class ExtraParamsProviderWrapper(private val block: (key: String) -> Any?) : ExtraParamsProvider {
    override fun getParam(key: String): Any? {
        return block(key)
    }
}
