package me.yifeiyuan.flap.ext

import androidx.recyclerview.widget.*
import me.yifeiyuan.flap.FlapAdapter

/**
 *
 * 封装处理 滑动删除 和 长按拖放排序 功能
 *
 * Created by 程序亦非猿 on 2022/8/17.
 *
 * @since 3.0.6
 */
class FlapItemTouchHelper(private val adapter: FlapAdapter) : ItemTouchHelper.Callback() {

    var isLongPressDragEnable = true
    var isSwipeEnable = true

    private val itemTouchHelper: ItemTouchHelper = ItemTouchHelper(this)

    override fun isLongPressDragEnabled(): Boolean {
        return isLongPressDragEnable
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return isSwipeEnable
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = genDefaultDragFlags(recyclerView, viewHolder)
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    private fun genDefaultDragFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return when (recyclerView.layoutManager) {
            is StaggeredGridLayoutManager -> {
                ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END
            }
            is GridLayoutManager -> {
                ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END
            }
            is LinearLayoutManager -> {
                ItemTouchHelper.UP or ItemTouchHelper.DOWN
            }
            else -> {
                ItemTouchHelper.UP or ItemTouchHelper.DOWN
            }
        }
    }

    private fun genDefaultSwipeFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return when (recyclerView.layoutManager) {
            is StaggeredGridLayoutManager -> {
                ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END
            }
            is GridLayoutManager -> {
                ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END
            }
            is LinearLayoutManager -> {
                ItemTouchHelper.UP or ItemTouchHelper.DOWN
            }
            else -> {
                ItemTouchHelper.START or ItemTouchHelper.END
            }
        }
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        if (viewHolder.itemViewType == target.itemViewType) {
            adapter.swapData(viewHolder.adapterPosition, target.adapterPosition)
            return true
        }
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        adapter.removeDataAt(viewHolder.adapterPosition)
    }

    fun attachToRecyclerView(recyclerView: RecyclerView) {
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun canDropOver(recyclerView: RecyclerView, current: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return super.canDropOver(recyclerView, current, target)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
    }
}