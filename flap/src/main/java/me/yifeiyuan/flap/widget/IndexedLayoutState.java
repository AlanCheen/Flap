package me.yifeiyuan.flap.widget;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by 程序亦非猿 on 2022/9/22.
 */
class IndexedLayoutState {

    static final int LAYOUT_START = -1;

    static final int LAYOUT_END = 1;

    static final int INVALID_LAYOUT = Integer.MIN_VALUE;

    static final int ITEM_DIRECTION_HEAD = -1;

    static final int ITEM_DIRECTION_TAIL = 1;

    /**
     * We may not want to recycle children in some cases (e.g. layout)
     */
    boolean mRecycle = true;

    /**
     * Number of pixels that we should fill, in the layout direction.
     */
    int mAvailable;

    /**
     * Current position on the adapter to get the next item.
     */
    int mCurrentPosition;

    /**
     * Defines the direction in which the data adapter is traversed.
     * Should be {@link #ITEM_DIRECTION_HEAD} or {@link #ITEM_DIRECTION_TAIL}
     */
    int mItemDirection;

    /**
     * Defines the direction in which the layout is filled.
     * Should be {@link #LAYOUT_START} or {@link #LAYOUT_END}
     */
    int mLayoutDirection;

    /**
     * This is the target pixel closest to the start of the layout that we are trying to fill
     */
    int mStartLine = 0;

    /**
     * This is the target pixel closest to the end of the layout that we are trying to fill
     */
    int mEndLine = 0;

    /**
     * If true, layout should stop if a focusable view is added
     */
    boolean mStopInFocusable;

    /**
     * If the content is not wrapped with any value
     */
    boolean mInfinite;

    /**
     * @return true if there are more items in the data adapter
     */
    boolean hasMore(RecyclerView.State state) {
        return mCurrentPosition >= 0 && mCurrentPosition < state.getItemCount();
    }

    /**
     * Gets the view for the next element that we should render.
     * Also updates current item index to the next item, based on {@link #mItemDirection}
     *
     * @return The next element that we should render.
     */
    View next(RecyclerView.Recycler recycler) {
        final View view = recycler.getViewForPosition(mCurrentPosition);
        mCurrentPosition += mItemDirection;
        return view;
    }

    @Override
    public String toString() {
        return "LayoutState{"
                + "mAvailable=" + mAvailable
                + ", mCurrentPosition=" + mCurrentPosition
                + ", mItemDirection=" + mItemDirection
                + ", mLayoutDirection=" + mLayoutDirection
                + ", mStartLine=" + mStartLine
                + ", mEndLine=" + mEndLine
                + '}';
    }
}
