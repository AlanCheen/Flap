@file:Suppress("MemberVisibilityCanBePrivate", "unused", "LeakingThis")

package me.yifeiyuan.flap

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flap.delegate.IAdapterDelegateManager
import me.yifeiyuan.flap.event.Event
import me.yifeiyuan.flap.event.EventObserver
import me.yifeiyuan.flap.event.EventObserverWrapper
import me.yifeiyuan.flap.ext.*
import me.yifeiyuan.flap.hook.IAdapterHookManager
import me.yifeiyuan.flap.hook.PreloadHook
import me.yifeiyuan.flap.pool.ComponentPool
import java.util.*
import me.yifeiyuan.flap.service.IAdapterServiceManager

/**
 * FlapAdapter is a flexible and powerful Adapter that makes you enjoy developing with RecyclerView.
 *
 *
 * Created by 程序亦非猿 on 2021/9/22.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 2020/9/22
 * @since 3.0.0
 */
open class FlapAdapter(val delegation: FlapDelegation = FlapDelegation()) : RecyclerView.Adapter<Component<*>>(), IAdapterHookManager by delegation, IAdapterDelegateManager by delegation, IAdapterServiceManager by delegation, SwipeDragHelper.Callback {

    companion object {
        private const val TAG = "FlapAdapter"
    }

    private var data: MutableList<Any> = mutableListOf()

    /**
     * Components 监听的生命周期对象，一般是 Activity
     * 默认取的是 RecyclerView.Context
     */
    private var lifecycleOwner: LifecycleOwner? = null

    /**
     * Components 是否监听生命周期事件
     */
    private var lifecycleEnable = true

    /**
     * 是否使用 ComponentPool
     */
    private var useComponentPool = true

    /**
     * 是否使用全局的 ComponentPool 做缓存
     */
    private var useGlobalComponentPool = false

    /**
     * RecyclerView 滑动到底部触发预加载
     */
    private var preloadHook: PreloadHook? = null

    /**
     * 所有事件的监听
     */
    var allEventsObserver: EventObserver? = null

    /**
     * 根据 Event.eventName 存放的
     */
    private val eventObservers: MutableMap<String, EventObserver> = mutableMapOf()

    /**
     * 是否使用 ApplicationContext 来创建 LayoutInflater 来创建 View
     *
     * 当开启后 Component.context 将变成 Application Context
     */
    var inflateWithApplicationContext = false

    private var paramProvider: ExtraParamsProvider? = null

    private var itemClicksHelper = ItemClicksHelper()
    val emptyViewHelper = EmptyViewHelper()

    lateinit var componentPool: ComponentPool

    lateinit var bindingRecyclerView: RecyclerView
    lateinit var bindingContext: Context

    init {
        adapterHooks.addAll(Flap.adapterHooks)
        adapterDelegates.addAll(Flap.adapterDelegates)
        adapterServices.putAll(Flap.adapterServices)

        delegation.fallbackDelegate = Flap.globalDefaultAdapterDelegate

        inflateWithApplicationContext = Flap.inflateWithApplicationContext
    }

    open fun setData(newDataList: MutableList<Any>) {
        data.clear()
        data.addAll(newDataList)
    }

    open fun <T : Any> setDataAndNotify(newDataList: MutableList<T>, notifyAll: Boolean = false) {
        data.clear()
        data.addAll(newDataList)
        if (notifyAll) {
            notifyDataSetChanged()
        } else {
            notifyItemRangeChanged(0, newDataList.size)
        }
    }

    open fun appendData(appendDataList: MutableList<Any>) {
        data.addAll(appendDataList)
    }

