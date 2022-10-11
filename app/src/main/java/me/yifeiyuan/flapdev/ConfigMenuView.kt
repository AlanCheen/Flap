package me.yifeiyuan.flapdev

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import me.yifeiyuan.flapdev.databinding.DebugMenuBinding

/**
 * Created by 程序亦非猿 on 2022/10/11.
 */
class ConfigMenuView : FrameLayout {

    constructor(context: Context) : super(context)

    private val binding: DebugMenuBinding = DebugMenuBinding.inflate(LayoutInflater.from(context), this, true)

    init {

        binding.orientationGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.horizontal -> {
                    callback?.onOrientationChanged(RecyclerView.HORIZONTAL)
                }
                R.id.vertical -> {
                    callback?.onOrientationChanged(RecyclerView.VERTICAL)
                }
            }
        }

        //0 Linear
        //1 Grid
        //2 Staggered
        //3 IndexedStaggered
        binding.layoutManagerGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.linear -> {
                    callback?.onLayoutManagerChanged(0)
                }
                R.id.grid -> {
                    callback?.onLayoutManagerChanged(1)
                }
                R.id.staggered -> {
                    callback?.onLayoutManagerChanged(2)
                }
                R.id.indexedStaggered -> {
                    callback?.onLayoutManagerChanged(3)
                }
            }
        }
    }

    var callback: Callback? = null

    interface Callback {
        fun onOrientationChanged(orientation: Int)
        fun onLayoutManagerChanged(type: Int)
        fun onPreloadChanged(direction: Int, enable: Boolean)
    }
}