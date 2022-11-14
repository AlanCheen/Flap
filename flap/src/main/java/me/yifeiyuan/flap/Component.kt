@file:Suppress("MemberVisibilityCanBePrivate")

package me.yifeiyuan.flap

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flap.event.Event
import me.yifeiyuan.flap.service.AdapterService

/**
 * Component is used by Flap as the base ViewHolder , which provides some useful and convenient abilities as well.
 *
 * Component 基于 ViewHolder， 封装了更多日常开发所需的功能。
 *
 * @see Flap.getParam 从 Adapter 获取额外的参数
 * @see Flap.fireEvent 发送事件给 Adapter
 * @see Flap.getActivityContext 获取 Activity 类型的 Context，因为如果你使用 Application 创建 Component，context 不是 Activity
 * @see Flap.inflateWithApplicationContext
 *
 * Created by 程序亦非猿 on 2021/9/22.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 2020/9/22
 * @since 3.0.0
 */
open class Component<T>(itemView: View) : RecyclerView.ViewHolder(itemView), LifecycleObserver, ComponentConfig {

    /**
     * 默认情况下 context 是 Activity Context ；
     * 如果设置了 FlapAdapter.inflateWithApplicationContext==true ，则会变成 Application Context，
     * 此时如果要获取 Activity Context 则需要通过 FlapAdapter#getActivityContext() 获取
     *
     * @see Flap.inflateWithApplicationContext
     * @see Flap.getActivityContext
     */
    val context: Context = itemView.context

    /**
     * @return true if component is visible
     */
    protected var isVisible = false

    internal var _bindingAdapter: RecyclerView.Adapter<*>? = null

    val adapter: RecyclerView.Adapter<*>
        get() = if (_bindingAdapter == null) {
            throw java.lang.IllegalArgumentException("onBind 还未调用，还不可以使用")
        } else {
            _bindingAdapter as RecyclerView.Adapter<*>
        }

    internal var _flap: Flap? = null

    val flap: Flap
        get() = if (_flap == null) {
            throw java.lang.IllegalArgumentException("onBind 还未调用，还不可以使用")
        } else {
            _flap as Flap
        }

    internal var _bindingData: Any? = null

    /**
     * @since 3.1.2
     */
    val data: T
        get() = if (_bindingData == null) {
            throw IllegalArgumentException("onBind 还未调用，还不可以使用")
        } else {
            @Suppress("UNCHECKED_CAST")
            _bindingData as T
        }

    @Suppress("UNCHECKED_CAST")
    internal fun bindData(model: Any, position: Int, payloads: List<Any>, adapter: RecyclerView.Adapter<*>, flap: Flap) {
        _bindingData = model
        _flap = flap
        _bindingAdapter = adapter
        onBind(model as T, position, payloads)
    }

    /**
     * 执行数据绑定，处理业务逻辑
     *
     * @param model    The model that you need to bind.
     * @param position position
     * @param adapter  Your adapter.
     * @param payloads The payloads you may need.
     */
    protected open fun onBind(
            model: T,
            position: Int,
            payloads: List<Any>) {
        onBind(model)
    }

    /**
     * 执行数据绑定，处理业务逻辑
     *
     * @see onBind
     */
    protected open fun onBind(model: T) {}

    @Suppress("UNCHECKED_CAST")
    fun <V : View> findViewById(@IdRes viewId: Int): V {
        return itemView.findViewById<View>(viewId) as V
    }

    /**
     * @see RecyclerView.Adapter.onViewAttachedToWindow
     */
    @CallSuper
    open fun onViewAttachedToWindow(adapter: RecyclerView.Adapter<*>) {
        onVisibilityChanged(true, adapter)
    }

    /**
     * @see RecyclerView.Adapter.onViewDetachedFromWindow
     */
    @CallSuper
    open fun onViewDetachedFromWindow(adapter: RecyclerView.Adapter<*>) {
        onVisibilityChanged(false, adapter)
    }

