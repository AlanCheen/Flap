package me.yifeiyuan.flapdev.showcase

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flap.widget.FlapGridLayoutManager
import me.yifeiyuan.flap.widget.FlapLinearLayoutManager
import me.yifeiyuan.flap.widget.FlapRecyclerView
import me.yifeiyuan.flap.widget.FlapStaggeredGridLayoutManager
import me.yifeiyuan.flapdev.R

/**
 * Created by 程序亦非猿 on 2022/2/21.
 *
 * 测试 FlapRecyclerView 的功能
 */
class FlapRecyclerViewTestcase : BaseCaseFragment() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_case_flap_rv
    }

    private lateinit var flapRecyclerView: FlapRecyclerView

    override fun onInit(view: View) {
        recyclerView = view.findViewById(R.id.recyclerView)

        flapRecyclerView = recyclerView as FlapRecyclerView
        adapter = flapRecyclerView.adapter as FlapAdapter

        flapRecyclerView.setData(createRefreshData(30))

        val emptyView = view.findViewById<View>(R.id.emptyView)

        flapRecyclerView.emptyView = emptyView

        setHasOptionsMenu(true)

        flapRecyclerView.doOnPrefetch {
            toast("on prefetch")
            loadMoreData(10)
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