    open fun appendDataAndNotify(appendDataList: MutableList<Any>, notifyAll: Boolean = false) {
        data.addAll(appendDataList)
        if (notifyAll) {
            notifyDataSetChanged()
        } else {
            notifyItemRangeInserted(itemCount - appendDataList.size, appendDataList.size)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Component<*> {
        val layoutInflater = LayoutInflater.from(if (inflateWithApplicationContext) parent.context.applicationContext else parent.context)
        return delegation.onCreateViewHolder(this, parent, viewType, layoutInflater)
    }

    override fun onBindViewHolder(component: Component<*>, position: Int) {
        this.onBindViewHolder(component, position, mutableListOf())
    }

    override fun onBindViewHolder(
            component: Component<*>,
            position: Int,
            payloads: MutableList<Any>
    ) {
        val data = getItemData(position)
        delegation.onBindViewHolder(this, data, component, position, payloads)
        tryAttachLifecycleOwner(component)
    }

    open fun getItemData(position: Int): Any {
        return data[position]
    }

    override fun getItemViewType(position: Int): Int {
        val itemData = getItemData(position)
        return delegation.getItemViewType(position, itemData)
    }

    override fun getItemId(position: Int): Long {
        val itemData = getItemData(position)
        return delegation.getItemId(position, itemData)
    }

    /**
     * Attaches the component to lifecycle if need.
     *
     * @param component The component we are going to bind.
     */
    private fun tryAttachLifecycleOwner(component: Component<*>) {
        if (lifecycleEnable) {
            if (lifecycleOwner == null) {
                throw NullPointerException("lifecycleOwner == null,无法监听生命周期,请先调用 #setLifecycleOwner()")
            }
            lifecycleOwner!!.lifecycle.addObserver(component)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        handleOnAttachedToRecyclerView(recyclerView)
        delegation.onAttachedToRecyclerView(this, recyclerView)
    }

    private fun handleOnAttachedToRecyclerView(recyclerView: RecyclerView) {
        bindingRecyclerView = recyclerView
        bindingContext = recyclerView.context
        //当没设置 lifecycleOwner 尝试获取 context 作为 LifecycleOwner
        if (lifecycleOwner == null && recyclerView.context is LifecycleOwner) {
            FlapDebug.d(TAG, "onAttachedToRecyclerView，FlapAdapter 自动设置了 recyclerView.context 为 LifecycleOwner")
            setLifecycleOwner(recyclerView.context as LifecycleOwner)
        }

        if (useComponentPool) {
            if (!this::componentPool.isInitialized) {
                componentPool = if (useGlobalComponentPool) Flap.globalComponentPool else ComponentPool()
            }

            if (recyclerView.recycledViewPool != componentPool) {
                recyclerView.setRecycledViewPool(componentPool)
            }
            bindingContext.applicationContext.registerComponentCallbacks(componentPool)
        }

        itemClicksHelper.attachRecyclerView(recyclerView)
        emptyViewHelper.attachRecyclerView(recyclerView, true)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        FlapDebug.d(TAG, "onDetachedFromRecyclerView: ")
        itemClicksHelper.detachRecyclerView(recyclerView)
        emptyViewHelper.detachRecyclerView()
        if (this::componentPool.isInitialized) {
            bindingContext.applicationContext.unregisterComponentCallbacks(componentPool)
        }
        delegation.onDetachedFromRecyclerView(this, recyclerView)
    }

    /**
     * 会优先于 FlapComponentPool.putRecycledView 被调用
     */
    override fun onViewRecycled(component: Component<*>) {
        delegation.onViewRecycled(this, component)
    }

    override fun onFailedToRecycleView(component: Component<*>): Boolean {
        return delegation.onFailedToRecycleView(this, component)
    }

    override fun onViewAttachedToWindow(component: Component<*>) {
        delegation.onViewAttachedToWindow(this, component)
    }

    override fun onViewDetachedFromWindow(component: Component<*>) {
        delegation.onViewDetachedFromWindow(this, component)
    }

    /**
     * 设置 Component 监听的 LifecycleOwner
     * 会尝试去获取 recyclerView.context 作为 LifecycleOwner
     * @see handleOnAttachedToRecyclerView
     */
    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) = apply {
        this.lifecycleOwner = lifecycleOwner
    }

    /**
     * 设置 Component 是否监听生命周期
     *
     * 默认开启
     *
     * @param lifecycleEnable 是否开启
     */
    fun setLifecycleEnable(lifecycleEnable: Boolean) = apply {
        this.lifecycleEnable = lifecycleEnable
    }

    /**
     * Set whether use the global RecycledViewPool or not.
     *
     * NOTE : Call this before you call RecyclerView.setAdapter.
     *
     * 是否使用全局单例的 FlapComponentPool
     *
     * @param enable false by default
     */
    fun enableGlobalComponentPool(enable: Boolean) = apply {
        useGlobalComponentPool = enable
    }

    fun enableComponentPool(enable: Boolean) = apply {
        useComponentPool = enable
    }

    /**
     * 通过 Adapter 发送事件
     *
     * @see observeEvent
     * @see observerEvents
     */
    fun <T> fireEvent(event: Event<T>) {
        val observer = eventObservers[event.eventName]
        observer?.onEvent(event)

        allEventsObserver?.onEvent(event)
    }

    /**
     * 观察指定 eventName 的事件
     * @see fireEvent
     */
    fun <T> observeEvent(eventName: String, block: (Event<T>) -> Unit) = apply {
        eventObservers[eventName] = EventObserverWrapper(block)
    }

    /**
     * 观察所有的事件
     */
    fun observerEvents(block: (Event<*>) -> Unit) = apply {
        allEventsObserver = object : EventObserver {
            override fun onEvent(event: Event<*>) {
                block.invoke(event)
            }
        }
    }

    /**
     * 预加载
     *
     * @see PreloadHook
     */
    fun doOnPreload(offset: Int = 0, minItemCount: Int = 2, onPreload: () -> Unit) = apply {
        preloadHook?.let {
            unregisterAdapterHook(it)
        }
        preloadHook = PreloadHook(offset, minItemCount, onPreload).also {
            registerAdapterHook(it)
        }
    }

    /**
     * 设置是否启用预加载
     * 需要先调用 doOnPreload 开启才有效。
     */
    fun setPreloadEnable(enable: Boolean) = apply {
        preloadHook?.preloadEnable = enable
    }

    fun setPreloadComplete() {
        preloadHook?.setPreloadComplete()
    }

    /**
     * 提供 Component 从 Adapter 获取参数的方法
     *
     * @return key 对应的参数，如果类型不匹配，则会为 null
     */
    @Suppress("UNCHECKED_CAST")
    open fun <P> getParam(key: String): P? {
        return paramProvider?.getParam(key) as? P?
    }

    fun setParamProvider(block: (key: String) -> Any?) = apply {
        paramProvider = ExtraParamsProviderWrapper(block)
    }

    /**
     *
     * @see FlapAdapter.inflateWithApplicationContext
     * @return activity context
     */
    fun getActivityContext(): Context {
        return bindingContext
    }

    /**
     * 设置点击事件监听
     * @see doOnItemLongClick
     */
    fun doOnItemClick(onItemClick: OnItemClickListener?) = apply {
        itemClicksHelper.onItemClickListener = onItemClick
    }

    /**
     * 设置长按事件监听
     * @see doOnItemClick
     */
    fun doOnItemLongClick(onItemLongClick: OnItemLongClickListener?) = apply {
        itemClicksHelper.onItemLongClickListener = onItemLongClick
    }

    fun setEmptyView(emptyView: View?) = apply {
        emptyViewHelper.emptyView = emptyView
    }

    fun insertDataAt(position: Int, element: Any, notify: Boolean = true) {
        this.data.add(position, element)
        if (notify) {
            notifyItemInserted(position)
        }
    }

    fun removeDataAt(position: Int, notify: Boolean = true) {
        data.removeAt(position)
        if (notify) {
            notifyItemRemoved(position)
        }
    }

    fun swapData(fromPosition: Int, toPosition: Int, notify: Boolean = true) {
        Collections.swap(data, fromPosition, toPosition)
        if (notify) {
            notifyItemMoved(fromPosition, toPosition)
        }
    }

    override fun onSwiped(position: Int) {
        removeDataAt(position)
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
        swapData(fromPosition, toPosition)
    }
}