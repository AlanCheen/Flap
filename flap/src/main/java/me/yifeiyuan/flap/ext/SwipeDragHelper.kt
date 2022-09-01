package me.yifeiyuan.flap.ext

import android.graphics.Canvas
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
            if (viewHolder.isDragEnable()) {
                finalDragFlags = if (viewHolder.getDragFlags() == FLAG_UN_SET) finalDragFlags else 0
            } else {
                finalDragFlags = FLAG_DISABLE
            }
        }

        var finalSwipeFlags = if (swipeFlags != FLAG_UN_SET) swipeFlags else getDefaultSwipeFlags(recyclerView, viewHolder)

        if (viewHolder is ComponentConfig) {
            if (viewHolder.isSwipeEnable()) {
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

    fun onItemMoved(block: OnItemMoveListener): SwipeDragHelper {
        onMoved = block
        return this
    }

    fun onSwipeStarted(block: OnSwipeStartedListener): SwipeDragHelper {
        onSwipeStarted = block
        return this
    }

    fun onSwipeReleased(block: OnSwipeReleasedListener): SwipeDragHelper {
        onSwipedReleased = block
        return this
    }

    fun onDragStarted(block: OnDragStartedListener): SwipeDragHelper {
        onDragStarted = block
        return this
    }

    fun onDragReleased(block: OnDragReleasedListener): SwipeDragHelper {
        onDragReleased = block
        return this
    }

    //Item 被滑动删除了调用
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onSwiped?.invoke(viewHolder.adapterPosition)
        callback.onSwiped(viewHolder.adapterPosition)
    }

    fun onItemSwiped(block: OnItemSwipedListener): SwipeDragHelper {
        onSwiped = block
        return this
    }

    fun attachToRecyclerView(recyclerView: RecyclerView): SwipeDragHelper {
        itemTouchHelper.attachToRecyclerView(recyclerView)
        return this
    }

    var swipedViewHolder: RecyclerView.ViewHolder? = null
    var draggedViewHolder: RecyclerView.ViewHolder? = null

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        if (ItemTouchHelper.ACTION_STATE_SWIPE == actionState) {
            swipedViewHolder = viewHolder
            callback.onSwipeStarted(viewHolder!!, viewHolder.adapterPosition)
            onSwipeStarted?.invoke(viewHolder!!, viewHolder.adapterPosition)
        } else if (ItemTouchHelper.ACTION_STATE_DRAG == actionState) {
            draggedViewHolder = viewHolder
            callback.onDragStarted(viewHolder!!, viewHolder.adapterPosition)
            onDragStarted?.invoke(viewHolder!!, viewHolder.adapterPosition)
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
            //滑动的时候可以绘制背景
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
    fun withDragEnable(enable: Boolean): SwipeDragHelper {
        isDragEnable = enable
        return this
    }

    /**
     * 滑动删除是否可用
     */
    fun withSwipeEnable(enable: Boolean): SwipeDragHelper {
        isSwipeEnable = enable
        return this
    }

    fun withDragFlags(dragFlags: Int): SwipeDragHelper {
        this.dragFlags = dragFlags
        return this
    }

    fun withSwipeFlags(swipeFlags: Int): SwipeDragHelper {
        this.swipeFlags = swipeFlags
        return this
    }

    fun withSwipeThreshold(swipeThreshold: Float): SwipeDragHelper {
        this.swipeThreshold = swipeThreshold
        return this
    }

    fun withDragThreshold(dragThreshold: Float): SwipeDragHelper {
        this.dragThreshold = dragThreshold
        return this
    }

    fun withSwipeBackground(swipeBackground: Drawable): SwipeDragHelper {
        this.swipeBackground = swipeBackground
        return this
    }

    interface Callback {
        fun onSwipeStarted(viewHolder: RecyclerView.ViewHolder, adapterPosition: Int) {}
        fun onSwipeReleased(viewHolder: RecyclerView.ViewHolder, adapterPosition: Int) {}
        fun onSwiped(position: Int)
        fun onDragStarted(viewHolder: RecyclerView.ViewHolder, adapterPosition: Int) {}
        fun onMoved(fromPosition: Int, toPosition: Int)
        fun onDragReleased(viewHolder: RecyclerView.ViewHolder, adapterPosition: Int) {}
    }
}