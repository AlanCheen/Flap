package me.yifeiyuan.flapdev

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.core.view.get
import androidx.multidex.MultiDexApplication
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import me.yifeiyuan.flap.Flap
import me.yifeiyuan.flap.apt.delegates.*
import me.yifeiyuan.flap.dsl.adapterHook
import me.yifeiyuan.flap.hook.LoggingHook
import me.yifeiyuan.flapdev.components.*

/**
 * Flap
 * Created by 程序亦非猿 on 2018/12/13.
 */
class FlapApplication : MultiDexApplication() {

    companion object {

        val ViewPager2.recyclerView: RecyclerView
            get() {
                return this[0] as RecyclerView
            }

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

        val dslAdapterHook = adapterHook {
            onCreateViewHolderStart { adapter, delegate, viewType ->

            }
            onCreateViewHolderEnd { adapter, delegate, viewType, component ->

            }
            onBindViewHolderStart { adapter, delegate, component, data, position, payloads ->

            }
            onBindViewHolderEnd { adapter, delegate, component, data, position, payloads ->
                Log.d("dslAdapterHook", "onBindViewHolderEnd() called with: adapter = $adapter, delegate = $delegate, component = $component, data = $data, position = $position, payloads = $payloads")
            }
            onViewAttachedToWindow { adapter, delegate, component ->

            }

            onViewDetachedFromWindow { adapter, delegate, component ->

            }
        }

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
//                    ApmHook(),
                    dslAdapterHook,
            )

            registerAdapterService(TestService::class.java)

            //可选
            withContext(this@FlapApplication)

            //打开日志
            setDebug(true)
        }
    }
}