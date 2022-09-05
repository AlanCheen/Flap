package me.yifeiyuan.flapdev.testcases

import android.util.Log
import android.view.View
import android.widget.ImageView
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flap.delegate.AdapterDelegate
import me.yifeiyuan.flap.dsl.adapterDelegate
import me.yifeiyuan.flap.dsl.adapterHook
import me.yifeiyuan.flap.ext.bindButton
import me.yifeiyuan.flap.ext.bindTextView
import me.yifeiyuan.flap.ext.bindView
import me.yifeiyuan.flap.hook.AdapterHook
import me.yifeiyuan.flapdev.R
import me.yifeiyuan.flapdev.components.SimpleImageModel
import me.yifeiyuan.flapdev.components.SimpleTextModel
import me.yifeiyuan.flapdev.components.TestAllModel
import me.yifeiyuan.flapdev.mockMultiTypeModels

private const val TAG = "LayoutDelegateDSLTest"

/**
 * 测试 DSL
 *
 * Created by 程序亦非猿 on 2022/8/4.
 */
class DSLTestcase : BaseTestcaseFragment() {

    override fun onInit(view: View) {
        super.onInit(view)

        val simpleTextDelegate = adapterDelegate<SimpleTextModel>(R.layout.flap_item_simple_text) {
            onBind { model ->
                bindTextView(R.id.tv_content) {
                    text = model.content
                }
            }

            onBind { model, position, payloads, adapter ->
                bindTextView(R.id.tv_content) {
                    text = model.content
                }
            }

            onClick { model, position, adapter ->
                toast("onClick() called with: component = $this, model = $model, position = $position")
            }

            onLongClick { model, position, adapter ->
                toast("onLongClick() called with: component = $this, model = $model, position = $position")
                true
            }

            onViewAttachedToWindow {
                Log.d(TAG, "simpleTextDelegate onViewAttachedToWindow() called ,position=$position")
            }

            onViewDetachedFromWindow {
                Log.d(TAG, "simpleTextDelegate onViewDetachedFromWindow() called ,position=$position")
            }

            onViewRecycled {
                Log.d(TAG, "onViewRecycled() called")
            }

            onFailedToRecycleView {
                Log.d(TAG, "onFailedToRecycleView() called")
                false
            }

            onResume {
                Log.d(TAG, "simpleTextDelegate onResume() called")
            }

            onPause {
                Log.d(TAG, "simpleTextDelegate onPause() called")
            }

            onStop {
                Log.d(TAG, "simpleTextDelegate onStop() called")
            }

            onDestroy {
                Log.d(TAG, "simpleTextDelegate onDestroy() called")
            }
        }

        val testAllDelegate = adapterDelegate<TestAllModel>(R.layout.component_test_all_feature) {

            onBind { model, position, payloads, adapter ->
                bindButton(R.id.testFireEvent) {
                    setOnClickListener {
                        toast("testAllDelegate clicked button")
                    }
                }
            }

            onClick { model, position, adapter ->
                toast("onClick() called with: component = $this, model = $model, position = $position")
            }

            onLongClick { model, position, adapter ->
                toast("onLongClick() called with: component = $this, model = $model, position = $position")
                true
            }

            onViewAttachedToWindow {
                Log.d(TAG, "testAllDelegate onViewAttachedToWindow() called ,position=$position")
            }

            onViewDetachedFromWindow {
                Log.d(TAG, "testAllDelegate onViewDetachedFromWindow() called ,position=$position")
            }

            onViewRecycled {

            }

            onFailedToRecycleView {
                false
            }

            onResume {
                Log.d(TAG, "testAllDelegate onResume() called")
            }

            onPause {
                Log.d(TAG, "testAllDelegate onPause() called")
            }
        }

//        val simpleImageDelegate = LayoutAdapterDelegate(SimpleImageModel::class.java,
//                R.layout.flap_item_simple_image) { model ->
//            bindView<ImageView>(R.id.logo) {
//                setOnClickListener {
//                    toast("simpleImageDelegate clicked")
//                }
//            }
//        }

        val simpleImageDelegate = adapterDelegate<SimpleImageModel>(R.layout.flap_item_simple_image) {

            onBind { model, position, payloads, adapter ->
                bindView<ImageView>(R.id.logo) {
                    setOnClickListener {
                        toast("simpleImageDelegate clicked")
                    }
                }
            }
            onResume {
                Log.d(TAG, "simpleImageDelegate onResume() called $this")
            }
        }

//        val hook2 = object : AdapterHook{
//            override fun onBindViewHolderEnd(adapter: FlapAdapter, delegate: AdapterDelegate<*, *>, component: Component<*>, data: Any, position: Int, payloads: MutableList<Any>) {
//
//            }
//        }
        val hook = adapterHook {
            onBindViewHolderEnd { adapter, delegate, component, data, position, payloads ->
                Log.d(TAG, "onBindViewHolderEnd() called with: adapter = $adapter, delegate = $delegate, component = $component, data = $data, position = $position, payloads = $payloads")
            }
        }



        adapter.registerAdapterDelegates(simpleTextDelegate, simpleImageDelegate, testAllDelegate)

        adapter.registerAdapterHook(hook)
    }

    override fun createAdapter(): FlapAdapter {
        return super.createAdapter().apply {
            clearAdapterDelegates()
        }
    }

    override fun isLongClickEnable(): Boolean {
        return false
    }

    override fun isClickEnable(): Boolean {
        return false
    }

    override fun createRefreshData(size: Int): MutableList<Any> {
        return mockMultiTypeModels()
    }
}