package me.yifeiyuan.flap

import androidx.annotation.NonNull
import me.yifeiyuan.flap.delegate.AdapterDelegate
import me.yifeiyuan.flap.hook.AdapterHook

/**
 * Created by 程序亦非猿 on 2022/3/8.
 *
 * @since 3.0.0
 */
internal interface IRegistry {

    fun registerAdapterHook(@NonNull adapterHook: AdapterHook)

    fun registerAdapterHooks(vararg adapterHooks: AdapterHook)

    fun unRegisterAdapterHook(@NonNull adapterHook: AdapterHook)

    fun clearAdapterHooks()

    fun registerAdapterDelegate(@NonNull adapterDelegate: AdapterDelegate<*,*>)

    fun registerAdapterDelegates(vararg adapterDelegates: AdapterDelegate<*, *>)

    fun unRegisterAdapterDelegate(@NonNull adapterDelegate: AdapterDelegate<*,*>)

    fun clearAdapterDelegates()
}