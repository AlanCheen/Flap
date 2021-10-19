package me.yifeiyuan.flap

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flap.hook.AdapterHook
import me.yifeiyuan.flap.ext.setOnItemClickListener

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
open class FlapAdapter : RecyclerView.Adapter<Component<*>>() {

    companion object {
        private const val TAG = "FlapAdapter"
    }

    var data: MutableList<Any> = mutableListOf()

    private var lifecycleOwner: LifecycleOwner? = null
    private var lifecycleEnable = true
    private var useComponentPool = true

    private var defaultAdapterDelegate: AdapterDelegate<*, *>? = null

    private val adapterDelegates: MutableList<AdapterDelegate<*, *>> = mutableListOf()

    //TODO 将空、异常状态放到 Adapter 真的好吗？
//    private val emptyStatusAdapterDelegate: AdapterDelegate<*, *>? = null
//    private val errorStatusAdapterDelegate: AdapterDelegate<*, *>? = null
//    private var state: UltraRecyclerView.State = UltraRecyclerView.State.Empty

    private val viewTypeDelegateMapper: MutableMap<Int, AdapterDelegate<*, *>?> = mutableMapOf()
    private val delegateViewTypeMapper: MutableMap<AdapterDelegate<*, *>, Int> = mutableMapOf()

    private val eventObservers = mutableListOf<OnEventObserver>()

    private val hooks: MutableList<AdapterHook> = mutableListOf<AdapterHook>().apply {
        addAll(Flap.hooks)
    }

    var paramProvider: ParamProvider? = null

    //    TODO 真的需要吗？
//    var onItemClickListener: ((View, Int, Any) -> Unit)? = null
    var onItemClickListener: OnItemClickListener? = null

    init {
        hooks.addAll(Flap.hooks)
        adapterDelegates.addAll(Flap.adapterDelegates)
        Flap.defaultAdapterDelegate?.let {
            defaultAdapterDelegate = it
        }
    }

    fun registerAdapterHook(adapterHook: AdapterHook) {
        hooks.add(adapterHook)
    }

    fun unRegisterAdapterHook(adapterHook: AdapterHook) {
        hooks.remove(adapterHook)
    }

    fun registerAdapterDelegate(adapterDelegate: AdapterDelegate<*, *>) {
        adapterDelegates.add(adapterDelegate)
    }

    fun registerAdapterDelegates(vararg delegates: AdapterDelegate<*, *>) {
        delegates.forEach {
            registerAdapterDelegate(it)
        }
    }

    fun unRegisterAdapterDelegate(adapterDelegate: AdapterDelegate<*, *>) {
        adapterDelegates.remove(adapterDelegate)
    }

    fun registerOnEventObserver(observer: OnEventObserver) {
        eventObservers.add(observer)
    }

    open fun setData(list: MutableList<Any>, notifyDataSetChanged: Boolean = true) {
        data = list
        if (notifyDataSetChanged) {
            notifyDataSetChanged()
        }
    }

    open fun appendData(list: MutableList<Any>, notifyDataSetChanged: Boolean = true) {
        data.addAll(list)
        if (notifyDataSetChanged) {
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Component<*> {
        val delegate = getDelegateByViewType(viewType)
        dispatchOnCreateViewHolderStart(this, delegate, viewType)
        val component = delegate.onCreateViewHolder(LayoutInflater.from(parent.context), parent, viewType)
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
        //ignore
        Log.d(TAG, "onBindViewHolder() called with: component = $component, position = $position")
    }

    override fun onBindViewHolder(
            component: Component<*>,
            position: Int,
            payloads: MutableList<Any>
    ) {
        Log.d(TAG, "onBindViewHolder() called with: component = $component, position = $position, payloads = $payloads")
        attachLifecycleOwnerIfNeed(component)
        val delegate = getDelegateByComponent(component)
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
        Log.d(TAG, "getItemViewType() called with: position = $position , $itemViewType")
        viewTypeDelegateMapper[itemViewType] = delegate
        delegateViewTypeMapper[delegate] = itemViewType
        return itemViewType
    }

    private fun generateItemViewType(): Int {
        val viewType = ViewCompat.generateViewId()
        if (viewTypeDelegateMapper.containsKey(viewType)) {
            return generateItemViewType()
        }
        return viewType
    }

    private fun getDelegateByComponent(component: Component<*>): AdapterDelegate<*, *> {
        return getDelegateByViewType(component.itemViewType)
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
        if (recyclerView.context is LifecycleOwner && lifecycleOwner == null) {
            setLifecycleOwner(recyclerView.context as LifecycleOwner)
        }
        if (useComponentPool) {
            recyclerView.setRecycledViewPool(Flap.globalComponentPool)
        }
        onItemClickListener?.let {
            recyclerView.setOnItemClickListener { v, p ->
                it.onItemClick(v, p)
            }
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
    }

    override fun onViewRecycled(holder: Component<*>) {
        holder.onViewRecycled(this)
    }

    override fun onFailedToRecycleView(holder: Component<*>): Boolean {
        return holder.onFailedToRecycleView(this)
    }

    override fun onViewAttachedToWindow(holder: Component<*>) {
        holder.onViewAttachedToWindow(this)
    }

    override fun onViewDetachedFromWindow(holder: Component<*>) {
        holder.onViewDetachedFromWindow(this)
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
        eventObservers.forEach {
            it.onEvent(event)
        }
    }

    /**
     *
     * @return key 对应的参数，如果类型不匹配，则会为 null
     */
    @Suppress("UNCHECKED_CAST")
    open fun <P> getParam(key: String): P? {
        return paramProvider?.getParam(key) as? P?
    }

    interface ParamProvider {
        fun getParam(key: String): Any?
    }

    interface OnEventObserver {
        fun onEvent(event: Event<*>)
    }

    interface OnItemClickListener {

        //为什么不直接返回 data 呢？担心有的人使用 Empty Error 那种特殊状态，getItemData 会出错
//        fun onItemClick(childView: View, position: Int, data: Any)

        fun onItemClick(childView: View, position: Int)
    }

}