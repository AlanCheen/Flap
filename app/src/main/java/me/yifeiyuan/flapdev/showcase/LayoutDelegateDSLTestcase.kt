package me.yifeiyuan.flapdev.showcase

import android.util.Log
import android.view.View
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flap.delegate.delegateBuilder
import me.yifeiyuan.flap.ext.bindTextView
import me.yifeiyuan.flapdev.R
import me.yifeiyuan.flapdev.components.SimpleTextModel

private const val TAG = "LayoutDelegateDSLTest"

/**
 * Created by 程序亦非猿 on 2022/8/4.
 */
class LayoutDelegateDSLTestcase : BaseTestcaseFragment() {

    override fun onInit(view: View) {
        super.onInit(view)

        adapter.registerAdapterDelegates(

                delegateBuilder<SimpleTextModel> {

                    layoutId(R.layout.flap_item_simple_text)

                    onLongClick { component, model, position ->
                        Log.d(TAG, "onLongClick() called with: component = $component, model = $model, position = $position")
                        toast("onLongClick() called with: component = $component, model = $model, position = $position")
                        false
                    }

                    onClick { component, model, position ->
                        Log.d(TAG, "onClick() called with: component = $component, model = $model, position = $position")
                        toast("onClick() called with: component = $component, model = $model, position = $position")
                    }

                    onBind { component, model ->
                        component.bindTextView(R.id.tv_content) {
                            text = model.content
                        }
                    }

                    onViewAttachedToWindow {
                        Log.d(TAG, "onViewAttachedToWindow() called")
                    }

                    onViewDetachedFromWindow {
                        Log.d(TAG, "onViewDetachedFromWindow() called")
                    }
                }
        )
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