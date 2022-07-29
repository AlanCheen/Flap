package me.yifeiyuan.flapdev.showcase

import android.os.Bundle
import android.view.View
import me.yifeiyuan.flapdev.components.CustomViewTypeModel
import java.util.ArrayList

/**
 * Created by 程序亦非猿 on 2022/7/25.
 */
class FlapComponentPoolTestcase : BaseCaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        recyclerView.setRecycledViewPool(SharedPool)
    }

    override fun createRefreshData(size: Int): MutableList<Any> {
        val models: MutableList<Any> = ArrayList()
        repeat(size) {
            models.add(CustomViewTypeModel())
        }
        return models
    }
}