package me.yifeiyuan.flapdev.showcase

import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flap.widget.FlapGridLayoutManager
import me.yifeiyuan.flap.widget.FlapLinearLayoutManager
import me.yifeiyuan.flap.widget.FlapRecyclerView
import me.yifeiyuan.flap.widget.FlapStaggeredGridLayoutManager
import me.yifeiyuan.flapdev.R
import me.yifeiyuan.flapdev.components.ZeroHeightModel

private const val TAG = "FlapRecyclerViewTestcas"

/**
 * Created by 程序亦非猿 on 2022/2/21.
 *
 * 测试 FlapRecyclerView 的功能
 */
class FlapRecyclerViewTestcase : BaseTestcaseFragment() {

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

            setData(createRefreshData(30))
        }
    }

    override fun createRefreshData(size: Int): MutableList<Any> {
        return super.createRefreshData(size).apply {
            add(0, ZeroHeightModel())
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
                flapRecyclerView.setData(createRefreshData(0))
            }
            R.id.resetData -> {
                flapRecyclerView.setData(createRefreshData(30))
            }
            R.id.horizontal -> {
                flapRecyclerView.orientation = RecyclerView.HORIZONTAL
            }
            R.id.vertical -> {
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