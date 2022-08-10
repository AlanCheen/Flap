@file:Suppress("MemberVisibilityCanBePrivate","unused")

package me.yifeiyuan.flap

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flap.delegate.AdapterDelegate
import me.yifeiyuan.flap.event.Event
import me.yifeiyuan.flap.event.EventObserver
import me.yifeiyuan.flap.event.EventObserverWrapper
import me.yifeiyuan.flap.ext.*
import me.yifeiyuan.flap.hook.AdapterHook
import me.yifeiyuan.flap.hook.PreloadHook

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
open class FlapAdapter : RecyclerView.Adapter<Component<*>>(), IRegistry {

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
     * 是否使用全局的 ComponentPool 做缓存
     */
    private var useGlobalComponentPool = false

    /**
     * 默认 AdapterDelegate，兜底处理
     */
    private var defaultAdapterDelegate: AdapterDelegate<*, *>? = null

    private val adapterDelegates: MutableList<AdapterDelegate<*, *>> = mutableListOf()

    private val hooks: MutableList<AdapterHook> = mutableListOf()

    /**
     * RecyclerView 滑动到底部触发预加载
     */
    var preloadHook: PreloadHook? = null

    private val viewTypeDelegateCache: MutableMap<Int, AdapterDelegate<*, *>?> = mutableMapOf()
    private val delegateViewTypeCache: MutableMap<AdapterDelegate<*, *>, Int> = mutableMapOf()

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

    var paramProvider: ExtraParamsProvider? = null

    private var itemClicksHelper = ItemClicksHelper()
    private val emptyViewHelper = EmptyViewHelper()

    lateinit var bindingRecyclerView: RecyclerView
    lateinit var bindingContext: Context

    init {
        hooks.addAll(Flap.globalHooks)
        adapterDelegates.addAll(Flap.globalAdapterDelegates)
        Flap.globalDefaultAdapterDelegate?.let {
            defaultAdapterDelegate = it
        }

        inflateWithApplicationContext = Flap.inflateWithApplicationContext
    }

    override fun registerAdapterHook(adapterHook: AdapterHook) {
        hooks.add(adapterHook)
    }

    override fun registerAdapterHooks(vararg adapterHooks: AdapterHook) {
        hooks.addAll(adapterHooks)
    }

    override fun unregisterAdapterHook(adapterHook: AdapterHook) {
        hooks.remove(adapterHook)
    }

    override fun clearAdapterHooks() {
        hooks.clear()
    }

    override fun registerAdapterDelegate(adapterDelegate: AdapterDelegate<*, *>) {
        adapterDelegates.add(adapterDelegate)
    }

    override fun registerAdapterDelegates(vararg delegates: AdapterDelegate<*, *>) {
        delegates.forEach {
            registerAdapterDelegate(it)
        }
    }

    override fun unregisterAdapterDelegate(adapterDelegate: AdapterDelegate<*, *>) {
        adapterDelegates.remove(adapterDelegate)
    }

    override fun clearAdapterDelegates() {
        adapterDelegates.clear()
    }

    open fun setData(newDataList: MutableList<Any>) {
        data.clear()
        data.addAll(newDataList)
        notifyDataSetChanged()
    }

