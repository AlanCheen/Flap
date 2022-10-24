package me.yifeiyuan.flap.widget

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.view.NestedScrollingParent3
import androidx.core.view.NestedScrollingParentHelper
import androidx.core.view.ViewCompat

/**
 * Created by 程序亦非猿 on 2022/10/24.
 *
 * https://stackoverflow.com/questions/68200773/vertical-recyclerview-nested-inside-vertical-recyclerview
 *
 * https://issuetracker.google.com/issues/120814730
 *
 * Chris Banes: It becomes hard to implement this correctly when the parent recycles views. What happens if only one child wants to handle NS? Lots of edge cases here, and I’m not totally sold on the use cases I’ve heard.
 *
 */
class FlapNestedRecyclerView
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FlapRecyclerView(context, attrs, defStyleAttr),
        NestedScrollingParent3 {

    private var nestedScrollTarget: View? = null
    private var nestedScrollTargetWasUnableToScroll = false
    private val parentHelper by lazy { NestedScrollingParentHelper(this) }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        // Nothing special if no child scrolling target.
        if (nestedScrollTarget == null) return super.dispatchTouchEvent(ev)

        // Inhibit the execution of our onInterceptTouchEvent for now...
        requestDisallowInterceptTouchEvent(true)
        // ... but do all other processing.
        var handled = super.dispatchTouchEvent(ev)

        // If the first dispatch yielded an unhandled event or the descendant view is unable to
        // scroll in the direction the user is scrolling, we dispatch once more but without skipping
        // our onInterceptTouchEvent. Note that RecyclerView automatically cancels active touches of
        // all its descendants once it starts scrolling so we don't have to do that.
        requestDisallowInterceptTouchEvent(false)
        if (!handled || nestedScrollTargetWasUnableToScroll) {
            handled = super.dispatchTouchEvent(ev)
        }

        return handled
    }

    // We only support vertical scrolling.
    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int) =
            nestedScrollAxes and ViewCompat.SCROLL_AXIS_VERTICAL != 0

    /*  Introduced with NestedScrollingParent2. */
    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int) =
            onStartNestedScroll(child, target, axes)

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int) {
        if (axes and View.SCROLL_AXIS_VERTICAL != 0) {
            // A descendant started scrolling, so we'll observe it.
            setTarget(target)
        }
        parentHelper.onNestedScrollAccepted(child, target, axes)
    }

    /*  Introduced with NestedScrollingParent2. */
    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
        if (axes and View.SCROLL_AXIS_VERTICAL != 0) {
            // A descendant started scrolling, so we'll observe it.
            setTarget(target)
        }
        parentHelper.onNestedScrollAccepted(child, target, axes, type)
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            dispatchNestedPreScroll(dx, dy, consumed, null)
        } else {
            super.onNestedPreScroll(target, dx, dy, consumed)
        }
    }

    /*  Introduced with NestedScrollingParent2. */
    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        onNestedPreScroll(target, dx, dy, consumed)
    }

    override fun onNestedScroll(
            target: View,
            dxConsumed: Int,
            dyConsumed: Int,
            dxUnconsumed: Int,
            dyUnconsumed: Int
    ) {
        if (target === nestedScrollTarget && dyUnconsumed != 0) {
            // The descendant could not fully consume the scroll. We remember that in order
            // to allow the RecyclerView to take over scrolling.
            nestedScrollTargetWasUnableToScroll = true
            // Let the parent start to consume scroll events.
            target.parent?.requestDisallowInterceptTouchEvent(false)
        }
    }

    /*  Introduced with NestedScrollingParent2. */
    override fun onNestedScroll(
            target: View,
            dxConsumed: Int,
            dyConsumed: Int,
            dxUnconsumed: Int,
            dyUnconsumed: Int,
            type: Int
    ) {
        onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
    }

    /*  Introduced with NestedScrollingParent3. */
    override fun onNestedScroll(
            target: View,
            dxConsumed: Int,
            dyConsumed: Int,
            dxUnconsumed: Int,
            dyUnconsumed: Int,
            type: Int,
            consumed: IntArray
    ) {
        onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
    }

    /* From ViewGroup */
    override fun onStopNestedScroll(child: View) {
        // The descendant finished scrolling. Clean up!
        setTarget(null)
        parentHelper.onStopNestedScroll(child)
    }

    /*  Introduced with NestedScrollingParent2. */
    override fun onStopNestedScroll(target: View, type: Int) {
        // The descendant finished scrolling. Clean up!
        setTarget(null)
        parentHelper.onStopNestedScroll(target, type)
    }

    /*  Introduced with NestedScrollingParent2. */
    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            false
        } else {
            super.onNestedPreFling(target, velocityX, velocityY)
        }
    }

    /* In ViewGroup for API 21+. */
    override fun onNestedFling(
            target: View,
            velocityX: Float,
            velocityY: Float,
            consumed: Boolean
    ) =
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                false
            } else {
                super.onNestedFling(target, velocityX, velocityY, consumed)
            }

    private fun setTarget(target: View?) {
        nestedScrollTarget = target
        nestedScrollTargetWasUnableToScroll = false
    }
}