package me.yifeiyuan.flap.ext

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flap.ComponentConfig
import me.yifeiyuan.flap.hook.AdapterHook


typealias OnItemClickListener = ((recyclerView: RecyclerView, childView: View, position: Int) -> Unit)
typealias OnItemLongClickListener = ((recyclerView: RecyclerView, childView: View, position: Int) -> Boolean)

/**
 * 给 RecyclerView 添加 Component 级别的单击、长按事件的帮助类
 *
 * @see OnItemClickListener
 * @see me.yifeiyuan.flap.FlapAdapter.doOnItemClick
 * @see OnItemLongClickListener
 * @see me.yifeiyuan.flap.FlapAdapter.doOnItemLongClick
 *
 * 实现参考：
 * https://www.littlerobots.nl/blog/Handle-Android-RecyclerView-Clicks/
 * https://stackoverflow.com/questions/24885223/why-doesnt-recyclerview-have-onitemclicklistener
 *
 * Created by 程序亦非猿 on 2022/7/28.
 *
 * @since 3.0.0
 */
internal class ItemClicksHelper : RecyclerView.OnChildAttachStateChangeListener, AdapterHook {

    lateinit var recyclerView: RecyclerView

    var onItemClickListener: OnItemClickListener? = null

    var onItemLongClickListener: OnItemLongClickListener? = null

    private val internalOnClickListener = View.OnClickListener { v ->
        val holder: RecyclerView.ViewHolder = recyclerView.getChildViewHolder(v)
        if (holder is ComponentConfig && holder.isClickable()) {
            onItemClickListener?.invoke(recyclerView, v, holder.position)
        }
    }

    private val internalOnLongClickListener = View.OnLongClickListener { v ->
        val holder: RecyclerView.ViewHolder = recyclerView.getChildViewHolder(v)
        if (holder is ComponentConfig && holder.isLongClickable()) {
            onItemLongClickListener?.invoke(recyclerView, v, holder.position) ?: false
        } else {
            false
        }
    }

    override fun onChildViewAttachedToWindow(view: View) {
        onItemClickListener?.let {
            view.setOnClickListener(internalOnClickListener)
        }

        onItemLongClickListener?.let {
            view.setOnLongClickListener(internalOnLongClickListener)
        }
    }

    override fun onChildViewDetachedFromWindow(view: View) {
        //do nothing
    }

    override fun onAdapterAttachedToRecyclerView(adapter: RecyclerView.Adapter<*>, recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        this.recyclerView.addOnChildAttachStateChangeListener(this)
    }

    override fun onAdapterDetachedFromRecyclerView(adapter: RecyclerView.Adapter<*>, recyclerView: RecyclerView) {
        recyclerView.removeOnChildAttachStateChangeListener(this)
    }
}