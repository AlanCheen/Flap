package me.yifeiyuan.flapdev.showcase

import android.util.Log
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flapdev.components.TestClickModel
import java.util.ArrayList

private const val TAG = "ItemClickTestcase"

/**
 *
 * 有个问题，当 doOnItemClick 和 doOnItemLongClick 同时设置时，longclick 在 down 的时候就触发了
 * Created by 程序亦非猿 on 2022/7/27.
 */
class ItemClickTestcase : BaseCaseFragment() {

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

//            doOnItemClick { childView, position ->
//                Log.d(TAG, "doOnItemClick called with: childView = $childView, position = $position")
//                toast("点击了 position = $position")
//            }

//            doOnItemLongClick { childView, position ->
//                Log.d(TAG, "doOnItemLongClick called with: childView = $childView, position = $position")
//                toast("长按了 position = $position")
//            }
        }
    }

    override fun createRefreshData(size: Int): MutableList<Any> {
        val list = ArrayList<Any>()
        repeat(size) {
            list.add(TestClickModel("TestClickModel 数据 $it of $size"))
        }
        return list
    }
}