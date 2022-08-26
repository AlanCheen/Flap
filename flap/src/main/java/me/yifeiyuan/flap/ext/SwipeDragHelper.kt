package me.yifeiyuan.flap.ext

import android.graphics.Canvas
import android.util.Log
import androidx.recyclerview.widget.*
import me.yifeiyuan.flap.FlapDebug

/**
 *
 * 封装处理 滑动删除 和 长按拖放排序 功能
 *
 * Created by 程序亦非猿 on 2022/8/17.
 *
 * @since 3.0.6
 */
class SwipeDragHelper(private val callback: Callback) : ItemTouchHelper.Callback() {

    /**
     * 拖动是否可用
     */
    var isDragEnable = true

    /**
     * 滑动删除是否可用
     */
    var isSwipeEnable = true

    var dragFlags = -1
    var swipeFlags = -1

    var swipeThreshold = 0.5f
    var dragThreshold = 0.5f

    private var onMove: ((fromPosition: Int, toPosition: Int) -> Unit)? = null
    private var onDismiss: ((position: Int) -> Unit)? = null

    private val itemTouchHelper: ItemTouchHelper = ItemTouchHelper(this)

    override fun isLongPressDragEnabled(): Boolean {
        return isDragEnable
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return isSwipeEnable
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val finalDragFlags = if (dragFlags != -1) dragFlags else genDefaultDragFlags(recyclerView, viewHolder)
        val finalSwipeFlags = if (swipeFlags != -1) swipeFlags else genDefaultSwipeFlags(recyclerView, viewHolder)
        return makeMovementFlags(finalDragFlags, finalSwipeFlags)
    }

    private fun genDefaultDragFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return when (recyclerView.layoutManager) {
            is StaggeredGridLayoutManager -> {
                ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            }
            is GridLayoutManager -> {
                ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
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
                ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            }
            is GridLayoutManager -> {
                ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            }
            is LinearLayoutManager -> {
                ItemTouchHelper.START or ItemTouchHelper.END
            }
            else -> {
                ItemTouchHelper.START or ItemTouchHelper.END
            }
        }
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        if (viewHolder.itemViewType == target.itemViewType) {
            onMove?.invoke(viewHolder.adapterPosition, target.adapterPosition)
            return true
        }
        return false
    }

    fun doOnMove(block: (fromPosition: Int, toPosition: Int) -> Unit) {
        onMove = block
    }

    //Item 被滑动删除了调用
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onDismiss?.invoke(viewHolder.adapterPosition)
        callback.onItemDismiss(viewHolder.adapterPosition)
    }

    fun doOnDismiss(block: (position: Int) -> Unit) {
        onDismiss = block
    }

    fun attachToRecyclerView(recyclerView: RecyclerView) {
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return swipeThreshold
    }

    override fun getMoveThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return dragThreshold
    }

    /**
     *  onMove return true 后会调用
     */
    override fun onMoved(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, fromPos: Int, target: RecyclerView.ViewHolder, toPos: Int, x: Int, y: Int) {
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y)
        callback.onItemMoved(fromPos, toPos)
    }

    /**
     * 当用户交互完成后调用，此时动画也结束了，例如滑动删除后、拖放完毕
     * 在 onViewDetachedFromWindow 之后
     */
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    override fun onChildDrawOver(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    interface Callback {
        fun onItemDismiss(position: Int)
        fun onItemMoved(fromPosition: Int, toPosition: Int)
    }
}