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
import me.yifeiyuan.flap.delegate.AdapterDelegate

/**
 * Component is used by Flap as the base ViewHolder , which provides some useful and convenient abilities as well.
 *
 * Component 基于 ViewHolder， 封装了更多日常开发所需的功能。
 *
 * @see FlapAdapter.getParam 从 Adapter 获取额外的参数
 * @see FlapAdapter.fireEvent 发送事件给 Adapter
 * @see FlapAdapter.getActivityContext 获取 Activity 类型的 Context，因为如果你使用 Application 创建 Component，context 不是 Activity
 * @see FlapAdapter.inflateWithApplicationContext
 *
 * Created by 程序亦非猿 on 2021/9/22.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 2020/9/22
 * @since 3.0.0
 */
abstract class Component<T>(itemView: View) : RecyclerView.ViewHolder(itemView), LifecycleObserver, ComponentConfig {

    /**
     * 默认情况下 context 是 Activity Context ；
     * 如果设置了 FlapAdapter.inflateWithApplicationContext==true ，则会变成 Application Context，
     * 此时如果要获取 Activity Context 则需要通过 FlapAdapter#getActivityContext() 获取
     *
     * @see FlapAdapter.inflateWithApplicationContext
     * @see FlapAdapter.getActivityContext
     */
    protected val context: Context = itemView.context

    /**
     * @return true if component is visible
     */
    protected var isVisible = false

    @Suppress("UNCHECKED_CAST")
    fun bindData(model: Any, position: Int, payloads: List<Any>, adapter: FlapAdapter, delegate: AdapterDelegate<*, *>) {
        onBind(model as T, position, payloads, adapter, delegate)
    }

    /**
     *
     * @param model    The model that you need to bind.
     * @param position position
     * @param adapter  Your adapter.
     * @param payloads The payloads you may need.
     */
    open fun onBind(
            model: T,
            position: Int,
            payloads: List<Any>,
            adapter: FlapAdapter,
            delegate: AdapterDelegate<*, *>
    ) {
        onBind(model)
    }

    /**
     * 执行数据绑定，处理业务逻辑
     *
     * @see onBind
     */
    abstract fun onBind(model: T)

    protected fun <V : View> findViewById(@IdRes viewId: Int): V {
        return itemView.findViewById<View>(viewId) as V
    }

    /**
     * @see FlapAdapter.onViewAttachedToWindow
     */
    @CallSuper
    open fun onViewAttachedToWindow(flapAdapter: FlapAdapter) {
        onVisibilityChanged(true, flapAdapter)
    }

    /**
     * @see FlapAdapter.onViewDetachedFromWindow
     */
    @CallSuper
    open fun onViewDetachedFromWindow(flapAdapter: FlapAdapter) {
        onVisibilityChanged(false, flapAdapter)
    }

    /**
     * @param visible     if component is visible
     * @param flapAdapter
     *
     * @see onViewAttachedToWindow
     * @see onViewDetachedFromWindow
     */
    @CallSuper
    open fun onVisibilityChanged(visible: Boolean, flapAdapter: FlapAdapter) {
        isVisible = visible
    }

    /**
     * @see FlapAdapter.onViewRecycled
     */
    open fun onViewRecycled(flapAdapter: FlapAdapter) {}

    /**
     * @return true if the View should be recycled, false otherwise.
     * @see FlapAdapter.onFailedToRecycleView
     */
    open fun onFailedToRecycleView(flapAdapter: FlapAdapter): Boolean {
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

}