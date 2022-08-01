package me.yifeiyuan.flapdev.showcase

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flapdev.HeaderFooterAdapter
import me.yifeiyuan.flapdev.R

private const val TAG = "HeaderFooterTestcase"

/**
 * Created by 程序亦非猿 on 2022/8/1.
 */
class HeaderFooterTestcase : BaseCaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val headerFooterAdapter = HeaderFooterAdapter(adapter)

        val headerView = LayoutInflater.from(activity).inflate(R.layout.header_layout, null, false)
        headerView.layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)

        headerFooterAdapter.headerView = headerView

        val footerView = LayoutInflater.from(activity).inflate(R.layout.footer_layout, null, false)
        footerView.layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)

        headerFooterAdapter.footerView = footerView

        //需要处理偏移量
        adapter.doOnItemClick { recyclerView, childView, position ->
            Log.d(TAG, "doOnItemClick called with: childView = $childView, position = $position")
            val component = recyclerView.getChildViewHolder(childView)
            val realPosition = if (position == 0) position else position - headerFooterAdapter.getHeaderCount()
            toast("点击了 position = $position，model=${adapter.getItemData(realPosition)}")
        }

        recyclerView.adapter = headerFooterAdapter
    }
}