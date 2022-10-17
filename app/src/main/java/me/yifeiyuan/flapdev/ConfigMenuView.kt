package me.yifeiyuan.flapdev

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.SeekBar
import androidx.recyclerview.widget.RecyclerView
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
                R.id.stickyHeader->{
                    callback?.onLayoutManagerChanged(4)
                }
            }
        }

        binding.clearAll.setOnClickListener {
            callback?.onClearAllData()
        }
        binding.addTopData.setOnClickListener {
            callback?.onAddDataToTop()
        }
        binding.resetData.setOnClickListener {
            callback?.onResetData()
        }
        binding.appendData.setOnClickListener {
            callback?.onAppendData()
        }
        binding.addZeroHeightData.setOnClickListener {
            callback?.onAddZeroHeightData()
        }

        binding.spanCount.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                callback?.onSpanCountChanged(seekBar.progress)
            }
        })

        binding.toggleSkeleton.setOnCheckedChangeListener { buttonView, isChecked ->
            callback?.onSkeletonVisibilityChanged(isChecked)
        }
    }

    var callback: Callback? = null

    interface Callback {
        fun onOrientationChanged(orientation: Int)
        fun onLayoutManagerChanged(type: Int)
        fun onPreloadChanged(direction: Int, enable: Boolean)
        fun onClearAllData()
        fun onAddDataToTop()
        fun onResetData()
        fun onAppendData()
        fun onSpanCountChanged(spanCount: Int)
        fun onSkeletonVisibilityChanged(show: Boolean)
        fun onAddZeroHeightData()
    }
}