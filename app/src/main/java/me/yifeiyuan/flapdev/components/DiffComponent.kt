package me.yifeiyuan.flapdev.components

import android.view.View
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flap.annotations.Delegate
import me.yifeiyuan.flap.delegate.AdapterDelegate
import me.yifeiyuan.flap.diff.DiffModel
import me.yifeiyuan.flap.ext.bindTextView
import me.yifeiyuan.flapdev.R

/**
 * Created by 程序亦非猿 on 2022/8/1.
 */

class TestDiffModel(var content: String, var age: Int, var desc: String) : DiffModel {

    override fun areItemsTheSame(other: DiffModel): Boolean {
        return false
    }

    override fun areContentsTheSame(other: DiffModel): Boolean {
        return false
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun toString(): String {
        return super.toString()
    }
}

@Delegate(layoutId = R.layout.component_diff)
class DiffComponent(view: View) : Component<TestDiffModel>(view) {

    override fun onBind(model: TestDiffModel) {
    }

    override fun onBind(model: TestDiffModel, position: Int, payloads: List<Any>, adapter: FlapAdapter, delegate: AdapterDelegate<*, *>) {

        bindTextView(R.id.content){

        }
        bindTextView(R.id.age){

        }
        bindTextView(R.id.content){

        }
    }

}