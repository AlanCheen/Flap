package me.yifeiyuan.flapdev.showcase

import android.os.Bundle
import android.view.View
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flapdev.ShowcaseAdapter
import me.yifeiyuan.flapdev.mockData

/**
 * 多类型
 */
class MultiTypeFragment : BaseFragment() {

    override fun createAdapter(): FlapAdapter {
        return ShowcaseAdapter().apply {
            mockData()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}