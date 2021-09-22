package me.yifeiyuan.flap

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flap.exceptions.DelegateNotFoundException
import me.yifeiyuan.flap.extensions.AdapterHook

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
    private var useFlapItemPool = true

    private var defaultAdapterDelegate: AdapterDelegate<*, *>? = null

    private val adapterDelegates: MutableList<AdapterDelegate<*, *>> = mutableListOf()

    //TODO 将空、异常状态放到 Adapter 真的好吗？
//    private val emptyStatusAdapterDelegate: AdapterDelegate<*, *>? = null
//    private val errorStatusAdapterDelegate: AdapterDelegate<*, *>? = null
//    private var state: UltraRecyclerView.State = UltraRecyclerView.State.Empty

    private val viewTypeDelegateMapper: MutableMap<Int, AdapterDelegate<*, *>?> = mutableMapOf()

    private val hooks: MutableList<AdapterHook> = mutableListOf<AdapterHook>().apply {
        addAll(Flap.hooks)
    }

//    TODO 真的需要吗？
//    var onItemClickListener: ((View, Int, Any) -> Unit)? = null

    private val dataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            super.onChanged()
            checkEmptyStatus()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            checkEmptyStatus()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            super.onItemRangeRemoved(positionStart, itemCount)
            checkEmptyStatus()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            super.onItemRangeChanged(positionStart, itemCount, payload)
            Log.d(TAG, "onItemRangeChanged() called with: positionStart = $positionStart, itemCount = $itemCount, payload = $payload")
        }
    }

    init {
        hooks.addAll(Flap.hooks)
        adapterDelegates.addAll(Flap.adapterDelegates)
        registerAdapterDataObserver(dataObserver)
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

    fun unRegisterAdapterDelegate(adapterDelegate: AdapterDelegate<*, *>) {
        adapterDelegates.remove(adapterDelegate)
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
        dispatchOnCreateViewHolderStart(delegate, viewType)
        val component = delegate.onCreateViewHolder(LayoutInflater.from(parent.context), parent, viewType)
        dispatchOnCreateViewHolderEnd(delegate, viewType, component)
        return component
    }

    private fun dispatchOnCreateViewHolderStart(delegate: AdapterDelegate<*, *>?, viewType: Int) {
        hooks.forEach {
            it.onCreateViewHolderStart(delegate, viewType)
        }
    }

    private fun dispatchOnCreateViewHolderEnd(
            delegate: AdapterDelegate<*, *>?,
            viewType: Int,
            component: Component<*>
    ) {
        hooks.forEach {
            it.onCreateViewHolderEnd(delegate, viewType, component)
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
        dispatchOnBindViewHolderStart(delegate, component, data, position, payloads)
        delegate.onBindViewHolder(
                component,
                data,
                position,
                payloads,
                this
        )
        dispatchOnBindViewHolderEnd(delegate, component, data, position, payloads)
    }

    private fun dispatchOnBindViewHolderStart(
            delegate: AdapterDelegate<*, *>,
            component: Component<*>,
            data: Any,
            position: Int,
            payloads: MutableList<Any>
    ) {
        hooks.forEach {
            it.onBindViewHolderStart(delegate, component, data, position, payloads)
        }
    }

    private fun dispatchOnBindViewHolderEnd(
            delegate: AdapterDelegate<*, *>,
            component: Component<*>,
            data: Any,
            position: Int,
            payloads: MutableList<Any>
    ) {
        hooks.forEach {
            it.onBindViewHolderEnd(delegate, component, data, position, payloads)
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

        itemViewType = delegate?.getItemViewType(itemData, position)
                ?: throw DelegateNotFoundException("$position , $itemData ,找不到对应的 AdapterDelegate，请注册")

        if (itemViewType == 0) {
            itemViewType = View.generateViewId()
        }
        viewTypeDelegateMapper[itemViewType] = delegate
        return itemViewType
    }

    private fun getDelegateByComponent(component: Component<*>): AdapterDelegate<*, *> {
        return getDelegateByViewType(component.itemViewType)
    }

    private fun getDelegateByViewType(viewType: Int): AdapterDelegate<*, *> {
        return viewTypeDelegateMapper[viewType] ?: defaultAdapterDelegate
        ?: throw DelegateNotFoundException("${viewType} 找不到对应的 Delegate")
    }

    override fun getItemId(position: Int): Long {
        val itemData = getItemData(position)
        val delegate = getDelegateByViewType(getItemViewType(position))
        return delegate.getItemId(itemData, position)
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
        if (useFlapItemPool) {
            recyclerView.setRecycledViewPool(Flap.globalComponentPool)
        }
    }

    override fun onViewRecycled(holder: Component<*>) {
        holder.onViewRecycled(this)
//        Log.d(TAG, "onViewRecycled() called with: holder = $holder")
    }

    override fun onFailedToRecycleView(holder: Component<*>): Boolean {
//        Log.d(TAG, "onFailedToRecycleView() called with: holder = $holder")
        return holder.onFailedToRecycleView(this)
    }

    override fun onViewAttachedToWindow(holder: Component<*>) {
        holder.onViewAttachedToWindow(this)
//        Log.d(TAG, "onViewAttachedToWindow() called with: holder = $holder")
    }

    override fun onViewDetachedFromWindow(holder: Component<*>) {
        holder.onViewDetachedFromWindow(this)
//        Log.d(TAG, "onViewDetachedFromWindow() called with: holder = $holder")
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
        useFlapItemPool = enable
        return this
    }

    private fun checkEmptyStatus() {
        if (itemCount == 0) {
//            state = UltraRecyclerView.State.Empty
//            notifyDataSetChanged()
        }
    }
}