package me.yifeiyuan.flapdev.testcases

import android.os.Handler
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flap.differ.IDiffer
import me.yifeiyuan.flap.differ.FlapDifferAdapter
import me.yifeiyuan.flapdev.components.TestDiffModel
import java.util.ArrayList


/**
 * 测试 FlapDifferAdapter 功能
 *
 * 测试说明，点击按钮后，再下拉刷新，只有被修改了的数据才会有刷新动画
 *
 * @see FlapDifferAdapter
 *
 * Created by 程序亦非猿 on 2022/8/1.
 */
class FlapDifferAdapterTestcase : BaseTestcaseFragment() {

    //如果不测试 header footer,就注释掉 onViewCreated
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val headerFooterAdapter = HeaderFooterAdapter(adapter)
//
//        val headerView = LayoutInflater.from(activity).inflate(R.layout.header_layout, null, false)
//        headerFooterAdapter.setupHeaderView(headerView)
//
//        val footerView = LayoutInflater.from(activity).inflate(R.layout.footer_layout, null, false)
//        headerFooterAdapter.setupFooterView(footerView)
//
//        adapter.doOnItemClick { recyclerView, childView, position ->
//            val component = recyclerView.getChildViewHolder(childView)
//            if (headerFooterAdapter.isHeader(component)) {
//                toast("点击了 position = $position，是 Header!")
//                return@doOnItemClick
//            }
//            if (headerFooterAdapter.isFooter(component)) {
//                toast("点击了 position = $position，是 Footer!")
//                return@doOnItemClick
//            }
//            val realPosition = if (position == 0) position else position - headerFooterAdapter.getHeaderCount()
//            toast("点击了 position = $position，model=${adapter.getItemData(realPosition)}")
//        }
//
//        recyclerView.adapter = headerFooterAdapter
//    }

    override fun createAdapter(): FlapAdapter {
        return FlapDifferAdapter<IDiffer>().apply {
            setData(createRefreshData())
        }
    }

    override fun createRefreshData(size: Int): MutableList<Any> {
        val list = mutableListOf<Any>()
        repeat(20) {
            list.add(TestDiffModel("content,$it of 20", it, "this is desc :${it % 3}"))
        }
        return list
    }

    override fun loadMoreData(size: Int) {
        Handler().postDelayed({
            val list = ArrayList<Any>()
            repeat(size) {
                list.add(TestDiffModel("content,$it of 20,more data", it, "this is desc :${it % 3}"))
            }
            adapter.appendData(list)
        }, 500)
    }

}