package me.yifeiyuan.flapdev.testcases

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flap.pool.RecyclerViewDumpHelper
import me.yifeiyuan.flap.widget.FlapLinearLayoutManager
import me.yifeiyuan.flapdev.R
import me.yifeiyuan.flapdev.components.CustomViewTypeComponent
import me.yifeiyuan.flapdev.components.CustomViewTypeModel
import java.util.ArrayList

/**
 *
 * 测试 ComponentPool
 *
 * 列表滑动时，只有 Recycler.mCachedViews 和 RecycledViewPool.mScrap 会回收缓存 ViewHolder
 *
 * Created by 程序亦非猿 on 2022/7/25.
 */
class ComponentPoolTestcase : BaseTestcaseFragment() {

    lateinit var poolDumper: RecyclerViewDumpHelper

    lateinit var infoTextView: TextView

    var dumpDetails: Boolean = false

    override fun onInit(view: View) {
        super.onInit(view)

        infoTextView = view.findViewById(R.id.info)

        poolDumper = RecyclerViewDumpHelper(recyclerView)
        setHasOptionsMenu(true)

        recyclerView.setItemViewCacheSize(6) // 设置 Recycler.mRequestedCacheMax 大小，默认 2

        val llm = FlapLinearLayoutManager(requireContext(), RecyclerView.VERTICAL)

//        llm.initialPrefetchItemCount = 3
        llm.isItemPrefetchEnabled = true // 影响 mCachedViews.size 大小，关闭后会降低 size ，具体是 GapWorker 处理

        recyclerView.layoutManager = llm

        recyclerView.recycledViewPool.setMaxRecycledViews(CustomViewTypeComponent.CUSTOM_ITEM_VIEW_TYPE, 10)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                infoTextView.text = poolDumper.dumpRecycler(dumpDetails)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.pool, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.dumpRecycler -> {
                infoTextView.text = poolDumper.dumpRecycler()
            }
            R.id.dumpPool -> {
                infoTextView.text = poolDumper.dumpRecycledViewPool()
            }
            R.id.dumpDetails -> {
                item.isChecked = item.isChecked.not()
                dumpDetails = item.isChecked
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun createRefreshData(size: Int): MutableList<Any> {
        val models: MutableList<Any> = ArrayList()
        repeat(30) {
            models.add(CustomViewTypeModel("Data $it of 30"))
        }
        return models
    }
}