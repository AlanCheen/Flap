package me.yifeiyuan.flap.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.FlapAdapter
import java.lang.Exception
import java.lang.reflect.ParameterizedType

/**
 *
 * A delegate class that delegates some methods of FlapAdapter.
 *
 * Created by 程序亦非猿 on 2021/9/22.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 2020/9/22
 * @since 3.0
 */
interface AdapterDelegate<T, VH : Component<T>> {

    companion object {
        private const val TAG = "AdapterDelegate"
    }

    /**
     * @param model
     * @return 是否代理该 model
     */
    fun delegate(model: Any): Boolean {
        try {
            if (model != null) {
                val type =
                        (this.javaClass.genericInterfaces[0] as ParameterizedType).actualTypeArguments[0]
                return type == model.javaClass
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

//    /**
//     * @param model
//     * @return 是否代理该 model
//     */
//    fun delegate(model: Any?): Boolean

    /**
     *
     * @param inflater LayoutInflater
     * @param parent   RecyclerView
     * @param viewType itemViewType
     * @return your component
     */
    fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): VH

    fun getItemId(model: Any): Long = RecyclerView.NO_ID

    fun getItemViewType(model: Any): Int = 0

    fun onBindViewHolder(
            component: Component<*>,
            data: Any,
            position: Int,
            payloads: List<Any>,
            adapter: FlapAdapter
    ) {
        component.bindData(data, position, payloads, adapter, this)
    }
}