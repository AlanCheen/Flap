package me.yifeiyuan.flapdev.showcase

import android.os.Bundle
import android.view.View
import me.yifeiyuan.flapdev.mockModels

/**
 * 多类型
 */
class MultiTypeCase : BaseCaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun createRefreshData(size: Int): MutableList<Any> {
        return mockModels()
    }
}