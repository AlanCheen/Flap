package me.yifeiyuan.flap.ext

import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.*
import me.yifeiyuan.flap.ComponentConfig


typealias  OnItemMoveListener = (fromPosition: Int, toPosition: Int) -> Unit
/**
 * item 被删除
 */
typealias  OnItemSwipedListener = (position: Int) -> Unit
typealias  OnSwipeStartedListener = (viewHolder: RecyclerView.ViewHolder, adapterPosition: Int) -> Unit
/**
 * 一次滑动手势释放
 * 但是不代表被删除
 */
typealias  OnSwipeReleasedListener = (viewHolder: RecyclerView.ViewHolder, adapterPosition: Int) -> Unit
typealias  OnDragStartedListener = (viewHolder: RecyclerView.ViewHolder, adapterPosition: Int) -> Unit
typealias  OnDragReleasedListener = (viewHolder: RecyclerView.ViewHolder, adapterPosition: Int) -> Unit

/**
 *
 * 封装处理 滑动删除 和 长按拖放排序 功能
 *
 * Created by 程序亦非猿 on 2022/8/17.
 *
 * @since 3.0.6
 */
class SwipeDragHelper(private val callback: Callback) : ItemTouchHelper.Callback() {

    companion object {
        const val FLAG_DISABLE = 0
        const val FLAG_UN_SET = -1
    }

    /**
     * 拖动是否可用
     */
    private var isDragEnable = true

    /**
     * 滑动删除是否可用
     */
    private var isSwipeEnable = true

    private var dragFlags = FLAG_UN_SET
    private var swipeFlags = FLAG_UN_SET

    private var swipeThreshold = 0.5f
    private var dragThreshold = 0.5f

    private var onMoved: OnItemMoveListener? = null
    private var onSwiped: OnItemSwipedListener? = null

    private var onSwipeStarted: OnSwipeStartedListener? = null
    private var onSwipedReleased: OnSwipeReleasedListener? = null

    private var onDragStarted: OnDragStartedListener? = null
    private var onDragReleased: OnDragReleasedListener? = null

    private var swipeBackground: Drawable? = null

    private val itemTouchHelper: ItemTouchHelper = ItemTouchHelper(this)

    override fun isLongPressDragEnabled(): Boolean {
        return isDragEnable
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return isSwipeEnable
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        var finalDragFlags = if (dragFlags != FLAG_UN_SET) dragFlags else getDefaultDragFlags(recyclerView, viewHolder)
        if (viewHolder is ComponentConfig) {
            if (viewHolder.isDragEnabled()) {
                finalDragFlags = if (viewHolder.getDragFlags() == FLAG_UN_SET) finalDragFlags else 0
            } else {
                finalDragFlags = FLAG_DISABLE
            }
        }

        var finalSwipeFlags = if (swipeFlags != FLAG_UN_SET) swipeFlags else getDefaultSwipeFlags(recyclerView, viewHolder)

        if (viewHolder is ComponentConfig) {
            if (viewHolder.isSwipeEnabled()) {
                finalSwipeFlags = if (viewHolder.getSwipeFlags() == FLAG_UN_SET) finalSwipeFlags else 0
            } else {
                finalSwipeFlags = FLAG_DISABLE
            }
        }
        return makeMovementFlags(finalDragFlags, finalSwipeFlags)
    }

    /**
     * 根据 layoutManager 提供默认 dragFlags
     */
    private fun getDefaultDragFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
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

