package me.yifeiyuan.flapdev.testcases

import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flap.ext.SwipeDragHelper
import me.yifeiyuan.flap.skeleton.Skeleton
import me.yifeiyuan.flap.widget.FlapGridLayoutManager
import me.yifeiyuan.flap.widget.FlapIndexedStaggeredGridLayoutManager
import me.yifeiyuan.flap.widget.FlapLinearLayoutManager
import me.yifeiyuan.flap.widget.FlapStaggeredGridLayoutManager
import me.yifeiyuan.flapdev.R
import me.yifeiyuan.flapdev.components.ZeroHeightModel
import me.yifeiyuan.flapdev.mockMultiTypeModels

/**
 * 多类型测试
 */
class MultiTypeTestcase : BaseTestcaseFragment() {

    companion object {
        private const val TAG = "MultiTypeTestcase"
    }

    lateinit var skeletonHelper: Skeleton

    override fun onInit(view: View) {
        super.onInit(view)
        setHasOptionsMenu(true)

        skeletonHelper = Skeleton()
                .bind(recyclerView)
                .adapter(adapter)
                .autoHide(true)
                .count(10)
                .shimmer(true)
//                .layout(R.layout.skeleton_layout) // 单个资源
                .layouts { //多资源
                    when (it % 3) {
                        0 -> {
                            R.layout.skeleton_layout
                        }
                        1 -> {
                            R.layout.skeleton_layout2
                        }
                        2 -> {
                            R.layout.skeleton_layout3
                        }
                        else -> {
                            R.layout.skeleton_layout
                        }
                    }
                }
                .onlyOnce(false)
                .withEmptyViewHelper(adapter.emptyViewHelper)
                .show()

        SwipeDragHelper(adapter)
                .withDragEnable(true)
                .withSwipeEnable(true)
//                .forVerticalList()
//                .forHorizontalList()
//                .forGrid()
                .withDragFlags(ItemTouchHelper.UP or ItemTouchHelper.DOWN)
                .withSwipeFlags(ItemTouchHelper.START or ItemTouchHelper.END)
                .withSwipeBackground(ColorDrawable(Color.parseColor("#ff0000")))
                .onItemSwiped {
                    toast("滑动删除了一个 item , position=$it")
                }
                .onItemMoved { fromPosition, toPosition ->
                    toast("移动交换了 $fromPosition to $toPosition")
                }
                .onDragStarted { viewHolder, adapterPosition ->
                    Log.d(TAG, "开始拖动 position=$adapterPosition")
                    toast("开始拖动 position=$adapterPosition")

                    //做个放大动画
                    val scaleY = ObjectAnimator.ofFloat(viewHolder.itemView, "scaleY", 1f, 1.5f)
                    scaleY.start()
                }
                .onDragReleased { viewHolder, adapterPosition ->
                    Log.d(TAG, "拖动结束 position=$adapterPosition")
                    toast("拖动结束 position=$adapterPosition")

                    //恢复原状
                    val scaleY = ObjectAnimator.ofFloat(viewHolder.itemView, "scaleY", 1f)
                    scaleY.start()
                }
                .onSwipeStarted { viewHolder, adapterPosition ->
                    Log.d(TAG, "滑动开始 position=$adapterPosition")
                    toast("滑动开始 position=$adapterPosition")
                }
                .onSwipeReleased { viewHolder, adapterPosition ->
                    Log.d(TAG, "滑动结束 position=$adapterPosition")
                    toast("滑动结束 position=$adapterPosition")
                }
                .onClearView { viewHolder, adapterPosition ->
                    Log.d(TAG, "onClearView called position=$adapterPosition")
                }
                .attachToRecyclerView(recyclerView)


        recyclerView.addItemDecoration(linearItemDecoration)
    }

    override fun updateSkeletonVisibility(show: Boolean) {
        if (show) {
            skeletonHelper.show()
        } else {
            skeletonHelper.hide()
        }
    }

    override fun createRefreshData(size: Int): MutableList<Any> {
        val list = mockMultiTypeModels()
        return list
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.multitype, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
        }
        return super.onOptionsItemSelected(item)
    }
}