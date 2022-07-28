package me.yifeiyuan.flap.ext

import android.view.View
import androidx.recyclerview.widget.RecyclerView


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
 */
internal class ItemClicksHelper : RecyclerView.OnChildAttachStateChangeListener {

    lateinit var recyclerView: RecyclerView

    var onItemClickListener: OnItemClickListener? = null

    var onItemLongClickListener: OnItemLongClickListener? = null

    private val internalOnClick = object : View.OnClickListener {
        override fun onClick(v: View) {
            val holder: RecyclerView.ViewHolder = recyclerView.getChildViewHolder(v)
            onItemClickListener?.invoke(recyclerView, v, holder.position)
        }
    }

    private val internalOnLongClick = object : View.OnLongClickListener {
        override fun onLongClick(v: View): Boolean {
            if (onItemLongClickListener == null) {
                return false
            } else {
                val holder: RecyclerView.ViewHolder = recyclerView.getChildViewHolder(v)
                return onItemLongClickListener!!.invoke(recyclerView, v, holder.position)
            }
        }
    }

    override fun onChildViewAttachedToWindow(view: View) {
        onItemClickListener?.let {
            view.setOnClickListener(internalOnClick)
        }

        onItemLongClickListener?.let {
            view.setOnLongClickListener(internalOnLongClick)
        }
    }

    override fun onChildViewDetachedFromWindow(view: View) {
    }

    fun attachRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        this.recyclerView.addOnChildAttachStateChangeListener(this)
    }

    fun detachRecyclerView(recyclerView: RecyclerView) {
        recyclerView.removeOnChildAttachStateChangeListener(this)
    }

}