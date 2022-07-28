package me.yifeiyuan.flapdev.components

import android.view.View
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.annotations.Delegate
import me.yifeiyuan.flap.ktx.bindTextView
import me.yifeiyuan.flapdev.R

/**
 *
 * Created by 程序亦非猿 on 2022/7/27.
 */

class TestClickModel(var content: String)

@Delegate(layoutId = R.layout.component_test_click)
class TestClickComponent(itemView: View) : Component<TestClickModel>(itemView) {
    override fun onBind(model: TestClickModel) {
        bindTextView(R.id.clicks){
            text = model.content
        }
    }
}