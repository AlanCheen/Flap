package me.yifeiyuan.flapdev

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.core.view.get
import androidx.multidex.MultiDexApplication
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import me.yifeiyuan.flap.Flap
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
            onCreateViewHolderStart { adapter, viewType ->

            }
            onCreateViewHolderEnd { adapter, viewType, component ->

            }
            onBindViewHolderStart { adapter, component, data, position, payloads ->

            }
            onBindViewHolderEnd { adapter, component, data, position, payloads ->
                Log.d("dslAdapterHook", "onBindViewHolderEnd() called with: adapter = $adapter, component = $component, data = $data, position = $position, payloads = $payloads")
            }

            onViewAttachedToWindow { adapter, component ->

            }

            onViewDetachedFromWindow { adapter, component ->

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
                    createTestClickDelegate(),
                    createZeroHeightComponentDelegate(),
                    createTestAdapterApiComponentDelegate(),
                    createDataBindingDelegate(),
                    createDiffDelegate(),
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