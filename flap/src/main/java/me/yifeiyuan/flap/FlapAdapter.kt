@file:Suppress("MemberVisibilityCanBePrivate")

package me.yifeiyuan.flap

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flap.delegate.AdapterDelegate
import me.yifeiyuan.flap.ext.Event
import me.yifeiyuan.flap.ext.EventObserver
import me.yifeiyuan.flap.ext.ParamProvider
import me.yifeiyuan.flap.ext.setOnItemClickListener
import me.yifeiyuan.flap.hook.AdapterHook
import me.yifeiyuan.flap.hook.PrefetchHook

/**
 * FlapAdapter is a flexible and powerful Adapter that makes you enjoy developing with RecyclerView.
 *
 *
 * Created by 程序亦非猿 on 2021/9/22.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 2020/9/22
 * @since 3.0
 */
open class FlapAdapter : RecyclerView.Adapter<Component<*>>(), IRegistry {

    companion object {
        private const val TAG = "FlapAdapter"
    }

    private var data: MutableList<Any> = mutableListOf()

    /**
     * Components 监听的生命周期对象，一般是 Activity 默认取的是 RecyclerView.Context
     */
    private var lifecycleOwner: LifecycleOwner? = null

    /**
     * Components 是否监听生命周期事件
     */
    private var lifecycleEnable = true

    /**
     * 是否使用全局的 ComponentPool 做缓存
     */
    private var useComponentPool = true

    /**
     * 默认 AdapterDelegate，兜底处理
     */
    private var defaultAdapterDelegate: AdapterDelegate<*, *>? = null

    private val adapterDelegates: MutableList<AdapterDelegate<*, *>> = mutableListOf()

    /**
     * RecyclerView 滑动到底部触发预加载
     */
    var prefetchDetector: PrefetchHook? = null

    //todo Map --> SparseArray ?
    private val viewTypeDelegateMapper: MutableMap<Int, AdapterDelegate<*, *>?> = mutableMapOf()
    private val delegateViewTypeMapper: MutableMap<AdapterDelegate<*, *>, Int> = mutableMapOf()

    /**
     *
     *
     * @see FlapAdapter.fireEvent
     */
    var eventObserver: EventObserver? = null

    private val hooks: MutableList<AdapterHook> = mutableListOf<AdapterHook>().apply {
        addAll(Flap.globalHooks)
    }

    /**
     * 是否使用 ApplicationContext 来创建 LayoutInflater 来创建 View
     *
     * 当开启后 Component.context 将变成 Application Context
     */
    var inflateWithApplicationContext = false

    var paramProvider: ParamProvider? = null

    //    TODO 真的需要吗？
    var onItemClickFunc: ((childView: View, position: Int) -> Unit)? = null

    var onItemClickListener: OnItemClickListener? = null

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

    override fun unRegisterAdapterHook(adapterHook: AdapterHook) {
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

    override fun unRegisterAdapterDelegate(adapterDelegate: AdapterDelegate<*, *>) {
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

    private fun dispatchOnCreateViewHolderStart(adapter: FlapAdapter, delegate: AdapterDelegate<*, *>?, viewType: Int) {
        hooks.forEach {
            it.onCreateViewHolderStart(adapter, delegate, viewType)
        }
    }

    private fun dispatchOnCreateViewHolderEnd(
            adapter: FlapAdapter,
            delegate: AdapterDelegate<*, *>?,
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
            attachLifecycleOwnerIfNeed(component)
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

        if (delegateViewTypeMapper.containsKey(delegate)) {
            return delegateViewTypeMapper[delegate]!!
        }

        itemViewType = delegate?.getItemViewType(itemData)
                ?: throw AdapterDelegateNotFoundException("$position , $itemData ,找不到对应的 Delegate，请先注册，或设置默认 Delegate")

        if (itemViewType == 0) {
            itemViewType = generateItemViewType()
        }
        FlapDebug.d(TAG, "getItemViewType() called with: position = $position , $itemViewType")
        viewTypeDelegateMapper[itemViewType] = delegate
        delegateViewTypeMapper[delegate] = itemViewType
        return itemViewType
    }

    private fun generateItemViewType(): Int {
        val viewType = ViewTypeGenerator.generateViewType()
        if (viewTypeDelegateMapper.containsKey(viewType)) {
            return generateItemViewType()
        }
        return viewType
    }

    private fun getDelegateByViewType(viewType: Int): AdapterDelegate<*, *> {
        return viewTypeDelegateMapper[viewType] ?: defaultAdapterDelegate
        ?: throw AdapterDelegateNotFoundException("找不到 viewType = ${viewType} 对应的 Delegate，请先注册，或设置默认 Delegate")
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
    private fun attachLifecycleOwnerIfNeed(component: Component<*>) {
        if (lifecycleEnable && lifecycleOwner != null) {
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
            setLifecycleOwner(recyclerView.context as LifecycleOwner)
        }
        if (useComponentPool) {
            recyclerView.setRecycledViewPool(Flap.globalComponentPool)
        }
        onItemClickListener?.let {
            recyclerView.setOnItemClickListener { v, p ->
                onItemClickFunc?.invoke(v, p)
                it.onItemClick(v, p)
            }
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
    }

    override fun onViewRecycled(component: Component<*>) {
        component.onViewRecycled(this)
    }

    override fun onFailedToRecycleView(component: Component<*>): Boolean {
        return component.onFailedToRecycleView(this)
    }

    override fun onViewAttachedToWindow(component: Component<*>) {
        component.onViewAttachedToWindow(this)
    }

    override fun onViewDetachedFromWindow(component: Component<*>) {
        component.onViewDetachedFromWindow(this)
    }

    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner): FlapAdapter {
        this.lifecycleOwner = lifecycleOwner
        return this
    }

    fun setLifecycleEnable(lifecycleEnable: Boolean): FlapAdapter {
        this.lifecycleEnable = lifecycleEnable
        return this
    }

    /**
     * Set whether use the global RecycledViewPool or not.
     *
     * NOTE : Call this before you call RecyclerView.setAdapter.
     *
     * @param enable true by default
     * @return this
     */
    fun setUseComponentPool(enable: Boolean): FlapAdapter {
        useComponentPool = enable
        return this
    }

    fun fireEvent(event: Event<*>) {
        eventObserver?.onEvent(event)
    }

    fun doOnPrefetch(offset: Int, minItemCount: Int, onPrefetch: () -> Unit) {
        prefetchDetector?.let {
            unRegisterAdapterHook(it)
        }
        prefetchDetector = PrefetchHook(offset, minItemCount, onPrefetch).also {
            registerAdapterHook(it)
        }
    }

    fun setPrefetchEnable(enable: Boolean) {
        prefetchDetector?.prefetchEnable = enable
    }

    fun setPrefetchComplete() {
        prefetchDetector?.setPrefetchComplete()
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

    fun attachTo(recyclerView: RecyclerView) {
        recyclerView.adapter = this
    }

    /**
     *
     * @see FlapAdapter.inflateWithApplicationContext
     * @return activity context
     */
    fun getActivityContext(): Context {
        return bindingContext
    }

    interface OnItemClickListener {

        //为什么不直接返回 data 呢？担心有的人使用 Empty Error 那种特殊状态，getItemData 会出错
//        fun onItemClick(childView: View, position: Int, data: Any)

        fun onItemClick(childView: View, position: Int)
    }

}