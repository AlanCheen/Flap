package me.yifeiyuan.flapdev.testcases

import android.graphics.Color
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import me.yifeiyuan.flap.decoration.SpaceItemDecoration
import me.yifeiyuan.flap.ext.doOnBindViewHolderEnd
import me.yifeiyuan.flap.ext.doOnCreateViewHolderEnd
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

        currentLayoutManager = staggeredGridLayoutManager
        recyclerView.layoutManager = staggeredGridLayoutManager

        recyclerView.setBackgroundColor(Color.parseColor("#16000000"))

        adapter.doOnCreateViewHolderEnd { adapter, delegate, viewType, component ->
            val preLP = component.itemView.layoutParams
            preLP.height = Random.nextInt(requireActivity().toPixel(50), requireActivity().toPixel(150))
        }

        adapter.doOnBindViewHolderEnd { adapter, delegate, component, data, position, payloads ->
            if (recyclerView.layoutManager is StaggeredGridLayoutManager) {
                val index = (component.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams).spanIndex
                Log.d("StaggeredGridL", "onInit() called with: position = $position, spanIndex = $index")
            }
        }

        adapter.doOnPreload {
            loadMoreData(20)
        }

        adapter.doOnItemClick { recyclerView, childView, position ->
            if (recyclerView.layoutManager is StaggeredGridLayoutManager) {
                val component  = recyclerView.getChildViewHolder(childView)
                val index = (component.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams).spanIndex
                Log.d("StaggeredGridL", "onInit() called with: position = $position, spanIndex = $index")
            }
        }

        adapter.doOnItemLongClick { recyclerView, childView, position ->
            if (position == 0) {
                recyclerView.requestLayout()
            }
            false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.item_decorations, menu)
    }

    override fun refreshData(size: Int) {
        super.refreshData(size)
        staggeredGridLayoutManager.invalidateSpanAssignments()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
        }
        return super.onOptionsItemSelected(item)
    }
}