    /**
     * 根据 layoutManager 提供默认 swipeFlags
     */
    private fun getDefaultSwipeFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
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
            onMoved?.invoke(viewHolder.adapterPosition, target.adapterPosition)
            return true
        }
        return false
    }

    fun onItemMoved(listener: OnItemMoveListener) = apply {
        onMoved = listener
    }

    fun onSwipeStarted(listener: OnSwipeStartedListener)= apply {
        onSwipeStarted = listener
    }

    fun onSwipeReleased(listener: OnSwipeReleasedListener)= apply {
        onSwipedReleased = listener
    }

    fun onDragStarted(listener: OnDragStartedListener)= apply {
        onDragStarted = listener
    }

    fun onDragReleased(listener: OnDragReleasedListener)= apply {
        onDragReleased = listener
    }

    //Item 被滑动删除了调用
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onSwiped?.invoke(viewHolder.adapterPosition)
        callback.onSwiped(viewHolder.adapterPosition)
    }

    fun onItemSwiped(listener: OnItemSwipedListener)= apply {
        onSwiped = listener
    }

    fun attachToRecyclerView(recyclerView: RecyclerView)= apply {
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    var swipedViewHolder: RecyclerView.ViewHolder? = null
    var draggedViewHolder: RecyclerView.ViewHolder? = null

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        if (ItemTouchHelper.ACTION_STATE_SWIPE == actionState) {
            swipedViewHolder = viewHolder
            callback.onSwipeStarted(viewHolder!!, viewHolder.adapterPosition)
            onSwipeStarted?.invoke(viewHolder, viewHolder.adapterPosition)
        } else if (ItemTouchHelper.ACTION_STATE_DRAG == actionState) {
            draggedViewHolder = viewHolder
            callback.onDragStarted(viewHolder!!, viewHolder.adapterPosition)
            onDragStarted?.invoke(viewHolder, viewHolder.adapterPosition)
        } else if (ItemTouchHelper.ACTION_STATE_IDLE == actionState) {
            if (draggedViewHolder != null) {
                callback.onDragReleased(draggedViewHolder!!, draggedViewHolder!!.adapterPosition)
                onDragReleased?.invoke(draggedViewHolder!!, draggedViewHolder!!.adapterPosition)
                draggedViewHolder = null
            } else if (swipedViewHolder != null) {
                callback.onSwipeReleased(swipedViewHolder!!, swipedViewHolder!!.adapterPosition)
                onSwipedReleased?.invoke(swipedViewHolder!!, swipedViewHolder!!.adapterPosition)
                swipedViewHolder = null
            }
        }
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
        callback.onMoved(fromPos, toPos)
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
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            //滑动删除的时候可以绘制背景
            swipeBackground?.let {
                val itemView = viewHolder.itemView
                if (dX > 0) {
                    it.setBounds(itemView.left, itemView.top, itemView.right.coerceAtMost(itemView.left + dX.toInt()), itemView.bottom)
                    it.draw(c)
                } else {
                    it.setBounds(itemView.left.coerceAtLeast(itemView.right + dX.toInt()), itemView.top, itemView.right, itemView.bottom)
                    it.draw(c)
                }
            }
        } else if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            //拖动
        }
    }

    override fun onChildDrawOver(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    /**
     * 拖动是否可用
     */
    fun withDragEnable(enable: Boolean)= apply {
        isDragEnable = enable
    }

    /**
     * 滑动删除是否可用
     */
    fun withSwipeEnable(enable: Boolean)= apply {
        isSwipeEnable = enable
    }

    /**
     * 快捷设置 dragFlags 为垂直方向 up or down
     */
    fun forVerticalList()= apply {
        this.dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
    }

    /**
     * 快捷设置 dragFlags 为水平方向 left or right
     */
    fun forHorizontalList()= apply {
        this.dragFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    }

    /**
     * 快捷设置 dragFlags 为全方向
     */
    fun forGrid()= apply {
        this.dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    }

    fun withDragFlags(dragFlags: Int)= apply {
        this.dragFlags = dragFlags
    }

    fun withSwipeFlags(swipeFlags: Int)= apply {
        this.swipeFlags = swipeFlags
    }

    fun withSwipeThreshold(swipeThreshold: Float)= apply {
        this.swipeThreshold = swipeThreshold
    }

    fun withDragThreshold(dragThreshold: Float)= apply {
        this.dragThreshold = dragThreshold
    }

    /**
     * 设置被滑动的 item 的背景
     */
    fun withSwipeBackground(swipeBackground: Drawable)= apply {
        this.swipeBackground = swipeBackground
    }

    fun withSwipeBackgroundColor(color: Int)= apply {
        this.swipeBackground = ColorDrawable(color)
    }

    interface Callback {
        fun onSwipeStarted(viewHolder: RecyclerView.ViewHolder, adapterPosition: Int) {}
        fun onSwipeReleased(viewHolder: RecyclerView.ViewHolder, adapterPosition: Int) {}
        fun onSwiped(position: Int) {}
        fun onDragStarted(viewHolder: RecyclerView.ViewHolder, adapterPosition: Int) {}
        fun onMoved(fromPosition: Int, toPosition: Int) {}
        fun onDragReleased(viewHolder: RecyclerView.ViewHolder, adapterPosition: Int) {}
    }
}