    open fun appendData(appendDataList: MutableList<Any>) {
        data.addAll(appendDataList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Component<*> {
        val delegate = getDelegateByViewType(viewType)
        dispatchOnCreateViewHolderStart(this, delegate, viewType)
        val layoutInflater = LayoutInflater.from(if (inflateWithApplicationContext) parent.context.applicationContext else parent.context)
        val component = delegate.onCreateViewHolder(layoutInflater, parent, viewType)
        dispatchOnCreateViewHolderEnd(this, delegate, viewType, component)
        return component
    }

    private fun dispatchOnCreateViewHolderStart(adapter: FlapAdapter, delegate: AdapterDelegate<*, *>, viewType: Int) {
        hooks.forEach {
            it.onCreateViewHolderStart(adapter, delegate, viewType)
        }
    }

    private fun dispatchOnCreateViewHolderEnd(
            adapter: FlapAdapter,
            delegate: AdapterDelegate<*, *>,
            viewType: Int,
            component: Component<*>
    ) {
        hooks.forEach {
            it.onCreateViewHolderEnd(adapter, delegate, viewType, component)
        }
    }

    override fun onBindViewHolder(component: Component<*>, position: Int) {
        this.onBindViewHolder(component, position, mutableListOf())
    }

    override fun onBindViewHolder(
            component: Component<*>,
            position: Int,
            payloads: MutableList<Any>
    ) {
        try {
            val delegate = getDelegateByViewType(component.itemViewType)
            val data = getItemData(position)
            dispatchOnBindViewHolderStart(this, delegate, component, data, position, payloads)
            delegate.onBindViewHolder(
                    component,
                    data,
                    position,
                    payloads,
                    this
            )
            dispatchOnBindViewHolderEnd(this, delegate, component, data, position, payloads)
            tryAttachLifecycleOwner(component)
        } catch (e: Exception) {
            e.printStackTrace()
            FlapDebug.e(TAG, "onBindViewHolder: Error = ", e)
        }
    }

    private fun dispatchOnBindViewHolderStart(
            adapter: FlapAdapter,
            delegate: AdapterDelegate<*, *>,
            component: Component<*>,
            data: Any,
            position: Int,
            payloads: MutableList<Any>
    ) {
        hooks.forEach {
            it.onBindViewHolderStart(adapter, delegate, component, data, position, payloads)
        }
    }

    private fun dispatchOnBindViewHolderEnd(
            adapter: FlapAdapter,
            delegate: AdapterDelegate<*, *>,
            component: Component<*>,
            data: Any,
            position: Int,
            payloads: MutableList<Any>
    ) {
        hooks.forEach {
            it.onBindViewHolderEnd(adapter, delegate, component, data, position, payloads)
        }
    }

    open fun getItemData(position: Int): Any {
        return data[position]
    }

    override fun getItemViewType(position: Int): Int {
        var itemViewType: Int
        val itemData = getItemData(position)

        val delegate: AdapterDelegate<*, *>? = adapterDelegates.firstOrNull {
            it.delegate(itemData)
        } ?: defaultAdapterDelegate

        if (delegateViewTypeCache.containsKey(delegate)) {
            return delegateViewTypeCache[delegate]!!
        }

        itemViewType = delegate?.getItemViewType(itemData)
                ?: throw AdapterDelegateNotFoundException("找不到对应的 AdapterDelegate，请先注册或设置默认 AdapterDelegate ，position=$position , itemData=$itemData")

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

    fun getDelegateByViewType(viewType: Int): AdapterDelegate<*, *> {
        return viewTypeDelegateCache[viewType] ?: defaultAdapterDelegate
        ?: throw AdapterDelegateNotFoundException("找不到 viewType = $viewType 对应的 Delegate，请先注册，或设置默认的 Delegate")
    }

    override fun getItemId(position: Int): Long {
        val itemData = getItemData(position)
        val delegate = getDelegateByViewType(getItemViewType(position))
        return delegate.getItemId(itemData)
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
    }

    private fun handleOnAttachedToRecyclerView(recyclerView: RecyclerView) {
        bindingRecyclerView = recyclerView
        bindingContext = recyclerView.context
        //当没设置 lifecycleOwner 尝试获取 context 作为 LifecycleOwner
        if (lifecycleOwner == null && recyclerView.context is LifecycleOwner) {
            FlapDebug.d(TAG, "onAttachedToRecyclerView，FlapAdapter 自动设置了 recyclerView.context 为 LifecycleOwner")
            setLifecycleOwner(recyclerView.context as LifecycleOwner)
        }

        val pool = if (useGlobalComponentPool) Flap.globalComponentPool else FlapComponentPool()
        recyclerView.setRecycledViewPool(pool)
//        recyclerView.context.applicationContext.registerComponentCallbacks(pool)

        itemClicksHelper.attachRecyclerView(recyclerView)
        emptyViewHelper.attachRecyclerView(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        FlapDebug.d(TAG, "onDetachedFromRecyclerView: ")
        itemClicksHelper.detachRecyclerView(recyclerView)
        emptyViewHelper.detachRecyclerView(recyclerView)
    }

    /**
     * 会优先于 FlapComponentPool.putRecycledView 被调用
     */
    override fun onViewRecycled(component: Component<*>) {
        val delegate = getDelegateByViewType(component.itemViewType)
        delegate.onViewRecycled(this, component)
    }

    override fun onFailedToRecycleView(component: Component<*>): Boolean {
        val delegate = getDelegateByViewType(component.itemViewType)
        return delegate.onFailedToRecycleView(this, component)
    }

    override fun onViewAttachedToWindow(component: Component<*>) {
        val delegate = getDelegateByViewType(component.itemViewType)
        delegate.onViewAttachedToWindow(this, component)
        hooks.forEach {
            it.onViewAttachedToWindow(this, delegate, component)
        }
    }

    override fun onViewDetachedFromWindow(component: Component<*>) {
        val delegate = getDelegateByViewType(component.itemViewType)
        delegate.onViewDetachedFromWindow(this, component)
        hooks.forEach {
            it.onViewDetachedFromWindow(this, delegate, component)
        }
    }

    /**
     * 设置 Component 监听的 LifecycleOwner
     * 会尝试去获取 recyclerView.context 作为 LifecycleOwner
     * @see handleOnAttachedToRecyclerView
     */
    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner): FlapAdapter {
        this.lifecycleOwner = lifecycleOwner
        return this
    }

    /**
     * 设置 Component 是否监听生命周期
     *
     * 默认开启
     *
     * @param lifecycleEnable 是否开启
     */
    fun setLifecycleEnable(lifecycleEnable: Boolean): FlapAdapter {
        this.lifecycleEnable = lifecycleEnable
        return this
    }

    /**
     * Set whether use the global RecycledViewPool or not.
     *
     * NOTE : Call this before you call RecyclerView.setAdapter.
     *
     * 是否使用全局单例的 FlapComponentPool
     *
     * @param enable false by default
     * @return this
     */
    fun setUseGlobalComponentPool(enable: Boolean): FlapAdapter {
        useGlobalComponentPool = enable
        return this
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
    fun <T> observeEvent(eventName: String, block: (Event<T>) -> Unit) {
        eventObservers[eventName] = EventObserverWrapper(block)
    }

    /**
     * 观察所有的事件
     */
    fun observerEvents(block: (Event<*>) -> Unit) {
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
    fun doOnPreload(offset: Int = 0, minItemCount: Int = 2, onPreload: () -> Unit) {
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
    fun setPreloadEnable(enable: Boolean) {
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
    fun doOnItemClick(onItemClick: OnItemClickListener?) {
        itemClicksHelper.onItemClickListener = onItemClick
    }

    /**
     * 设置长按事件监听
     * @see doOnItemClick
     */
    fun doOnItemLongClick(onItemLongClick: OnItemLongClickListener?) {
        itemClicksHelper.onItemLongClickListener = onItemLongClick
    }

    fun setEmptyView(emptyView: View?) {
        emptyViewHelper.emptyView = emptyView
    }

    /**
     * 当 Adapter.data 中存在一个 Model 没有对应的 AdapterDelegate.delegate()==true 时抛出
     */
    internal class AdapterDelegateNotFoundException(errorMessage: String) : Exception(errorMessage)
}

