package me.yifeiyuan.flapdev.showcase

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import androidx.core.graphics.drawable.DrawableCompat
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flap.delegate.LayoutAdapterDelegate
import me.yifeiyuan.flap.ext.bindTextView
import me.yifeiyuan.flap.ext.bindView
import me.yifeiyuan.flapdev.R
import me.yifeiyuan.flapdev.components.SimpleImageModel
import me.yifeiyuan.flapdev.components.SimpleTextModel
import me.yifeiyuan.flapdev.mockMultiTypeModels

private const val TAG = "LayoutDelegateTestcase"

/**
 * 测试 LayoutAdapterDelegate
 *
 * @see LayoutAdapterDelegate
 *
 * Created by 程序亦非猿 on 2022/8/4.
 */
class LayoutAdapterDelegateTestcase : BaseTestcaseFragment() {

    override fun onInit(view: View) {
        super.onInit(view)

        val simpleTextDelegate = LayoutAdapterDelegate(
                SimpleTextModel::class.java,
                R.layout.flap_item_simple_text,
        ) { component, model, position ->
            component.bindTextView(R.id.tv_content) {
                text = model.content
            }
        }

        val simpleImageDelegate = LayoutAdapterDelegate(SimpleImageModel::class.java,
                R.layout.flap_item_simple_image){ component, model, position ->
            with(component) {
                bindView<ImageView>(R.id.logo) {
                    DrawableCompat.setTint(drawable, Color.parseColor("#2211fff2"))

                    setOnClickListener {
                        toast("simpleImageDelegate clicked")
                    }
                }
            }
        }

        adapter.registerAdapterDelegates(simpleTextDelegate, simpleImageDelegate)
    }

    //先清理所有的全局 Delegate
    override fun createAdapter(): FlapAdapter {
        return super.createAdapter().apply {
            clearAdapterDelegates()
        }
    }

    override fun createRefreshData(size: Int): MutableList<Any> {
        return mockMultiTypeModels()
    }
}