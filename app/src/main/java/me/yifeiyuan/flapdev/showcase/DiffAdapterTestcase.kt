package me.yifeiyuan.flapdev.showcase

import android.os.Handler
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flap.differ.IDiffer
import me.yifeiyuan.flap.differ.FlapDifferAdapter
import me.yifeiyuan.flapdev.components.TestDiffModel
import java.util.ArrayList


/**
 * Created by 程序亦非猿 on 2022/8/1.
 */
class DiffAdapterTestcase : BaseTestcaseFragment() {

    override fun createAdapter(): FlapAdapter {
        return FlapDifferAdapter<IDiffer>().apply {
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