package me.yifeiyuan.flap.widget;

import android.view.View;

import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 代码基于 StaggeredGridLayoutManager 用到的 ScrollbarHelper 修改
 * Created by 程序亦非猿 on 2022/9/22.
 */
class IndexedScrollbarHelper {

    /**
     * @param startChild View closest to start of the list. (top or left)
     * @param endChild   View closest to end of the list (bottom or right)
     */
    static int computeScrollOffset(RecyclerView.State state, OrientationHelper orientation,
                                   View startChild, View endChild, RecyclerView.LayoutManager lm,
                                   boolean smoothScrollbarEnabled, boolean reverseLayout) {
        if (lm.getChildCount() == 0 || state.getItemCount() == 0 || startChild == null
                || endChild == null) {
            return 0;
        }
        final int minPosition = Math.min(lm.getPosition(startChild),
                lm.getPosition(endChild));
        final int maxPosition = Math.max(lm.getPosition(startChild),
                lm.getPosition(endChild));
        final int itemsBefore = reverseLayout
                ? Math.max(0, state.getItemCount() - maxPosition - 1)
                : Math.max(0, minPosition);
        if (!smoothScrollbarEnabled) {
            return itemsBefore;
        }
        final int laidOutArea = Math.abs(orientation.getDecoratedEnd(endChild)
                - orientation.getDecoratedStart(startChild));
        final int itemRange = Math.abs(lm.getPosition(startChild)
                - lm.getPosition(endChild)) + 1;
        final float avgSizePerRow = (float) laidOutArea / itemRange;

        return Math.round(itemsBefore * avgSizePerRow + (orientation.getStartAfterPadding()
                - orientation.getDecoratedStart(startChild)));
    }

    /**
     * @param startChild View closest to start of the list. (top or left)
     * @param endChild   View closest to end of the list (bottom or right)
     */
    static int computeScrollExtent(RecyclerView.State state, OrientationHelper orientation,
                                   View startChild, View endChild, RecyclerView.LayoutManager lm,
                                   boolean smoothScrollbarEnabled) {
        if (lm.getChildCount() == 0 || state.getItemCount() == 0 || startChild == null
                || endChild == null) {
            return 0;
        }
        if (!smoothScrollbarEnabled) {
            return Math.abs(lm.getPosition(startChild) - lm.getPosition(endChild)) + 1;
        }
        final int extend = orientation.getDecoratedEnd(endChild)
                - orientation.getDecoratedStart(startChild);
        return Math.min(orientation.getTotalSpace(), extend);
    }

    /**
     * @param startChild View closest to start of the list. (top or left)
     * @param endChild   View closest to end of the list (bottom or right)
     */
    static int computeScrollRange(RecyclerView.State state, OrientationHelper orientation,
                                  View startChild, View endChild, RecyclerView.LayoutManager lm,
                                  boolean smoothScrollbarEnabled) {
        if (lm.getChildCount() == 0 || state.getItemCount() == 0 || startChild == null
                || endChild == null) {
            return 0;
        }
        if (!smoothScrollbarEnabled) {
            return state.getItemCount();
        }
        // smooth scrollbar enabled. try to estimate better.
        final int laidOutArea = orientation.getDecoratedEnd(endChild)
                - orientation.getDecoratedStart(startChild);
        final int laidOutRange = Math.abs(lm.getPosition(startChild)
                - lm.getPosition(endChild))
                + 1;
        // estimate a size for full list.
        return (int) ((float) laidOutArea / laidOutRange * state.getItemCount());
    }

}
