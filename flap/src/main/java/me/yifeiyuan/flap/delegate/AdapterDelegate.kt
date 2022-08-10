package me.yifeiyuan.flap.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.FlapAdapter
import java.lang.reflect.ParameterizedType

/**
 *
 * A delegate class that delegates some methods of FlapAdapter.
 *
 * AdapterDelegate 是 FlapAdapter 的代理类，负责代理 Adapter 的部分方法。
 * 当一个 AdapterDelegate.delegate(model) 返回 true 表示它负责接手这个数据模型。
 * 一般一个 AdapterDelegate 代理一个类型的 Model，是一对一的关系，这样更加解耦。
 * 这样可以解耦 Adapter 对不同类型的 Model 的处理。
 *
 * 如果一个 model 没有任何 AdapterDelegate 代理，那么 FallbackAdapterDelegate 会兜底处理
 * @see FallbackAdapterDelegate
 *
 *
 * Created by 程序亦非猿 on 2021/9/22.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 2020/9/22
 * @since 3.0.0
 */
interface AdapterDelegate<M, VH : Component<M>> {

    /**
     * 该方法确定一个 AdapterDelegate 是否接受代理一个 Model，如果代理则 return true
     *
     * 如果你想为 YourModel 做代理，则可以这么写 ：
     * Kotlin:
     * return model.javaClass == YourModel::class.java
     * Java:
     * return model.getClass() == YourModel.class;
     *
     * @param model 数据
     * @return true 表示代理该 model
     */
    fun delegate(model: Any): Boolean {
        try {
            val type = (this.javaClass.genericInterfaces[0] as ParameterizedType).actualTypeArguments[0]
            return type == model.javaClass
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 根据代理的 Model 创建与之对应的 Component
     *
     * @param inflater LayoutInflater
     * @param parent   RecyclerView
     * @param viewType itemViewType
     * @return your component
     *
     * @see RecyclerView.Adapter.onCreateViewHolder
     */
    fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): VH

    /**
     * @see RecyclerView.Adapter.getItemId
     */
    fun getItemId(model: Any): Long = RecyclerView.NO_ID

    /**
     * @see RecyclerView.Adapter.getItemViewType
     */
    fun getItemViewType(model: Any): Int = 0

    /**
     * @see RecyclerView.Adapter.onBindViewHolder
     */
    fun onBindViewHolder(
            component: Component<*>,
            data: Any,
            position: Int,
            payloads: List<Any>,
            adapter: FlapAdapter
    ) {
        component.bindData(data, position, payloads, adapter, this)
    }

    /**
     * @see FlapAdapter.onViewAttachedToWindow
     */
    fun onViewAttachedToWindow(adapter: FlapAdapter, component: Component<*>) {
        component.onViewAttachedToWindow(adapter)
    }

    /**
     * @see FlapAdapter.onViewDetachedFromWindow
     */
    fun onViewDetachedFromWindow(adapter: FlapAdapter, component: Component<*>) {
        component.onViewDetachedFromWindow(adapter)
    }

    /**
     * @see FlapAdapter.onFailedToRecycleView
     */
    fun onFailedToRecycleView(adapter: FlapAdapter, component: Component<*>): Boolean {
        return component.onFailedToRecycleView(adapter)
    }

    /**
     * @see FlapAdapter.onViewRecycled
     */
    fun onViewRecycled(adapter: FlapAdapter, component: Component<*>) {
        component.onViewRecycled(adapter)
    }
}