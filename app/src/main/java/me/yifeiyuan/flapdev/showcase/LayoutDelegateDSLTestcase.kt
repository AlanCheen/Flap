package me.yifeiyuan.flapdev.showcase

import android.util.Log
import android.view.View
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flap.delegate.makeDelegate
import me.yifeiyuan.flap.event.Event
import me.yifeiyuan.flap.ext.bindButton
import me.yifeiyuan.flap.ext.bindTextView
import me.yifeiyuan.flapdev.R
import me.yifeiyuan.flapdev.components.SimpleTextModel
import me.yifeiyuan.flapdev.components.TestAllModel

private const val TAG = "LayoutDelegateDSLTest"

/**
 * Created by 程序亦非猿 on 2022/8/4.
 */
class LayoutDelegateDSLTestcase : BaseTestcaseFragment() {

    override fun onInit(view: View) {
        super.onInit(view)

        val simpleTextDelegate = makeDelegate<SimpleTextModel>(R.layout.flap_item_simple_text) {

            onBind { component, model, position ->
                component.bindTextView(R.id.tv_content) {
                    text = model.content
                }
            }

            onClick { component, model, position ->
                toast("onClick() called with: component = $component, model = $model, position = $position")
            }
        }

        val testAllDelegate = makeDelegate<TestAllModel>(R.layout.component_test_all_feature) {
            onBind { component, model, position ->
                with(component) {
                    bindButton(R.id.testFireEvent) {
                        setOnClickListener {
                            toast("testAllDelegate clicked button")
                        }
                    }
                }
            }

            onViewAttachedToWindow {
                Log.d(TAG, "onViewAttachedToWindow() called")
            }

            onViewDetachedFromWindow {
                Log.d(TAG, "onViewDetachedFromWindow() called")
            }
        }

        adapter.registerAdapterDelegates(simpleTextDelegate, testAllDelegate)
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

//    override fun createRefreshData(size: Int): MutableList<Any> {
//        return mockMultiTypeModels()
//    }
}