package me.yifeiyuan.flapdev

import android.os.Handler
import android.os.Looper
import androidx.multidex.MultiDexApplication
import me.yifeiyuan.flap.Flap
import me.yifeiyuan.flap.apt.delegates.*
import me.yifeiyuan.flap.hook.ApmHook
import me.yifeiyuan.flap.hook.DebugHelperHook
import me.yifeiyuan.flap.ktmodule.KtModuleComponentDelegate
import me.yifeiyuan.flapdev.components.CustomViewTypeComponentDelegate
import me.yifeiyuan.flapdev.components.generictest.GenericFlapComponentDelegate
import me.yifeiyuan.flapdev.components.SimpleTextComponentDelegate
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
        Flap.apply {

            //Flap 这里注册的都是是全局的，只是为了测试方便
            //实际开发使用的话 哪个 Adapter 需要才注册更加合适。
            registerAdapterDelegates(
                    SimpleTextComponentDelegate(),
                    SimpleImageComponentAdapterDelegate(),
                    CustomViewTypeComponentDelegate(),
                    GenericFlapComponentDelegate(),
                    ViewBindingComponentAdapterDelegate(),
                    JavaModuleComponentAdapterDelegate(),
                    KtModuleComponentAdapterDelegate(),
                    TestClickComponentAdapterDelegate(),
                    TestBinderComponentAdapterDelegate(),
                    TestAllComponentAdapterDelegate(),
                    SimpleDataBindingComponentAdapterDelegate()
            )

            //也是全局
            registerAdapterHooks(DebugHelperHook(), ApmHook())

            //打开日志
            setDebug(true)
        }
    }
}

val uiThread = Handler(Looper.getMainLooper())

fun Any.post(runnable: Runnable) {
    uiThread.post(runnable)
}

fun Any.postDelay(delay: Long, runnable: Runnable) {
    uiThread.postDelayed(runnable, delay)
}