package me.yifeiyuan.flapdev.components

import android.util.Log
import me.yifeiyuan.flap.dsl.viewbinding.adapterDelegateViewBinding
import me.yifeiyuan.flapdev.FlapApplication.Companion.toast
import me.yifeiyuan.flapdev.databinding.FlapItemVbBinding

/**
 *
 * 测试使用了 ViewBinding 的 Component
 * 使用 ViewBinding 的例子
 * Created by 程序亦非猿 on 2020/9/30.
 */

class ViewBindingModel

private const val TAG = "ViewBindingComponent"

fun createViewBindingDelegate() = adapterDelegateViewBinding<ViewBindingModel, FlapItemVbBinding>({ layoutInflater, parent -> FlapItemVbBinding.inflate(layoutInflater, parent, false) }) {

    onBind { model ->
        binding.tvContent.text = "adapterDelegateViewBinding DSL 支持 ViewBinding"
    }

    onClick { model, position ->
        toast("viewBindingDelegate onClick() called with: component = $this, model = $model, position = $position")
    }

    onResume {
        Log.d(TAG, "viewBindingDelegate onResume() called")
    }

    onPause {
        Log.d(TAG, "viewBindingDelegate onPause() called")
    }
}