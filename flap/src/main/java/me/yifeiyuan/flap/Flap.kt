@file:Suppress("UNCHECKED_CAST")

package me.yifeiyuan.flap

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flap.delegate.AdapterDelegate
import me.yifeiyuan.flap.delegate.FallbackAdapterDelegate
import me.yifeiyuan.flap.event.Event
import me.yifeiyuan.flap.event.EventObserver
import me.yifeiyuan.flap.event.EventObserverWrapper
import me.yifeiyuan.flap.ext.*
import me.yifeiyuan.flap.hook.AdapterHook
import me.yifeiyuan.flap.hook.PreloadHook
import me.yifeiyuan.flap.pool.ComponentPool
import me.yifeiyuan.flap.service.AdapterService

/**
 * 负责代理部分 Adapter API 实现
 *
 * 本着组合由于继承的理念，Flap SDK 核心功能在这里实现，方便适配多种系统 Adapter。
 *
 * Created by 程序亦非猿 on 2022/9/27.
 *
 * @since 3.1.5
 */
class Flap : FlapApi {

    override val adapterDelegates: MutableList<AdapterDelegate<*, *>> = mutableListOf()
    override val adapterHooks: MutableList<AdapterHook> = mutableListOf()
    override val adapterServices: MutableMap<Class<*>, AdapterService> = mutableMapOf()

    companion object {
        private const val TAG = "Flap"
    }

    /**
     * 默认使用 RecyclerView.Context，如果在 Fragment 里使用，则需要设置为 Fragment.viewLifecycleOwner
     */
    private var lifecycleOwner: LifecycleOwner? = null

    private val viewTypeDelegateCache: MutableMap<Int, AdapterDelegate<*, *>?> = mutableMapOf()
    private val delegateViewTypeCache: MutableMap<AdapterDelegate<*, *>, Int> = mutableMapOf()

    var fallbackDelegate: FallbackAdapterDelegate

    /**
     * 是否使用 ApplicationContext 来创建 LayoutInflater 来创建 View
     *
     * 当开启后 Component.context 将变成 Application Context
     */
    var inflateWithApplicationContext = false

    private val emptyViewHelperImpl = EmptyViewHelper().also {
        registerAdapterHook(it)
    }

    lateinit var componentPool: ComponentPool

    lateinit var bindingRecyclerView: RecyclerView
    lateinit var bindingContext: Context

    var flapRuntime: FlapRuntime? = null

    var activityContext: Activity? = null

    init {
        adapterHooks.addAll(FlapInitializer.adapterHooks)
        adapterDelegates.addAll(FlapInitializer.adapterDelegates)
        adapterServices.putAll(FlapInitializer.adapterServices)

        fallbackDelegate = FlapInitializer.globalFallbackAdapterDelegate

        inflateWithApplicationContext = FlapInitializer.inflateWithApplicationContext
    }

    override fun onCreateViewHolder(adapter: RecyclerView.Adapter<*>, parent: ViewGroup, viewType: Int): Component<*> {
        val delegate = getDelegateByViewType(viewType)
        dispatchOnPreCreateViewHolder(adapter, viewType)

        val inflater = userLayoutInflater
                ?: LayoutInflater.from(if (inflateWithApplicationContext) parent.context.applicationContext else parent.context)

        val component = delegate.onCreateViewHolder(inflater, parent, viewType)
        dispatchOnPostCreateViewHolder(adapter, viewType, component)
        return component
    }

