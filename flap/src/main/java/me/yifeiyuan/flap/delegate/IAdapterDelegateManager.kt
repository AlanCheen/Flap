package me.yifeiyuan.flap.delegate

import androidx.annotation.NonNull

/**
 * Created by 程序亦非猿 on 2022/9/5.
 *
 * @since 3.1.0
 */
interface IAdapterDelegateManager {

    val adapterDelegates: MutableList<AdapterDelegate<*,*>>

    fun registerAdapterDelegate(@NonNull adapterDelegate: AdapterDelegate<*,*>){
        this.adapterDelegates.add(adapterDelegate)
    }

    fun registerAdapterDelegates(vararg adapterDelegates: AdapterDelegate<*, *>){
        this.adapterDelegates.addAll(adapterDelegates)
    }

    fun unregisterAdapterDelegate(@NonNull adapterDelegate: AdapterDelegate<*,*>){
        this.adapterDelegates.remove(adapterDelegate)
    }

    fun clearAdapterDelegates(){
        adapterDelegates.clear()
    }
}