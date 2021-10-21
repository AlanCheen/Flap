package me.yifeiyuan.flapdev.showcase

import android.os.Bundle
import android.util.Log
import android.view.View
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flap.hook.PrefetchDetector
import me.yifeiyuan.flap.hook.attachTo
import me.yifeiyuan.flapdev.ShowcaseAdapter
import me.yifeiyuan.flapdev.components.simpletext.SimpleTextModel
import java.util.ArrayList

/**
 * Created by 程序亦非猿 on 2021/10/19.
 */
class PrefetchFragment : BaseFragment() {

    companion object {
        private const val TAG = "PrefetchFragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        usePrefetchDetector()

//        adapter.doOnPrefetch(3){
//
//        }
    }

    private fun useAdapter(){
        adapter.doOnPrefetch(3) {
            loadMoreData(5)
        }
    }

    private fun usePrefetchDetector() {
        val prefetchDetector = PrefetchDetector(4) {
            toast("开始预加载")
            Log.d(TAG, "onViewCreated: 开始预加载")
            loadMoreData()
        }.attachTo(adapter)

        prefetchDetector.prefetchEnable = true // 默认 true
        prefetchDetector.setPrefetchComplete() // 当出错时，需要手动调用
    }

}