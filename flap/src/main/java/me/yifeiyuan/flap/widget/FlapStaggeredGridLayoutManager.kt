package me.yifeiyuan.flap.widget

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import me.yifeiyuan.flap.FlapDebug

/**
 * Created by 程序亦非猿 on 2021/9/30.
 * @since 3.0.0
 */
open class FlapStaggeredGridLayoutManager
    : StaggeredGridLayoutManager {

    companion object {
        private const val TAG = "FlapStaggeredGridLayoutManager"
    }

    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) : super(context, attrs, defStyleAttr, defStyleRes)

    constructor(spanCount: Int, orientation: Int = RecyclerView.VERTICAL) : super(spanCount, orientation)

    var supportsPredictiveItemAnimations = false

    init {
        gapStrategy = GAP_HANDLING_NONE
    }

    /**
     * Disable predictive animations. There is a bug in RecyclerView which causes views that
     * are being reloaded to pull invalid ViewHolders from the internal recycler stack if the
     * adapter size has decreased since the ViewHolder was recycled.
     *
     * https://stackoverflow.com/questions/30220771/recyclerview-inconsistency-detected-invalid-item-position
     */
    override fun supportsPredictiveItemAnimations(): Boolean {
        return supportsPredictiveItemAnimations
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: Exception) {
            e.printStackTrace()
            FlapDebug.e(TAG, "onLayoutChildren: ", e)
        }
    }

    override fun onScrollStateChanged(state: Int) {
        try {
            super.onScrollStateChanged(state)
        } catch (e: Exception) {
            e.printStackTrace()
            FlapDebug.e(TAG, "onScrollStateChanged: ", e)
        }
    }
}