package me.yifeiyuan.flapdev.components

import android.view.View
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.annotations.Delegate
import me.yifeiyuan.flapdev.R

/**
 *
 * 高度为 0 的组件
 * Created by 程序亦非猿 on 2022/8/4.
 */

class ZeroHeightModel

@Delegate(layoutId = R.layout.component_zero_height)
class ZeroHeightComponent(view: View) : Component<ZeroHeightModel>(view) {
    override fun onBind(model: ZeroHeightModel) {
    }
}