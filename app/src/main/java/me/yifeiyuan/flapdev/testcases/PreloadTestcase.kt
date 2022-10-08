package me.yifeiyuan.flapdev.testcases

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import me.yifeiyuan.flap.hook.PreloadHook
import me.yifeiyuan.flapdev.R
import me.yifeiyuan.flapdev.components.SimpleTextModel
import java.util.*

/**
 * Created by 程序亦非猿 on 2021/10/19.
 */
class PreloadTestcase : BaseTestcaseFragment() {

    companion object {
        private const val TAG = "PreloadTestcase"
    }

    private var testPreloadErrorCase = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setupPreload()

        recyclerView.layoutManager = linearLayoutManager
        recyclerView.addItemDecoration(linearItemDecoration)
    }

    private fun setupPreload() {
        //滑动到底部
        adapter.doOnPreload(offset = 0, minItemCount = 2, direction = PreloadHook.SCROLL_DOWN) {
            requestMoreData()
        }

        //滑动到顶部
        adapter.doOnPreload(offset = 2, minItemCount = 2, direction = PreloadHook.SCROLL_UP) {

            toast("顶部，开始预加载")

            Handler().postDelayed({
                val list = ArrayList<Any>()
                val size = 5
                repeat(size) {
                    list.add(SimpleTextModel("头部加载更多数据 $it of $size"))
                }
                adapter.addDataAndNotify(list)
            }, 300)
        }
    }

    private fun requestMoreData() {
        Log.d(TAG, "requestMoreData")
        if (testPreloadErrorCase) {
            Log.d(TAG, "预加载失败场景，必须调用 setPreloadComplete ")
            adapter.setPreloadComplete() // 当出错时，需要手动调用，不然不会再进行检查
        } else {
            Log.d(TAG, "onViewCreated: 开始预加载")
            toast("底部，开始预加载")
            loadMoreData()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.preload, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setLoadMoreError -> {
                item.isChecked = item.isChecked.not()
                testPreloadErrorCase = item.isChecked
            }
            R.id.setLoadMoreEnable -> {
                item.isChecked = item.isChecked.not()
                adapter.setPreloadEnable(item.isChecked)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}