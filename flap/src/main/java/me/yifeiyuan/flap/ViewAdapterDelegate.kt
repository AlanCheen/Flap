package me.yifeiyuan.flap

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes

/**
 * Created by 程序亦非猿 on 2021/10/27.
 *
 * todo
 */
interface ViewAdapterDelegate<T> : AdapterDelegate<T, ViewComponent<T>> {

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): ViewComponent<T> {
        return ViewComponent(inflater,parent,viewType)
    }

    override fun getItemViewType(model: Any): Int {
        return getLayoutResId(model as T)
    }

    @LayoutRes
    fun getLayoutResId(model: T): Int

}

class ViewComponent<T>(inflater: LayoutInflater, parent: ViewGroup, layoutId: Int):Component<T>(inflater.inflate(layoutId, parent, false)){
    override fun onBind(model: T) {
    }
}