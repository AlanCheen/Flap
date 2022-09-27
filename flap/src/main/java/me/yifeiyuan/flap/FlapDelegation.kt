package me.yifeiyuan.flap

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flap.delegate.AdapterDelegate
import me.yifeiyuan.flap.delegate.AdapterDelegateManager
import me.yifeiyuan.flap.delegate.IAdapterDelegateManager
import me.yifeiyuan.flap.hook.AdapterHookManager
import me.yifeiyuan.flap.hook.IAdapterHookManager
import me.yifeiyuan.flap.service.AdapterServiceManager
import me.yifeiyuan.flap.service.IAdapterServiceManager

/**
 * Created by 程序亦非猿 on 2022/9/27.
 */

class FlapDelegation : IAdapterHookManager by AdapterHookManager(), IAdapterDelegateManager by AdapterDelegateManager(), IAdapterServiceManager by AdapterServiceManager() {

    companion object {
        private const val TAG = "FlapDelegation"

        /**
         * 当 Adapter.data 中存在一个 Model 没有对应的 AdapterDelegate.delegate()==true 时抛出
         */
        internal class AdapterDelegateNotFoundException(errorMessage: String) : Exception(errorMessage)
    }

    private val viewTypeDelegateCache: MutableMap<Int, AdapterDelegate<*, *>?> = mutableMapOf()
    private val delegateViewTypeCache: MutableMap<AdapterDelegate<*, *>, Int> = mutableMapOf()

    var fallbackDelegate: AdapterDelegate<*, *>? = null

    internal fun onCreateViewHolder(adapter: FlapAdapter, parent: ViewGroup, viewType: Int, layoutInflater: LayoutInflater): Component<*> {
        val delegate = getDelegateByViewType(viewType)
        dispatchOnCreateViewHolderStart(adapter, delegate, viewType)
        val component = delegate.onCreateViewHolder(layoutInflater, parent, viewType)
        dispatchOnCreateViewHolderEnd(adapter, delegate, viewType, component)
        return component
    }

    private fun dispatchOnCreateViewHolderStart(adapter: FlapAdapter, delegate: AdapterDelegate<*, *>, viewType: Int) {
        adapterHooks.forEach {
            it.onCreateViewHolderStart(adapter, delegate, viewType)
        }
    }

    private fun dispatchOnCreateViewHolderEnd(
            adapter: FlapAdapter,
            delegate: AdapterDelegate<*, *>,
            viewType: Int,
            component: Component<*>
    ) {
        adapterHooks.forEach {
            it.onCreateViewHolderEnd(adapter, delegate, viewType, component)
        }
    }

    private fun getDelegateByViewType(viewType: Int): AdapterDelegate<*, *> {
        return viewTypeDelegateCache[viewType] ?: fallbackDelegate
        ?: throw AdapterDelegateNotFoundException("找不到 viewType = $viewType 对应的 Delegate，请先注册，或设置默认的 Delegate")
    }

    internal fun onBindViewHolder(
            adapter: FlapAdapter,
            itemData: Any,
            component: Component<*>,
            position: Int,
            payloads: MutableList<Any>
    ) {
        try {
            val delegate = getDelegateByViewType(component.itemViewType)
            dispatchOnBindViewHolderStart(adapter, delegate, component, itemData, position, payloads)
            delegate.onBindViewHolder(
                    component,
                    itemData,
                    position,
                    payloads,
                    adapter
            )
            dispatchOnBindViewHolderEnd(adapter, delegate, component, itemData, position, payloads)
        } catch (e: Exception) {
            e.printStackTrace()
            FlapDebug.e(TAG, "onBindViewHolder: Error = ", e)
        }
    }

    private fun dispatchOnBindViewHolderStart(
            adapter: FlapAdapter,
            delegate: AdapterDelegate<*, *>,
            component: Component<*>,
            itemData: Any,
            position: Int,
            payloads: MutableList<Any>
    ) {
        adapterHooks.forEach {
            it.onBindViewHolderStart(adapter, delegate, component, itemData, position, payloads)
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
        adapterHooks.forEach {
            it.onBindViewHolderEnd(adapter, delegate, component, data, position, payloads)
        }
    }

    internal fun getItemViewType(position: Int, itemData: Any): Int {

        var itemViewType: Int

        val delegate: AdapterDelegate<*, *>? = adapterDelegates.firstOrNull {
            it.delegate(itemData)
        } ?: fallbackDelegate

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

    internal fun getItemId(position: Int, itemData: Any): Long {
        val delegate = getDelegateByViewType(getItemViewType(position, itemData))
        return delegate.getItemId(itemData, position)
    }

    internal fun onViewRecycled(adapter: FlapAdapter, component: Component<*>) {
        val delegate = getDelegateByViewType(component.itemViewType)
        delegate.onViewRecycled(adapter, component)
    }

    internal fun onFailedToRecycleView(adapter: FlapAdapter, component: Component<*>): Boolean {
        val delegate = getDelegateByViewType(component.itemViewType)
        return delegate.onFailedToRecycleView(adapter, component)
    }

    internal fun onViewAttachedToWindow(adapter: FlapAdapter, component: Component<*>) {
        val delegate = getDelegateByViewType(component.itemViewType)
        delegate.onViewAttachedToWindow(adapter, component)
        adapterHooks.forEach {
            it.onViewAttachedToWindow(adapter, delegate, component)
        }
    }

    internal fun onViewDetachedFromWindow(adapter: FlapAdapter, component: Component<*>) {
        val delegate = getDelegateByViewType(component.itemViewType)
        delegate.onViewDetachedFromWindow(adapter, component)
        adapterHooks.forEach {
            it.onViewDetachedFromWindow(adapter, delegate, component)
        }
    }

    internal fun onAttachedToRecyclerView(adapter: FlapAdapter, recyclerView: RecyclerView) {
        //do nothing
    }

    internal fun onDetachedFromRecyclerView(adapter: FlapAdapter, recyclerView: RecyclerView) {
        //do nothing
    }
}