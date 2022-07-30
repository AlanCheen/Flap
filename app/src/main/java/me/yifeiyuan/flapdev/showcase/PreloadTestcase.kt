package me.yifeiyuan.flapdev.showcase

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import me.yifeiyuan.flap.hook.PreloadHook
import me.yifeiyuan.flapdev.R

/**
 * Created by 程序亦非猿 on 2021/10/19.
 */
class PreloadTestcase : BaseCaseFragment() {

    companion object {
        private const val TAG = "PrefetchFragment"
    }

    private var testPrefetchErrorCase = false

    private lateinit var prefetchDetector: PreloadHook

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

//        usePrefetchDetector()
        useAdapter()
    }

    private fun useAdapter() {
        adapter.doOnPrefetch(offset = 0,minItemCount = 2) {
            requestMoreDataAdapter()
        }
    }

    private fun requestMoreDataAdapter() {
        Log.d(TAG, "requestMoreData")
        if (testPrefetchErrorCase) {
            Log.d(TAG, "预加载失败场景，必须调用 setPrefetchComplete ")
            adapter.setPrefetchComplete() // 当出错时，需要手动调用，不然不会再进行检查
        } else {
            Log.d(TAG, "onViewCreated: 开始预加载")
            toast("开始预加载")
            loadMoreData()
        }
    }

    private fun usePrefetchDetector() {
        prefetchDetector = PreloadHook(minItemCount = 10,offset = 4) {
            requestMoreData()
        }
        adapter.registerAdapterHook(prefetchDetector)
    }

    private fun requestMoreData() {
        Log.d(TAG, "requestMoreData")
        if (testPrefetchErrorCase) {
            Log.d(TAG, "预加载失败场景，必须调用 setPrefetchComplete ")
            prefetchDetector.setPrefetchComplete() // 当出错时，需要手动调用，不然不会再进行检查
        } else {
            Log.d(TAG, "onViewCreated: 开始预加载")
            toast("开始预加载")
            loadMoreData()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.prefetch, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setLoadMoreError -> {
                item.isChecked = item.isChecked.not()
                testPrefetchErrorCase = item.isChecked
            }
            R.id.setLoadMoreEnable -> {
                item.isChecked = item.isChecked.not()
                if (this::prefetchDetector.isInitialized) {
                    prefetchDetector.prefetchEnable = item.isChecked
                }else{
                    adapter.setPrefetchEnable(item.isChecked)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}