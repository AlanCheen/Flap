package me.yifeiyuan.flapdev.showcase

import android.os.Bundle
import android.view.View
import me.yifeiyuan.flapdev.mockMultiTypeModels

/**
 * 多类型测试
 */
class MultiTypeTestcase : BaseCaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val llm = LinearLayoutManager(requireContext())
//        llm.recycleChildrenOnDetach = true
//        recyclerView.layoutManager = llm
//        recyclerView.setRecycledViewPool(SharedPool)
    }

    override fun createRefreshData(size: Int): MutableList<Any> {
        return mockMultiTypeModels()
    }
}