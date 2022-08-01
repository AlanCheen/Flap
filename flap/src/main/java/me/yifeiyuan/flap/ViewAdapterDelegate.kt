package me.yifeiyuan.flap

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import me.yifeiyuan.flap.delegate.AdapterDelegate
import kotlin.reflect.KClass

/**
 * Created by 程序亦非猿 on 2021/10/27.
 *
 * todo
 */
class ViewAdapterDelegate<T>(var layoutId: Int, var binder: (model: T, component: Component<T>, position: Int, payloads: List<Any>, adapter: FlapAdapter) -> Unit) : AdapterDelegate<T, ViewComponent<T>> {

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): ViewComponent<T> {
        val view = inflater.inflate(layoutId, parent, false)
        return ViewComponent(view)
    }

    override fun onBindViewHolder(component: Component<*>, data: Any, position: Int, payloads: List<Any>, adapter: FlapAdapter) {
        super.onBindViewHolder(component, data, position, payloads, adapter)
        binder.invoke(data as T, component as Component<T>, position, payloads, adapter)
    }

    override fun getItemViewType(model: Any): Int {
        return layoutId
    }
}

class ViewComponent<T>(view: View) : Component<T>(view) {

    override fun onBind(model: T) {
//        binder.invoke(itemView, model)
    }
}


class DelegateConfigBuilder<T> {

    var modelClass: KClass<*>? = null

    var binder: ((model: T, component: Component<T>, position: Int, payloads: List<Any>, adapter: FlapAdapter) -> Unit)? = null

}