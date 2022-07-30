package me.yifeiyuan.flapdev.showcase

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import me.yifeiyuan.flapdev.R

/**
 * Created by 程序亦非猿 on 2021/10/19.
 */
class PreloadTestcase : BaseCaseFragment() {

    companion object {
        private const val TAG = "PrefetchFragment"
    }

    private var testPreloadErrorCase = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        useAdapter()
    }

    private fun useAdapter() {
        adapter.doOnPreload(offset = 0, minItemCount = 2) {
            requestMoreData()
        }
    }

    private fun requestMoreData() {
        Log.d(TAG, "requestMoreData")
        if (testPreloadErrorCase) {
            Log.d(TAG, "预加载失败场景，必须调用 setPrefetchComplete ")
            adapter.setPreloadComplete() // 当出错时，需要手动调用，不然不会再进行检查
        } else {
            Log.d(TAG, "onViewCreated: 开始预加载")
            toast("开始预加载")
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