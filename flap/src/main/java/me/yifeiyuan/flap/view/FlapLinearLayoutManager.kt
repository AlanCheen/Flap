package me.yifeiyuan.flap.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flap.FlapDebug

/**
 * Created by 程序亦非猿 on 2021/9/30.
 */
open class FlapLinearLayoutManager : LinearLayoutManager {

    companion object {
        private const val TAG = "FlapLinearLayoutManager"
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, orientation: Int, reverseLayout: Boolean) : super(
            context,
            orientation,
            reverseLayout
    )

    constructor(
            context: Context?,
            attrs: AttributeSet?,
            defStyleAttr: Int,
            defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun getExtraLayoutSpace(state: RecyclerView.State?): Int {
        return super.getExtraLayoutSpace(state)
    }

    override fun getInitialPrefetchItemCount(): Int {
        return super.getInitialPrefetchItemCount()
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: Exception) {
            e.printStackTrace()
            FlapDebug.e(TAG, "onLayoutChildren: ", e)
        }
    }
}