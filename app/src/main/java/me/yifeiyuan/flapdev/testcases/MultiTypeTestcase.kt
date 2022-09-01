package me.yifeiyuan.flapdev.testcases

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import me.yifeiyuan.flap.widget.FlapLinearLayoutManager
import me.yifeiyuan.flap.widget.FlapStaggeredGridLayoutManager
import me.yifeiyuan.flapdev.R
import me.yifeiyuan.flapdev.components.ZeroHeightModel
import me.yifeiyuan.flapdev.mockMultiTypeModels

/**
 * 多类型测试
 */
class MultiTypeTestcase : BaseTestcaseFragment() {

    //测试第一个组件高度为 0 的 case,会导致不能下拉刷新
    var testZeroHeight = false
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

        recyclerView.addItemDecoration(linearItemDecoration)
    }

    override fun createRefreshData(size: Int): MutableList<Any> {
        val list = mockMultiTypeModels()
        if (testZeroHeight) {
            list.add(0, ZeroHeightModel())
        }
        return list
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.multitype, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.emptyData -> {
                skeletonHelper.hide()
                adapter.setDataAndNotify(mutableListOf())
            }
            R.id.resetData -> {
                adapter.setDataAndNotify(mockMultiTypeModels())
            }
            R.id.linear -> {
                recyclerView.layoutManager = FlapLinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
                recyclerView.invalidateItemDecorations()
            }
            R.id.grid -> {
                val spanCount = 3
                recyclerView.layoutManager = FlapGridLayoutManager(requireActivity(), spanCount, RecyclerView.VERTICAL, false).apply {

                    spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                        override fun getSpanSize(position: Int): Int {
                            var spanSize = 1
                            spanSize = if (position % 2 == 0) 2 else 1
                            return spanSize
                        }
                    }
                }
                recyclerView.invalidateItemDecorations()
            }
            R.id.staggered -> {
                recyclerView.layoutManager = FlapStaggeredGridLayoutManager(3)
                recyclerView.invalidateItemDecorations()
            }
            R.id.testZeroHeight -> {
                testZeroHeight = !testZeroHeight
                onRefresh()
            }
            R.id.toggleSkeleton -> {
                if (skeletonHelper.isShowing) {
                    skeletonHelper.hide()
                } else {
                    skeletonHelper.show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}