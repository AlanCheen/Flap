package me.yifeiyuan.flapdev

import android.app.Application
import android.widget.Toast
import androidx.multidex.MultiDexApplication
import me.yifeiyuan.flap.Flap
import me.yifeiyuan.flap.apt.delegates.*
import me.yifeiyuan.flap.hook.LoggingHook
import me.yifeiyuan.flapdev.components.*

/**
 * Flap
 * Created by 程序亦非猿 on 2018/12/13.
 */
class FlapApplication : MultiDexApplication() {

    companion object {
        var application: Application? = null
            set(value) {
                field = value
                toast = Toast.makeText(value, "", Toast.LENGTH_SHORT)
            }
        lateinit var toast: Toast
        fun toast(title: String) {
            toast.setText(title)
            toast.duration = Toast.LENGTH_SHORT
            toast.show()
        }
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
                    createFullConfigAdapterDelegate(),
                    createBannerAdapterDelegate(),
                    createSimpleImageDelegate(),
                    SimpleTextComponentDelegate(),
                    createCustomViewTypeComponentDelegate(),
                    createViewBindingDelegate(),
                    KtModuleComponentAdapterDelegate(),
                    TestClickComponentAdapterDelegate(),
                    createZeroHeightComponentDelegate(),
                    createTestAdapterApiComponentDelegate(),
                    DataBindingComponentAdapterDelegate(),
                    DiffComponentAdapterDelegate(),
            )

            //也是全局
            registerAdapterHooks(
                    LoggingHook(
                            enableLog = true,
//                            printTrace = true
                    ),
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