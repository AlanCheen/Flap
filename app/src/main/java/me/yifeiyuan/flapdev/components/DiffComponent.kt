package me.yifeiyuan.flapdev.components

import android.os.SystemClock
import android.view.View
import android.widget.Button
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flap.annotations.Delegate
import me.yifeiyuan.flap.delegate.AdapterDelegate
import me.yifeiyuan.flap.differ.IDiffer
import me.yifeiyuan.flap.ext.bindButton
import me.yifeiyuan.flap.ext.bindTextView
import me.yifeiyuan.flapdev.R

/**
 * Created by 程序亦非猿 on 2022/8/1.
 *
 * 如果都返回 true ，内容修改后再下拉刷新，不会有 onbind 行为
 */

class TestDiffModel(var content: String, var id: Int, var desc: String) : IDiffer {

    override fun areItemsTheSame(newItem: Any): Boolean {
        if (newItem.javaClass == TestDiffModel::class.java) {
            return id == (newItem as TestDiffModel).id
        } else {
            return false
        }
//        return true
    }

    override fun areContentsTheSame(newItem: Any): Boolean {
        return content == (newItem as TestDiffModel).content
//        return true
    }

    override fun getChangePayload(newItem: Any): Any? {
        return (newItem as TestDiffModel).content
    }

    override fun toString(): String {
        return "(id=$id,content='$content',desc='$desc')TestDiffModel"
    }
}

@Delegate(layoutId = R.layout.component_diff)
class DiffComponent(view: View) : Component<TestDiffModel>(view) {

    override fun onBind(model: TestDiffModel) {
    }

    override fun onBind(model: TestDiffModel, position: Int, payloads: List<Any>, adapter: FlapAdapter, delegate: AdapterDelegate<*, *>) {

        if (payloads.isNotEmpty()) {
            bindTextView(R.id.content) {
                text = "展示 content ：${payloads.get(0)}"
            }

            //当 payloads 更新时，事件点击需要重新设置
            bindButton(R.id.modifyContent) {
                setOnClickListener {
                    model.content = "修改后 Content:" + (SystemClock.uptimeMillis() % 10000).toInt().toString()
                    adapter.notifyItemChanged(position)
                }
            }

            bindButton(R.id.modifyId) {
                setOnClickListener {
                    model.id = (SystemClock.uptimeMillis() % 10000).toInt()
                    adapter.notifyItemChanged(position)
                }
            }
            return
        }

        bindTextView(R.id.content) {
            text = "展示 content ：${model.content}"
        }

        bindTextView(R.id.id) {
            text = "展示 ID ：${model.id}"
        }

        bindTextView(R.id.desc) {
            text = "展示 desc ：${model.desc}"
        }

        bindButton(R.id.modifyId) {
            setOnClickListener {
                model.id = (SystemClock.uptimeMillis() % 10000).toInt()
                adapter.notifyItemChanged(position)
            }
        }

        bindButton(R.id.modifyContent) {
            setOnClickListener {
                model.content = "修改后 Content:" + (SystemClock.uptimeMillis() % 10000).toInt().toString()
                adapter.notifyItemChanged(position)
            }
        }
    }

}