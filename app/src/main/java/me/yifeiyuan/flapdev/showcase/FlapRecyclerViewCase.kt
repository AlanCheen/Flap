package me.yifeiyuan.flapdev.showcase

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import me.yifeiyuan.flap.widget.FlapRecyclerView
import me.yifeiyuan.flapdev.R

/**
 * Created by 程序亦非猿 on 2022/2/21.
 */
class FlapRecyclerViewCase : BaseCaseFragment() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_case_flap_rv
    }

   lateinit var flapRecyclerView: FlapRecyclerView

    override fun onInit(view: View) {
        recyclerView = view.findViewById(R.id.recyclerView)
        flapRecyclerView = recyclerView as FlapRecyclerView

        flapRecyclerView.setData(createRefreshData(30))

        val emptyView = view.findViewById<View>(R.id.emptyView)

        flapRecyclerView.emptyView = emptyView

        setHasOptionsMenu(true)
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
        }
        return super.onOptionsItemSelected(item)
    }
}