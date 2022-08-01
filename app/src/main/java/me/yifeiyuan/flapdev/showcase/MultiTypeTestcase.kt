package me.yifeiyuan.flapdev.showcase

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flap.widget.FlapGridLayoutManager
import me.yifeiyuan.flap.widget.FlapLinearLayoutManager
import me.yifeiyuan.flap.widget.FlapStaggeredGridLayoutManager
import me.yifeiyuan.flapdev.HeaderFooterAdapter
import me.yifeiyuan.flapdev.R
import me.yifeiyuan.flapdev.mockMultiTypeModels

/**
 * 多类型测试
 */
class MultiTypeTestcase : BaseCaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun createRefreshData(size: Int): MutableList<Any> {
        return mockMultiTypeModels()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.multitype, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.emptyData -> {
                adapter.setData(mutableListOf())
            }
            R.id.resetData -> {
                adapter.setData(mockMultiTypeModels())
            }
            R.id.linear -> {
                recyclerView.layoutManager = FlapLinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
            }
            R.id.grid -> {
                val spanCount = 2
                recyclerView.layoutManager = FlapGridLayoutManager(requireActivity(), spanCount, RecyclerView.VERTICAL, false).apply {

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
                recyclerView.layoutManager = FlapStaggeredGridLayoutManager(3).apply {

                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}