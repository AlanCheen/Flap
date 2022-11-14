package me.yifeiyuan.flap.ext

import androidx.recyclerview.widget.RecyclerView

/**
 * 只关心数据有变化，不关心变化的细节。
 *
 * Created by 程序亦非猿 on 2022/8/12.
 *
 * @since 3.0.1
 */
abstract class OnAdapterDataChangedObserver : RecyclerView.AdapterDataObserver() {

    /**
     * 当 Adapter 的数据发生任何变化都会回调
     */
    abstract fun onAdapterDataChanged()

    override fun onChanged() {
        super.onChanged()
        onAdapterDataChanged()
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
        onAdapterDataChanged()
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
        onAdapterDataChanged()
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        onAdapterDataChanged()
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        onAdapterDataChanged()
    }

    override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
        onAdapterDataChanged()
    }

    override fun onStateRestorationPolicyChanged() {}
}