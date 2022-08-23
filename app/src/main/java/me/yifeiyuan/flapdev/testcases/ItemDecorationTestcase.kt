package me.yifeiyuan.flapdev.testcases

import android.graphics.Color
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flap.decoration.GridItemDecoration
import me.yifeiyuan.flap.decoration.GridSpaceItemDecoration
import me.yifeiyuan.flap.decoration.LinearItemDecoration
import me.yifeiyuan.flap.decoration.LinearSpaceItemDecoration
import me.yifeiyuan.flap.ext.doOnCreateViewHolderEnd
import me.yifeiyuan.flap.widget.FlapGridLayoutManager
import me.yifeiyuan.flap.widget.FlapLinearLayoutManager
import me.yifeiyuan.flap.widget.FlapStaggeredGridLayoutManager
import me.yifeiyuan.flapdev.R
import me.yifeiyuan.flapdev.toPixel
import kotlin.random.Random

/**
 * Created by 程序亦非猿 on 2022/8/18.
 */

class ItemDecorationTestcase : BaseTestcaseFragment() {

    override fun onInit(view: View) {
        super.onInit(view)
        setHasOptionsMenu(true)

        currentItemDecoration = gridItemDecoration
        recyclerView.addItemDecoration(currentItemDecoration)

        currentLayoutManager = gridLayoutManager
        recyclerView.layoutManager = currentLayoutManager

        recyclerView.setBackgroundColor(Color.parseColor("#16000000"))

        adapter.doOnCreateViewHolderEnd { adapter, delegate, viewType, component ->
            val preLP = component.itemView.layoutParams
            preLP.height = Random.nextInt(requireActivity().toPixel(50), requireActivity().toPixel(150))
        }
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

                currentLayoutManager = linearLayoutManager
                recyclerView.layoutManager = currentLayoutManager
            }
            R.id.grid -> {
                recyclerView.removeItemDecoration(currentItemDecoration)
                currentItemDecoration = gridItemDecoration
                recyclerView.addItemDecoration(currentItemDecoration)
                recyclerView.invalidateItemDecorations()

                currentLayoutManager = gridLayoutManager
                recyclerView.layoutManager = currentLayoutManager
            }
            R.id.staggered -> {
                recyclerView.removeItemDecoration(currentItemDecoration)
                currentItemDecoration = gridItemDecoration
                recyclerView.addItemDecoration(currentItemDecoration)
                recyclerView.invalidateItemDecorations()

                currentLayoutManager = staggeredGridLayoutManager
                recyclerView.layoutManager = currentLayoutManager
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

        when (currentLayoutManager) {
            is FlapLinearLayoutManager -> {
                (currentLayoutManager as FlapLinearLayoutManager).orientation = orientation
            }
            is FlapGridLayoutManager -> {
                (currentLayoutManager as FlapGridLayoutManager).orientation = orientation
            }
            is FlapStaggeredGridLayoutManager -> {
                (currentLayoutManager as FlapStaggeredGridLayoutManager).orientation = orientation
            }
        }

        recyclerView.invalidateItemDecorations()
    }
}
