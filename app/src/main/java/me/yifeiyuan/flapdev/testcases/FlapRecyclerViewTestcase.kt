package me.yifeiyuan.flapdev.testcases

import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flap.decoration.LinearSpaceItemDecoration
import me.yifeiyuan.flap.widget.FlapGridLayoutManager
import me.yifeiyuan.flap.widget.FlapLinearLayoutManager
import me.yifeiyuan.flap.widget.FlapRecyclerView
import me.yifeiyuan.flap.widget.FlapStaggeredGridLayoutManager
import me.yifeiyuan.flapdev.R
import me.yifeiyuan.flapdev.toPixel

private const val TAG = "FlapRecyclerViewTestcas"

/**
 *
 * 测试 FlapRecyclerView 的功能
 *
 * Created by 程序亦非猿 on 2022/2/21.
 */
class FlapRecyclerViewTestcase : BaseTestcaseFragment() {

    private lateinit var linearSpaceItemDecoration : LinearSpaceItemDecoration

    override fun getLayoutId(): Int {
        return R.layout.fragment_case_flap_rv
    }

    private lateinit var flapRecyclerView: FlapRecyclerView

    override fun onInit(view: View) {
        super.onInit(view)
        recyclerView = view.findViewById(R.id.recyclerView)

        flapRecyclerView = recyclerView as FlapRecyclerView
        adapter = flapRecyclerView.adapter as FlapAdapter

        setHasOptionsMenu(true)

        with(flapRecyclerView.flapAdapter) {

            this.setEmptyView(emptyView)

            doOnPreload {
                toast("onPreload 预加载")
                loadMoreData(10)
            }

            doOnItemClick { recyclerView, childView, position ->
                Log.d(TAG, "doOnItemClick called with: childView = $childView, position = $position")
                val component = recyclerView.getChildViewHolder(childView)
                toast("点击了 position = $position，model=${adapter.getItemData(position)}")
            }

            doOnItemLongClick { recyclerView, childView, position ->
                Log.d(TAG, "doOnItemLongClick called with: childView = $childView, position = $position")
                toast("长按了 position = $position")
                true
            }

            setDataAndNotify(createRefreshData(30))
        }

        linearSpaceItemDecoration = LinearSpaceItemDecoration(requireActivity().toPixel(6))
        recyclerView.addItemDecoration(linearSpaceItemDecoration)
    }

    override fun createRefreshData(size: Int): MutableList<Any> {
        return super.createRefreshData(size).apply {
            // 测试第一个 item 是 高度=0 的组件是否影响下拉刷新，结果应该是：依然能够下拉刷新。
//            add(0, ZeroHeightModel())
        }
    }

    override fun refreshData(size: Int) {
        flapRecyclerView.setData(createRefreshData(size))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.flap, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.emptyData -> {
                flapRecyclerView.setData(mutableListOf())
            }
            R.id.resetData -> {
                flapRecyclerView.setData(createRefreshData(30))
            }
            R.id.horizontal -> {
                linearSpaceItemDecoration.orientation = RecyclerView.HORIZONTAL
                flapRecyclerView.orientation = RecyclerView.HORIZONTAL
            }
            R.id.vertical -> {
                linearSpaceItemDecoration.orientation = RecyclerView.VERTICAL
                flapRecyclerView.orientation = RecyclerView.VERTICAL
            }
            R.id.linear -> {
                flapRecyclerView.layoutManager = FlapLinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
            }
            R.id.grid -> {
                val spanCount = 2
                flapRecyclerView.layoutManager = FlapGridLayoutManager(requireActivity(), spanCount, RecyclerView.VERTICAL, false).apply {
                    spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                        override fun getSpanSize(position: Int): Int {
                            var spanSize = 1
                            spanSize = if (position % 2 == 0) 2 else 1
                            return spanSize
                        }
                    }
                }
            }
            R.id.staggered -> {
                flapRecyclerView.layoutManager = FlapStaggeredGridLayoutManager(3).apply {
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}