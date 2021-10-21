package me.yifeiyuan.flapdev.showcase

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import me.yifeiyuan.flap.hook.PrefetchDetector
import me.yifeiyuan.flapdev.appendMockData
import me.yifeiyuan.flapdev.appendPrefetchData

/**
 * Created by 程序亦非猿 on 2021/10/19.
 */
class PrefetchFragment : BaseFragment() {

    companion object{
        private const val TAG = "PrefetchFragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefetchDetector = PrefetchDetector(3) {
            toast("开始预加载")
            Log.d(TAG, "onViewCreated: 开始预加载")
            Handler().postDelayed({
                adapter.appendPrefetchData()
            }, 500)
        }

        prefetchDetector.prefetchEnable = true // 默认 true

        prefetchDetector.setPrefetchComplete() // 当出错时，需要手动调用

        adapter.registerAdapterHook(prefetchDetector)

//        adapter.doOnPrefetch(3){
//
//        }
    }
}