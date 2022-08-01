package me.yifeiyuan.flapdev.showcase

import android.util.Log
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flapdev.components.TestClickModel
import java.util.ArrayList

private const val TAG = "ItemClicksTestcase"

/**
 * 测试点击和长按事件
 *
 * Created by 程序亦非猿 on 2022/7/27.
 */
class ItemClicksTestcase : BaseTestcaseFragment() {

    override fun createAdapter(): FlapAdapter {
        return super.createAdapter().apply {

            doOnItemClick { recyclerView, childView, position ->
                Log.d(TAG, "doOnItemClick called with: childView = $childView, position = $position")
                toast("点击了 position = $position")
            }

            doOnItemLongClick { recyclerView, childView, position ->
                Log.d(TAG, "doOnItemLongClick called with: childView = $childView, position = $position")
                toast("长按了 position = $position")
                true
            }
        }
    }

    override fun createRefreshData(size: Int): MutableList<Any> {
        val list = ArrayList<Any>()
        repeat(size) {
            list.add(TestClickModel("测试点击、长按，$it of $size"))
        }
        return list
    }
}