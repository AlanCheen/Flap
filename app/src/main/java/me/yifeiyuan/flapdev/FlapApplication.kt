package me.yifeiyuan.flapdev

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.multidex.MultiDexApplication
import me.yifeiyuan.flap.Flap
import me.yifeiyuan.flap.ktmodule.KtModuleComponentDelegate
import me.yifeiyuan.flapdev.components.bindersample.BinderComponentDelegate
import me.yifeiyuan.flapdev.components.customviewtype.CustomViewTypeComponentDelegate
import me.yifeiyuan.flapdev.components.generictest.GenericFlapComponentDelegate
import me.yifeiyuan.flapdev.components.simpleimage.SimpleImageComponentDelegate
import me.yifeiyuan.flapdev.components.simpletext.SimpleTextComponentDelegate
import me.yifeiyuan.ktx.foundation.othermodule.JavaModuleComponentDelegate
import me.yifeiyuan.ktx.foundation.othermodule.vb.ViewBindingComponentDelegate

/**
 * Flap
 * Created by 程序亦非猿 on 2018/12/13.
 */
class FlapApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        initFlap()
    }

    private fun initFlap() {
        //only setDebug(true) when you are testing
//        Flap.setDebug(true);
        val t1 = System.currentTimeMillis()
        //自定义 item view type ; custom item view type
//        Flap.getDefault().register(new CustomViewTypeComponent.Factory());
        val t2 = System.currentTimeMillis()
        Log.e("Flap", "Init Flap time cost :" + (t2 - t1))

//        Flap.getDefault().getFlapItemPool().setMaxRecycledViews(new SimpleImageItem.Factory().getItemViewType(null), 8);
//        Flap.getDefault().getFlapItemPool().setMaxRecycledViews(new SimpleImageItem.Factory().getItemViewType(null), 8);

        Flap.registerAdapterDelegates(
                SimpleTextComponentDelegate(),
                SimpleImageComponentDelegate(),
                CustomViewTypeComponentDelegate(),
                GenericFlapComponentDelegate(),
                ViewBindingComponentDelegate(),
                JavaModuleComponentDelegate(),
                KtModuleComponentDelegate(),
                BinderComponentDelegate()
        )
    }
}

val uiThread = Handler(Looper.getMainLooper())

fun Any.post(runnable: Runnable) {
    uiThread.post(runnable)
}

fun Any.postDelay(delay: Long, runnable: Runnable) {
    uiThread.postDelayed(runnable, delay)
}