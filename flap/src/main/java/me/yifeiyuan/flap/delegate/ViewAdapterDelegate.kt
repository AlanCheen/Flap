package me.yifeiyuan.flap.delegate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flap.ext.OnItemClickListener
import me.yifeiyuan.flap.ext.OnItemLongClickListener
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


    override fun onBind(model: T, position: Int, payloads: List<Any>, adapter: FlapAdapter, delegate: AdapterDelegate<*, *>) {
        super.onBind(model, position, payloads, adapter, delegate)
    }

    override fun onViewAttachedToWindow(flapAdapter: FlapAdapter) {
        super.onViewAttachedToWindow(flapAdapter)
    }

    override fun onViewDetachedFromWindow(flapAdapter: FlapAdapter) {
        super.onViewDetachedFromWindow(flapAdapter)
    }

    override fun onViewRecycled(flapAdapter: FlapAdapter) {
        super.onViewRecycled(flapAdapter)
    }

    override fun onFailedToRecycleView(flapAdapter: FlapAdapter): Boolean {
        return super.onFailedToRecycleView(flapAdapter)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
    }

    override fun onBind(model: T) {
    }
}


class DelegateConfigBuilder<Model> {

    var modelClass: KClass<*>? = null

    var binder: ((model: Model, component: Component<Model>, position: Int, payloads: List<Any>, adapter: FlapAdapter) -> Unit)? = null

    var onItemClickListener: OnItemClickListener? = null
    var onItemLongClickListener: OnItemLongClickListener? = null
}