package me.yifeiyuan.flapdev

import android.view.View
import me.yifeiyuan.flap.decoration.LinearSpaceItemDecoration
import me.yifeiyuan.flap.dsl.adapterDelegate
import me.yifeiyuan.flap.ext.bindTextView
import me.yifeiyuan.flapdev.components.SimpleTextModel
import me.yifeiyuan.flapdev.testcases.BaseTestcaseFragment

/**
 * GitHub 代码示例
 *
 * Created by 程序亦非猿 on 2022/8/9.
 */
class GitHubDemoFragment : BaseTestcaseFragment() {

    override fun onInit(view: View) {
        super.onInit(view)

        recyclerView.addItemDecoration(LinearSpaceItemDecoration(requireActivity().toPixel(6)))

        val simpleTextDelegate = adapterDelegate<SimpleTextModel>(R.layout.flap_component_simple_text) {
            onBind { model ->
                bindTextView(R.id.tv_content) {
                    text = model.content
                }
            }
        }

        adapter.registerAdapterDelegate(simpleTextDelegate)

        val dataList = ArrayList<Any>()

        dataList.add(SimpleTextModel("Android"))
        dataList.add(SimpleTextModel("Java"))
        dataList.add(SimpleTextModel("Kotlin"))

        adapter.setDataAndNotify(dataList)
    }

    override fun createRefreshData(size: Int): MutableList<Any> {
        return mutableListOf(
                SimpleTextModel("Android"),
                SimpleTextModel("Java"),
                SimpleTextModel("Kotlin")
        )
    }
}