    private fun dispatchOnPreCreateViewHolder(adapter: RecyclerView.Adapter<*>, viewType: Int) {
        try {
            adapterHooks.forEach {
                it.onPreCreateViewHolder(adapter, viewType)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun dispatchOnPostCreateViewHolder(adapter: RecyclerView.Adapter<*>, viewType: Int, component: Component<*>) {
        try {
            adapterHooks.forEach {
                it.onPostCreateViewHolder(adapter, viewType, component)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getDelegateByViewType(viewType: Int): AdapterDelegate<*, *> {
        return viewTypeDelegateCache[viewType] ?: fallbackDelegate
    }

    override fun onBindViewHolder(
            adapter: RecyclerView.Adapter<*>,
            itemData: Any,
            component: Component<*>,
            position: Int,
            payloads: MutableList<Any>) {
        try {
            val delegate = getDelegateByViewType(component.itemViewType)
            dispatchOnPreBindViewHolder(adapter, component, itemData, position, payloads)
            delegate.onBindViewHolder(
                    component,
                    itemData,
                    position,
                    payloads,
                    adapter,
                    this
            )
            dispatchOnPostBindViewHolder(adapter, component, itemData, position, payloads)
            tryAttachLifecycleOwner(component)
        } catch (e: Exception) {
            FlapDebug.e(TAG, "onBindViewHolder: Error = ", e)
        }
    }

    /**
     * Attaches the component to lifecycle if need.
     *
     * @param component The component we are going to bind.
     */
    private fun tryAttachLifecycleOwner(component: Component<*>) {
        if (lifecycleOwner == null) {
            throw NullPointerException("lifecycleOwner can not be null, please call FlapAdapter#setLifecycleOwner() first")
        }
        lifecycleOwner!!.lifecycle.addObserver(component)
    }

    private fun dispatchOnPreBindViewHolder(
            adapter: RecyclerView.Adapter<*>,
            component: Component<*>,
            itemData: Any,
            position: Int,
            payloads: MutableList<Any>) {
        try {
            adapterHooks.forEach {
                it.onPreBindViewHolder(adapter, component, itemData, position, payloads)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun dispatchOnPostBindViewHolder(
            adapter: RecyclerView.Adapter<*>,
            component: Component<*>,
            data: Any,
            position: Int,
            payloads: MutableList<Any>) {
        try {
            adapterHooks.forEach {
                it.onPostBindViewHolder(adapter, component, data, position, payloads)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemViewType(position: Int, itemData: Any): Int {

        var itemViewType: Int

        val delegate: AdapterDelegate<*, *> = adapterDelegates.firstOrNull {
            it.isDelegateFor(itemData)
        } ?: fallbackDelegate

        if (delegateViewTypeCache.containsKey(delegate)) {
            return delegateViewTypeCache[delegate]!!
        }

        itemViewType = delegate.getItemViewType(itemData)

        if (itemViewType == 0) {
            itemViewType = generateItemViewType()
        }
        FlapDebug.d(TAG, "getItemViewType() called with: position = $position , itemViewType = $itemViewType")
        viewTypeDelegateCache[itemViewType] = delegate
        delegateViewTypeCache[delegate] = itemViewType
        return itemViewType
    }

    private fun generateItemViewType(): Int {
        val viewType = ViewTypeGenerator.generateViewType()
        if (viewTypeDelegateCache.containsKey(viewType)) {
            return generateItemViewType()
        }
        return viewType
    }

    override fun getItemId(position: Int, itemData: Any): Long {
        val delegate = getDelegateByViewType(getItemViewType(position, itemData))
        return delegate.getItemId(itemData, position)
    }

    /**
     * 会优先于 FlapComponentPool.putRecycledView 被调用
     * @see RecyclerView.Adapter.onViewRecycled
     */
    override fun onViewRecycled(adapter: RecyclerView.Adapter<*>, component: Component<*>) {
        val delegate = getDelegateByViewType(component.itemViewType)
        delegate.onViewRecycled(adapter, component)
    }

    /**
     * @see RecyclerView.Adapter.onFailedToRecycleView
     */
    override fun onFailedToRecycleView(adapter: RecyclerView.Adapter<*>, component: Component<*>): Boolean {
        val delegate = getDelegateByViewType(component.itemViewType)
        return delegate.onFailedToRecycleView(adapter, component)
    }

    /**
     * @see RecyclerView.Adapter.onViewAttachedToWindow
     */
    override fun onViewAttachedToWindow(adapter: RecyclerView.Adapter<*>, component: Component<*>) {
        val delegate = getDelegateByViewType(component.itemViewType)
        delegate.onViewAttachedToWindow(adapter, component)
        dispatchOnViewAttachedToWindow(adapter, component)
    }

    private fun dispatchOnViewAttachedToWindow(adapter: RecyclerView.Adapter<*>, component: Component<*>) {
        try {
            adapterHooks.forEach {
                it.onViewAttachedToWindow(adapter, component)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * @see RecyclerView.Adapter.onViewDetachedFromWindow
     */
    override fun onViewDetachedFromWindow(adapter: RecyclerView.Adapter<*>, component: Component<*>) {
        val delegate = getDelegateByViewType(component.itemViewType)
        delegate.onViewDetachedFromWindow(adapter, component)
        dispatchOnViewDetachedFromWindow(adapter, component)
    }

    private fun dispatchOnViewDetachedFromWindow(adapter: RecyclerView.Adapter<*>, component: Component<*>) {
        try {
            adapterHooks.forEach {
                it.onViewDetachedFromWindow(adapter, component)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 是否使用 ComponentPool
     */
    private var useComponentPool = true

    /**
     * @see RecyclerView.Adapter.onAttachedToRecyclerView
     */
    override fun onAttachedToRecyclerView(adapter: RecyclerView.Adapter<*>, recyclerView: RecyclerView) {
        handleOnAdapterAttachedToRecyclerView(recyclerView)
        dispatchOnAdapterAttachedToRecyclerView(adapter, recyclerView)
    }

    private fun handleOnAdapterAttachedToRecyclerView(recyclerView: RecyclerView) {
        bindingRecyclerView = recyclerView
        bindingContext = recyclerView.context

        if (bindingContext is Activity) {
            activityContext = bindingContext as Activity
        }

        //当没设置 lifecycleOwner 尝试获取 context 作为 LifecycleOwner
        if (lifecycleOwner == null && recyclerView.context is LifecycleOwner) {
            FlapDebug.d(TAG, "onAttachedToRecyclerView，FlapAdapter 自动设置了 recyclerView.context 为 LifecycleOwner")
            lifecycleOwner = recyclerView.context as LifecycleOwner
        }

        if (useComponentPool) {
            if (!this::componentPool.isInitialized) {
                componentPool = ComponentPool()
            }

            if (recyclerView.recycledViewPool != componentPool) {
                recyclerView.setRecycledViewPool(componentPool)
            }
            bindingContext.applicationContext.registerComponentCallbacks(componentPool)
        }
    }

    private fun dispatchOnAdapterAttachedToRecyclerView(adapter: RecyclerView.Adapter<*>, recyclerView: RecyclerView) {
        try {
            adapterHooks.forEach {
                it.onAdapterAttachedToRecyclerView(adapter, recyclerView)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDetachedFromRecyclerView(adapter: RecyclerView.Adapter<*>, recyclerView: RecyclerView) {
        if (this::componentPool.isInitialized) {
            bindingContext.applicationContext.unregisterComponentCallbacks(componentPool)
        }
        dispatchOnDetachedFromRecyclerView(adapter, recyclerView)
    }

    private fun dispatchOnDetachedFromRecyclerView(adapter: RecyclerView.Adapter<*>, recyclerView: RecyclerView) {
        try {
            adapterHooks.forEach {
                it.onAdapterDetachedFromRecyclerView(adapter, recyclerView)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * RecyclerView 滑动到顶部触发预加载
     */
    private var scrollUpPreloadHook: PreloadHook? = null

    /**
     * RecyclerView 滑动到底部触发预加载
     */
    private var scrollDownPreloadHook: PreloadHook? = null

    override fun doOnPreload(offset: Int, minItemCount: Int, direction: Int, onPreload: () -> Unit) = apply {
        when (direction) {
            PreloadHook.SCROLL_UP -> {
                scrollUpPreloadHook?.let {
                    unregisterAdapterHook(it)
                }
                scrollUpPreloadHook = PreloadHook(offset, minItemCount, direction, onPreload = onPreload).also {
                    registerAdapterHook(it)
                }
            }
            PreloadHook.SCROLL_DOWN -> {
                scrollDownPreloadHook?.let {
                    unregisterAdapterHook(it)
                }
                scrollDownPreloadHook = PreloadHook(offset, minItemCount, direction, onPreload = onPreload).also {
                    registerAdapterHook(it)
                }
            }
        }
    }

    override fun setPreloadEnable(enable: Boolean, direction: Int) = apply {
        when (direction) {
            PreloadHook.SCROLL_UP -> {
                scrollUpPreloadHook?.preloadEnable = enable
            }
            PreloadHook.SCROLL_DOWN -> {
                scrollDownPreloadHook?.preloadEnable = enable
            }
        }
    }

    override fun setPreloadComplete(direction: Int) {
        when (direction) {
            PreloadHook.SCROLL_UP -> {
                scrollUpPreloadHook?.setPreloadComplete()
            }
            PreloadHook.SCROLL_DOWN -> {
                scrollDownPreloadHook?.setPreloadComplete()
            }
        }
    }

    private var itemClicksHelper = ItemClicksHelper().also {
        registerAdapterHook(it)
    }

    override fun doOnItemClick(onItemClick: OnItemClickListener?) = apply {
        itemClicksHelper.onItemClickListener = onItemClick
    }

    override fun doOnItemLongClick(onItemLongClick: OnItemLongClickListener?) = apply {
        itemClicksHelper.onItemLongClickListener = onItemLongClick
    }

    override fun withEmptyView(emptyView: View?) = apply {
        emptyViewHelperImpl.emptyView = emptyView
    }

    override fun getEmptyViewHelper(): EmptyViewHelper {
        return emptyViewHelperImpl
    }

    private var paramProvider: ExtraParamsProvider? = null

    @Suppress("UNCHECKED_CAST")
    override fun <P> getParam(key: String): P? {
        return paramProvider?.getParam(key) as? P?
    }

    override fun withParamProvider(provider: (key: String) -> Any?) = apply {
        paramProvider = ExtraParamsProviderWrapper(provider)
    }

    override fun withLifecycleOwner(lifecycleOwner: LifecycleOwner) = apply {
        this.lifecycleOwner = lifecycleOwner
    }

    override fun setComponentPoolEnable(enable: Boolean) = apply {
        useComponentPool = enable
    }

    /**
     * 所有事件的监听
     */
    var allEventsObserver: EventObserver? = null

    /**
     * 根据 Event.eventName 存放的
     */
    private val eventObservers: MutableMap<String, EventObserver> = mutableMapOf()

    /**
     * 通过 Adapter 发送事件
     *
     * @see observeEvent
     * @see observerEvents
     */
    override fun <T> fireEvent(event: Event<T>) {
        val observer = eventObservers[event.eventName]
        observer?.onEvent(event)

        allEventsObserver?.onEvent(event)
    }

    /**
     * 观察指定 eventName 的事件
     * @see fireEvent
     */
    override fun <T> observeEvent(eventName: String, block: (Event<T>) -> Unit) = apply {
        eventObservers[eventName] = EventObserverWrapper(block)
    }

    override fun observerEvents(block: (Event<*>) -> Unit) = apply {
        allEventsObserver = object : EventObserver {
            override fun onEvent(event: Event<*>) {
                block.invoke(event)
            }
        }
    }

    override fun withActivity(activity: Activity) = apply {
        this.activityContext = activity
        if (activity is LifecycleOwner) {
            lifecycleOwner = activity
        }
    }

    override fun <T : Activity> getActivity(): T {
        return activityContext as T
    }

    override fun withRuntime(runtime: FlapRuntime) = apply {
        flapRuntime = runtime
    }

    override fun <T : FlapRuntime> getRuntime(): T {
        return flapRuntime as T
    }

    var userLayoutInflater: LayoutInflater? = null
    override fun withLayoutInflater(layoutInflater: LayoutInflater) = apply {
        userLayoutInflater = layoutInflater
    }
}