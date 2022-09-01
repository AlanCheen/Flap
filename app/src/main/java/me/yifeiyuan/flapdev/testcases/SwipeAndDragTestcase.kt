package me.yifeiyuan.flapdev.testcases

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flap.ext.SwipeDragHelper


private const val TAG = "ItemClicksTestcase"

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
                .withDragFlags(ItemTouchHelper.UP or ItemTouchHelper.DOWN)
                .withSwipeFlags(ItemTouchHelper.START or ItemTouchHelper.END)
                .withSwipeBackground(ColorDrawable(Color.parseColor("#ff0000")))
                .onItemSwiped {
                    toast("滑动删除了一个 item , position=$it")
                }
                .onItemMoved { fromPosition, toPosition ->
                    toast("移动交换了 $fromPosition to $toPosition")
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