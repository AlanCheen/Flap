package me.yifeiyuan.flapdev.testcases

import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flap.ext.SwipeDragHelper


private const val TAG = "SwipeAndDragTestcase"

/**
 * 测试滑动删除&拖放
 *
 * Created by 程序亦非猿 on 2022/8/17.
 */
class SwipeAndDragTestcase : BaseTestcaseFragment() {

    override fun onInit(view: View) {
        super.onInit(view)

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

        recyclerView.addItemDecoration(spaceItemDecoration)
    }

    override fun createAdapter(): FlapAdapter {
        return super.createAdapter().apply {
            doOnItemClick { recyclerView, childView, position ->
                Log.d(TAG, "doOnItemClick called with: childView = $childView, position = $position")
                toast("点击了 position = $position")
            }

            doOnItemLongClick { recyclerView, childView, position ->
                Log.d(TAG, "doOnItemLongClick called with: childView = $childView, position = $position")
                toast("长按了 position = $position")
                false
            }
        }
    }
}