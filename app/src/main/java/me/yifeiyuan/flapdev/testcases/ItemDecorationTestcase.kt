package me.yifeiyuan.flapdev.testcases

import android.graphics.Color
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flap.decoration.LinearItemDecoration
import me.yifeiyuan.flap.decoration.LinearSpaceItemDecoration
import me.yifeiyuan.flap.widget.FlapGridLayoutManager
import me.yifeiyuan.flap.widget.FlapLinearLayoutManager
import me.yifeiyuan.flap.widget.FlapStaggeredGridLayoutManager
import me.yifeiyuan.flapdev.R

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

//        adapter.doOnCreateViewHolderEnd { adapter, delegate, viewType, component ->
//            val preLP = component.itemView.layoutParams
//            preLP.height = Random.nextInt(requireActivity().toPixel(50), requireActivity().toPixel(150))
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.item_decorations, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.linear -> {
                switchLayoutManager(0)
            }
            R.id.grid -> {
                switchLayoutManager(1)
            }
            R.id.staggered -> {
                switchLayoutManager(2)
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
}
