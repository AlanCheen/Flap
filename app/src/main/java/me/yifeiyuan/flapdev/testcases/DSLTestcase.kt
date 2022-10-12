package me.yifeiyuan.flapdev.testcases

import android.util.Log
import android.view.View
import me.yifeiyuan.flap.dsl.adapterDelegate
import me.yifeiyuan.flap.dsl.adapterHook
import me.yifeiyuan.flap.ext.bindButton
import me.yifeiyuan.flapdev.R
import me.yifeiyuan.flapdev.components.TestAdapterApiModel
import me.yifeiyuan.flapdev.components.createDataBindingDelegate
import me.yifeiyuan.flapdev.mockMultiTypeModels

private const val TAG = "DSLTestcase"

/**
 * 测试 DSL
 *
 * Created by 程序亦非猿 on 2022/8/4.
 */
class DSLTestcase : BaseTestcaseFragment() {

    override fun onInit(view: View) {
        super.onInit(view)

        recyclerView.addItemDecoration(currentItemDecoration)

        val testAllDelegate = adapterDelegate<TestAdapterApiModel>(R.layout.component_test_all_feature) {

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
                Log.d(TAG, "TestAdapterApi onViewAttachedToWindow() called ,position=$position")
            }

            onViewDetachedFromWindow {
                Log.d(TAG, "TestAdapterApi onViewDetachedFromWindow() called ,position=$position")
            }

            onViewRecycled {
                Log.d(TAG, "TestAdapterApi onViewRecycled() called")
            }

            onFailedToRecycleView {
                Log.d(TAG, "TestAdapterApi onFailedToRecycleView() called")
                false
            }

            onResume {
                Log.d(TAG, "TestAdapterApi onResume() called")
            }

            onPause {
                Log.d(TAG, "TestAdapterApi onPause() called")
            }

            onStop {
                Log.d(TAG, "TestAdapterApi onStop() called")
            }

            onDestroy {
                Log.d(TAG, "TestAdapterApi onDestroy() called")
            }
        }

        val hook = adapterHook {
            onCreateViewHolderStart { adapter, delegate, viewType ->

            }
            onCreateViewHolderEnd { adapter, delegate, viewType, component ->

            }
            onBindViewHolderStart { adapter, delegate, component, data, position, payloads ->

            }
            onBindViewHolderEnd { adapter, delegate, component, data, position, payloads ->
                Log.d(TAG, "onBindViewHolderEnd() called with: adapter = $adapter, delegate = $delegate, component = $component, data = $data, position = $position, payloads = $payloads")
            }
            onViewAttachedToWindow { adapter, delegate, component ->

            }

            onViewDetachedFromWindow { adapter, delegate, component ->

            }
        }

        val dataBindingDelegate = createDataBindingDelegate()

        adapter.registerAdapterDelegates(0, testAllDelegate, dataBindingDelegate)

        adapter.registerAdapterHook(hook)
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