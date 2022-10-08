package me.yifeiyuan.flap.delegate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.FlapDebug

/**
 * FallbackAdapterDelegate is a build-in AdapterDelegate that would be used when something went wrong.
 * So that Flap won't crash your App.
 *
 * FallbackAdapterDelegate 是一个默认代理所有数据模型的 AdapterDelegate，可以用来处理未知模型兜底逻辑。
 *
 * 如果一个 model 没有任何 AdapterDelegate 代理，那么 FallbackAdapterDelegate 会负责接手处理。
 *
 * 开发者也可以自己定义自己的 FallbackAdapterDelegate。
 *
 * @see me.yifeiyuan.flap.Flap.globalFallbackAdapterDelegate
 * @see me.yifeiyuan.flap.FlapAdapter.defaultAdapterDelegate
 * @see FallbackComponent
 *
 * Created by 程序亦非猿 on 2021/9/22.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 2020/9/22
 * @since 3.0.0
 */
internal class FallbackAdapterDelegate : AdapterDelegate<Any, FallbackComponent> {

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): FallbackComponent {
        return FallbackComponent(TextView(parent.context))
    }

    override fun delegate(model: Any): Boolean {
        return true
    }

}

internal class FallbackComponent(v: View) : Component<Any>(v) {
    override fun onBind(model: Any) {
        if (FlapDebug.isDebug) {
            (itemView as TextView).run {
                text = "model : $model 没有对应的 AdapterDelegate ，请注册，position = $position，该信息只有开启 Debug 模式才会展示。"
            }
        }
    }

    override fun isClickable(): Boolean {
        return false
    }

    override fun isLongClickable(): Boolean {
        return false
    }

    override fun isDragEnabled(): Boolean {
        return false
    }

    override fun isSwipeEnabled(): Boolean {
        return false
    }
}
