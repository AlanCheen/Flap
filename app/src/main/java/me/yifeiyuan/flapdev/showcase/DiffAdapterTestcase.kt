package me.yifeiyuan.flapdev.showcase

import android.os.Bundle
import android.os.Handler
import android.view.View
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flap.diff.DiffModel
import me.yifeiyuan.flap.diff.FlapDiffAdapter
import me.yifeiyuan.flapdev.components.TestDiffModel
import java.util.ArrayList


/**
 * Created by 程序亦非猿 on 2022/8/1.
 */
class DiffAdapterTestcase : BaseTestcaseFragment() {

    override fun createAdapter(): FlapAdapter {
        return FlapDiffAdapter<DiffModel>().apply {
            setData(createRefreshData())
        }
    }

    override fun createRefreshData(size: Int): MutableList<Any> {
        val list = mutableListOf<Any>()
        repeat(20){
            list.add(TestDiffModel("content,$it of 20",it,"this is desc :${it%3}"))
        }
        return list
    }

    override fun loadMoreData(size: Int) {
        Handler().postDelayed({
            val list = ArrayList<Any>()
            repeat(size) {
                list.add(TestDiffModel("content,$it of 20,more data",it,"this is desc :${it%3}"))
            }
            adapter.appendData(list)
        }, 500)
    }

}