package me.yifeiyuan.flap.ext

import androidx.recyclerview.widget.RecyclerView

/**
 * Created by 程序亦非猿 on 2022/8/12.
 *
 * @since 3.0.1
 */
abstract class OnAdapterDataChangedObserver : RecyclerView.AdapterDataObserver() {

    abstract fun onDataChanged()

    override fun onChanged() {
        super.onChanged()
        onDataChanged()
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
        onDataChanged()
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
        onDataChanged()
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        onDataChanged()
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        onDataChanged()
    }

    override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
        onDataChanged()
    }

    override fun onStateRestorationPolicyChanged() {
    }
}