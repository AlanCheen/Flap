package me.yifeiyuan.flapdev

import androidx.multidex.MultiDexApplication
import me.yifeiyuan.flap.Flap
import me.yifeiyuan.flap.FlapGlobalConfigBuilder
import me.yifeiyuan.flap.apt.delegates.*
import me.yifeiyuan.flap.hook.ApmHook
import me.yifeiyuan.flap.hook.LoggingHook
import me.yifeiyuan.flap.initFlap
import me.yifeiyuan.flapdev.components.CustomViewTypeComponentDelegate
import me.yifeiyuan.flapdev.components.SimpleTextComponentDelegate
import me.yifeiyuan.flapdev.components.generictest.GenericFlapComponentDelegate

/**
 * Flap
 * Created by 程序亦非猿 on 2018/12/13.
 */
class FlapApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        initFlap1()
        initFlap2()
        initFlapByDSL()
    }

    // Config 类 + Builder 的方式
    private fun initFlap2() {
        val config = FlapGlobalConfigBuilder()
                .context(this)
                .adapterDelegates(
                        ZeroHeightComponentAdapterDelegate(),
                        SimpleTextComponentDelegate(),
                        SimpleImageComponentAdapterDelegate(),
                        CustomViewTypeComponentDelegate(),
                        GenericFlapComponentDelegate(),
                )
                .adapterHooks(LoggingHook(), ApmHook())
                .debug(true)
                .build()

        Flap.init(config)
    }

    private fun initFlapByDSL() {

        initFlap {
            context(this@FlapApplication)
            debug(true)
            adapterDelegates(
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
            adapterHooks(
                    LoggingHook(), ApmHook()
            )
        }
    }

    // 单例+apply 的方式
    private fun initFlap1() {
        Flap.apply {
            //Flap 这里注册的都是是全局的，只是为了测试方便
            //实际开发使用的话 哪个 Adapter 需要才注册更加合适。
            registerAdapterDelegates(
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
            registerAdapterHooks(
                    LoggingHook(), ApmHook()
            )

            //可选
            init(this@FlapApplication)

            //打开日志
            setDebug(true)
        }
    }
}