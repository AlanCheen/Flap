package me.yifeiyuan.flapdev

import android.app.Application
import androidx.multidex.MultiDexApplication
import me.yifeiyuan.flap.Flap
import me.yifeiyuan.flap.apt.delegates.*
import me.yifeiyuan.flap.hook.LoggingHook
import me.yifeiyuan.flapdev.components.*
import me.yifeiyuan.flapdev.components.generictest.GenericFlapComponentDelegate

/**
 * Flap
 * Created by 程序亦非猿 on 2018/12/13.
 */
class FlapApplication : MultiDexApplication() {

    companion object{

        var application :Application?=null
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        initFlap()
    }

    private fun initFlap() {

        Flap.apply {

            //Flap 这里注册的都是是全局的，只是为了测试方便
            //实际开发使用的话 哪个 Adapter 需要才注册更加合适。
            registerAdapterDelegates(
                    fullConfigAdapterDelegate(),
                    bannerAdapterDelegate(),
                    ZeroHeightComponentAdapterDelegate(),
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
                    DataBindingComponentAdapterDelegate(),
                    DiffComponentAdapterDelegate(),
            )

            //也是全局
            registerAdapterHooks(LoggingHook(),
//                    ApmHook()
            )

            registerAdapterService(TestService::class.java)

            //可选
            withContext(this@FlapApplication)

            //打开日志
            setDebug(true)
        }
    }
}