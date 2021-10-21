package me.yifeiyuan.flapdev.showcase

import android.os.Bundle
import android.view.View
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flapdev.ShowcaseAdapter
import me.yifeiyuan.flapdev.mockData
import me.yifeiyuan.flapdev.mockModels

/**
 * 多类型
 */
class MultiTypeFragment : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun createRefreshData(size: Int): MutableList<Any> {
        return mockModels()
    }
}