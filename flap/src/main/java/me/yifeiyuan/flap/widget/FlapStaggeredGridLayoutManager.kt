package me.yifeiyuan.flap.widget

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import me.yifeiyuan.flap.FlapDebug

/**
 * Created by 程序亦非猿 on 2021/9/30.
 *
 */
open class FlapStaggeredGridLayoutManager
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0)
    : StaggeredGridLayoutManager(context, attrs, defStyleAttr, defStyleRes) {

    companion object {
        private const val TAG = "FlapLinearLayoutManager"
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