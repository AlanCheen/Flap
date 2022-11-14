package me.yifeiyuan.flapdev.components

import me.yifeiyuan.flap.differ.IDiffer
import me.yifeiyuan.flap.dsl.adapterDelegate
import me.yifeiyuan.flap.ext.bindTextView
import me.yifeiyuan.flapdev.FlapApplication.Companion.application
import me.yifeiyuan.flapdev.R
import me.yifeiyuan.flapdev.toPixel
import kotlin.random.Random

/**
 *
 * 由 Model 属性决定 Component 的功能开关
 *
 * Created by 程序亦非猿 on 2022/9/13.
 */

class TestConfigModel : IDiffer {

    var id: Int = -1
    var title: String? = null
    var content: String? = null

    var dragEnable: Boolean = false
    var swipeEnable: Boolean = false

    var swipeFlags: Int = 0
    var dragFlags: Int = 0

    var clickEnable: Boolean = false
    var longClickEnable: Boolean = false

    var height: Int = -1

    init {
        height = Random.nextInt(application!!.toPixel(60), application!!.toPixel(120))
    }

    override fun areItemsTheSame(newItem: Any): Boolean {
        return false
    }

    override fun areContentsTheSame(newItem: Any): Boolean {
        return false
    }

    override fun getChangePayload(newItem: Any): Any? {
        return super.getChangePayload(newItem)
    }
}

fun createFullConfigAdapterDelegate() = adapterDelegate<TestConfigModel>(R.layout.component_full_feature) {

    onBind { model, position, payloads ->

        bindTextView(R.id.title) {
            text = model.title
        }

        bindTextView(R.id.content) {
            text = model.content
        }

        swipeEnable = model.swipeEnable
        dragEnable = model.dragEnable
        swipeFlags = model.swipeFlags
        dragFlags = model.dragFlags
        clickable = model.clickEnable
        longClickable = model.longClickEnable

        if (model.height > 0) {
            val lp = itemView.layoutParams.apply {
                height = model.height
            }
            itemView.layoutParams = lp
        }
    }
}