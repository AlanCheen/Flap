package me.yifeiyuan.flapdev.testcases

import android.graphics.Color
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.*
import me.yifeiyuan.flap.decoration.*
import me.yifeiyuan.flap.widget.FlapGridLayoutManager
import me.yifeiyuan.flap.widget.FlapLinearLayoutManager
import me.yifeiyuan.flap.widget.FlapStaggeredGridLayoutManager
import me.yifeiyuan.flapdev.R
import me.yifeiyuan.flapdev.toPixel

/**
 * Created by 程序亦非猿 on 2022/8/18.
 */

class ItemDecorationTestcase : BaseTestcaseFragment() {

    lateinit var linearItemDecoration: RecyclerView.ItemDecoration
    lateinit var gridItemDecoration: RecyclerView.ItemDecoration

    lateinit var currentItemDecoration: RecyclerView.ItemDecoration
    lateinit var currentLM: RecyclerView.LayoutManager

    lateinit var linearLayoutManager: FlapLinearLayoutManager
    lateinit var gridLayoutManager: FlapGridLayoutManager
    lateinit var staggeredGridLayoutManager: FlapStaggeredGridLayoutManager

    override fun onInit(view: View) {
        super.onInit(view)
        setHasOptionsMenu(true)

        val drawable = ContextCompat.getDrawable(requireActivity(), R.drawable.linear_item_decoration)

        //--1
//        linearItemDecoration = LinearItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL)
//        linearItemDecoration.setDrawable(drawable!!)
        //--1

        //--2
        linearItemDecoration = LinearItemDecoration(requireActivity().toPixel(6), Color.BLUE)
                .withFirstItemTopEdge(true)
                .withLastItemBottomEdge(true)

        //--2

        //--3
//        linearItemDecoration = LinearItemDecoration(requireActivity(), resources.getDimensionPixelSize(R.dimen.item_decoration_size), Color.parseColor("#ff0000"))
//        linearItemDecoration = LinearItemDecoration(requireActivity(), resources.getDimensionPixelSize(R.dimen.item_decoration_size))
        //--3

//        linearItemDecoration = LinearSpaceItemDecoration(requireActivity().toPixel(6)).apply {
//            isIncludeFirstItemTopSpace = false
//            isIncludeLastItemBottomSpace = false
//        }

//        gridItemDecoration = GridItemDecoration(drawable!!)

        gridItemDecoration = GridSpaceItemDecoration(requireActivity().toPixel(6))

        currentItemDecoration = linearItemDecoration

        recyclerView.addItemDecoration(linearItemDecoration)

        linearLayoutManager = FlapLinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
        currentLM = linearLayoutManager
        recyclerView.layoutManager = currentLM

        val spanCount = 3
        gridLayoutManager = FlapGridLayoutManager(requireActivity(), spanCount, RecyclerView.VERTICAL, false).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    var spanSize = 1
//                            spanSize = if (position % 2 == 0) 2 else 1
                    return spanSize
                }
            }
        }

        staggeredGridLayoutManager = FlapStaggeredGridLayoutManager(3).apply {}
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.item_decorations, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.linear -> {
                recyclerView.removeItemDecoration(currentItemDecoration)
                currentItemDecoration = linearItemDecoration
                recyclerView.addItemDecoration(currentItemDecoration)
                recyclerView.invalidateItemDecorations()

                currentLM = linearLayoutManager
                recyclerView.layoutManager = currentLM
            }
            R.id.grid -> {
                recyclerView.removeItemDecoration(currentItemDecoration)
                currentItemDecoration = gridItemDecoration
                recyclerView.addItemDecoration(currentItemDecoration)
                recyclerView.invalidateItemDecorations()

                currentLM = gridLayoutManager
                recyclerView.layoutManager = currentLM
            }
            R.id.staggered -> {
                recyclerView.removeItemDecoration(currentItemDecoration)
                currentItemDecoration = gridItemDecoration
                recyclerView.addItemDecoration(currentItemDecoration)
                recyclerView.invalidateItemDecorations()

                currentLM = staggeredGridLayoutManager
                recyclerView.layoutManager = currentLM
            }

            R.id.horizontal -> {
                updateOrientation(RecyclerView.HORIZONTAL)
            }
            R.id.vertical -> {
                updateOrientation(RecyclerView.VERTICAL)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateOrientation(orientation: Int) {

        when (currentItemDecoration) {
            is LinearItemDecoration -> {
                (currentItemDecoration as LinearItemDecoration).orientation = orientation
            }
            is LinearSpaceItemDecoration -> {
                (currentItemDecoration as LinearSpaceItemDecoration).orientation = orientation
            }
            is GridItemDecoration -> {
                (currentItemDecoration as GridItemDecoration).orientation = orientation
            }
            is GridSpaceItemDecoration -> {
                (currentItemDecoration as GridSpaceItemDecoration).orientation = orientation
            }
        }

        when (currentLM) {
            is FlapLinearLayoutManager -> {
                (currentLM as FlapLinearLayoutManager).orientation = orientation
            }
            is FlapGridLayoutManager -> {
                (currentLM as FlapGridLayoutManager).orientation = orientation
            }
            is FlapStaggeredGridLayoutManager -> {
                (currentLM as FlapStaggeredGridLayoutManager).orientation = orientation
            }
        }

        recyclerView.invalidateItemDecorations()
    }
}
