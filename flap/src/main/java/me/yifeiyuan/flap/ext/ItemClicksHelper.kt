package me.yifeiyuan.flap.ext

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flap.ComponentConfig


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
internal class ItemClicksHelper : RecyclerView.OnChildAttachStateChangeListener {

    lateinit var recyclerView: RecyclerView

    var onItemClickListener: OnItemClickListener? = null

    var onItemLongClickListener: OnItemLongClickListener? = null

    private val internalOnClickListener = View.OnClickListener { v ->
        val holder: RecyclerView.ViewHolder = recyclerView.getChildViewHolder(v)
        onItemClickListener?.invoke(recyclerView, v, holder.position)
    }

    private val internalOnLongClickListener = View.OnLongClickListener { v ->
        val holder: RecyclerView.ViewHolder = recyclerView.getChildViewHolder(v)
        onItemLongClickListener?.invoke(recyclerView, v, holder.position) ?: false
    }

    override fun onChildViewAttachedToWindow(view: View) {
        val holder: RecyclerView.ViewHolder = recyclerView.getChildViewHolder(view)

        onItemClickListener?.let {
            if (holder is ComponentConfig) {
                if (holder.isClickable()) {
                    view.setOnClickListener(internalOnClickListener)
                } else {
                    view.setOnClickListener(null)
                }
            } else {
                view.setOnClickListener(internalOnClickListener)
            }
        }

        onItemLongClickListener?.let {
            if (holder is ComponentConfig) {
                if (holder.isLongClickable()) {
                    view.setOnLongClickListener(internalOnLongClickListener)
                } else {
                    view.setOnLongClickListener(null)
                }
            } else {
                view.setOnLongClickListener(internalOnLongClickListener)
            }
        }
    }

    override fun onChildViewDetachedFromWindow(view: View) {
        //do nothing
    }

    fun attachRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        this.recyclerView.addOnChildAttachStateChangeListener(this)
    }

    fun detachRecyclerView(recyclerView: RecyclerView) {
        recyclerView.removeOnChildAttachStateChangeListener(this)
    }

}