    /**
     * @param visible     if component is visible
     * @param adapter
     *
     * @see onViewAttachedToWindow
     * @see onViewDetachedFromWindow
     */
    @CallSuper
    open fun onVisibilityChanged(visible: Boolean, adapter: RecyclerView.Adapter<*>) {
        isVisible = visible
    }

    /**
     * @see RecyclerView.Adapter.onViewRecycled
     */
    open fun onViewRecycled(adapter: RecyclerView.Adapter<*>) {}

    /**
     * @return true if the View should be recycled, false otherwise.
     * @see RecyclerView.Adapter.onFailedToRecycleView
     */
    open fun onFailedToRecycleView(adapter: RecyclerView.Adapter<*>): Boolean {
        return false
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    open fun onResume(owner: LifecycleOwner) {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    open fun onPause(owner: LifecycleOwner) {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    open fun onStop(owner: LifecycleOwner) {
    }

    @CallSuper
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onDestroy(owner: LifecycleOwner) {
        owner.lifecycle.removeObserver(this)
        _bindingAdapter = null
        _flap = null
        _bindingData = null
    }

    fun getString(@StringRes resId: Int): String {
        return context.getString(resId)
    }

    fun getString(@StringRes resId: Int, vararg formatArgs: Any): String {
        return context.getString(resId, *formatArgs)
    }

    fun getDrawable(@DrawableRes id: Int): Drawable? {
        return ContextCompat.getDrawable(context, id)
    }

    @ColorInt
    fun getColor(@ColorRes id: Int): Int {
        return ContextCompat.getColor(context, id)
    }

    fun getColorStateList(@ColorRes id: Int): ColorStateList? {
        return ContextCompat.getColorStateList(context, id)
    }

    fun updateLayoutParams(block: RecyclerView.LayoutParams.() -> Unit) {
        //第一次 bind 的时候 还没有 attach 到 RV，没有 LP,默认设置一个先
        val currentLayoutParams: RecyclerView.LayoutParams = (if (itemView.layoutParams == null) (RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)) else itemView.layoutParams) as RecyclerView.LayoutParams
        block.invoke(currentLayoutParams)
        itemView.layoutParams = currentLayoutParams
    }

    fun hide() {
        updateComponentVisibility(false)
    }

    fun show() {
        updateComponentVisibility(true)
    }

    /**
     *
     *  Note: 直接设置 itemView 的可见性会有高度 Bug
     */
    private fun updateComponentVisibility(isVisible: Boolean) {
        val param: RecyclerView.LayoutParams = (if (itemView.layoutParams == null) (RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)) else itemView.layoutParams) as RecyclerView.LayoutParams

        if (isVisible) {
            if (itemView.getTag(R.id.flap_component_height) is Int) {
                param.height = (itemView.getTag(R.id.flap_component_height) as Int)
            } else if (param.height <= 0) {
                param.height = RecyclerView.LayoutParams.WRAP_CONTENT
            }
            if (param.width <= 0) {
                param.width = RecyclerView.LayoutParams.MATCH_PARENT
            }
            itemView.visibility = View.VISIBLE
        } else {
            if (itemView.height > 0) {
                itemView.setTag(R.id.flap_component_height, itemView.height)
            } else if (param.height > 0) {
                itemView.setTag(R.id.flap_component_height, param.height)
            }
            param.height = 0
            param.width = 0
            itemView.visibility = View.GONE
        }
        itemView.layoutParams = param
    }

    inline fun <reified T : AdapterService> callService(noinline block: T.() -> Unit) {
        flap.getAdapterService(T::class.java)?.let {
            it.apply { block() }
        }
    }

    fun <P> getParam(key: String): P? {
        return flap.getParam(key) as? P
    }

    fun <T> fireEvent(event: Event<T>) {
        flap.fireEvent(event)
    }


}