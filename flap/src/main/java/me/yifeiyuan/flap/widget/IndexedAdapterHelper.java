package me.yifeiyuan.flap.widget;

import android.util.Log;

import androidx.core.util.Pools;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by 程序亦非猿 on 2022/9/22.
 */
class IndexedAdapterHelper implements IndexedOpReorderer.Callback {

    static final int POSITION_TYPE_INVISIBLE = 0;

    static final int POSITION_TYPE_NEW_OR_LAID_OUT = 1;

    private static final boolean DEBUG = false;

    private static final String TAG = "AHT";

    private Pools.Pool<IndexedAdapterHelper.UpdateOp> mUpdateOpPool = new Pools.SimplePool<>(IndexedAdapterHelper.UpdateOp.POOL_SIZE);

    final ArrayList<IndexedAdapterHelper.UpdateOp> mPendingUpdates = new ArrayList<>();

    final ArrayList<IndexedAdapterHelper.UpdateOp> mPostponedList = new ArrayList<>();

    IndexedAdapterHelper.Callback mCallback;

    Runnable mOnItemProcessedCallback;

    boolean mDisableRecycler;

    IndexedOpReorderer mOpReorderer;

    private int mExistingUpdateTypes = 0;

    IndexedAdapterHelper(IndexedAdapterHelper.Callback callback) {
        this(callback, false);
    }

    IndexedAdapterHelper(IndexedAdapterHelper.Callback callback, boolean disableRecycler) {
        mCallback = callback;
        mDisableRecycler = disableRecycler;
        mOpReorderer = new IndexedOpReorderer(this);
    }

    IndexedAdapterHelper() {
    }

    IndexedAdapterHelper addUpdateOp(IndexedAdapterHelper.UpdateOp... ops) {
        Collections.addAll(mPendingUpdates, ops);
        return this;
    }

    void reset() {
        recycleUpdateOpsAndClearList(mPendingUpdates);
        recycleUpdateOpsAndClearList(mPostponedList);
        mExistingUpdateTypes = 0;
    }

    void preProcess() {
        mOpReorderer.reorderOps(mPendingUpdates);
        final int count = mPendingUpdates.size();
        for (int i = 0; i < count; i++) {
            IndexedAdapterHelper.UpdateOp op = mPendingUpdates.get(i);
            switch (op.cmd) {
                case IndexedAdapterHelper.UpdateOp.ADD:
                    applyAdd(op);
                    break;
                case IndexedAdapterHelper.UpdateOp.REMOVE:
                    applyRemove(op);
                    break;
                case IndexedAdapterHelper.UpdateOp.UPDATE:
                    applyUpdate(op);
                    break;
                case IndexedAdapterHelper.UpdateOp.MOVE:
                    applyMove(op);
                    break;
            }
            if (mOnItemProcessedCallback != null) {
                mOnItemProcessedCallback.run();
            }
        }
        mPendingUpdates.clear();
    }

    void consumePostponedUpdates() {
        final int count = mPostponedList.size();
        for (int i = 0; i < count; i++) {
            mCallback.onDispatchSecondPass(mPostponedList.get(i));
        }
        recycleUpdateOpsAndClearList(mPostponedList);
        mExistingUpdateTypes = 0;
    }

    private void applyMove(IndexedAdapterHelper.UpdateOp op) {
        // MOVE ops are pre-processed so at this point, we know that item is still in the adapter.
        // otherwise, it would be converted into a REMOVE operation
        postponeAndUpdateViewHolders(op);
    }

    private void applyRemove(IndexedAdapterHelper.UpdateOp op) {
        int tmpStart = op.positionStart;
        int tmpCount = 0;
        int tmpEnd = op.positionStart + op.itemCount;
        int type = -1;
        for (int position = op.positionStart; position < tmpEnd; position++) {
            boolean typeChanged = false;
            RecyclerView.ViewHolder vh = mCallback.findViewHolder(position);
            if (vh != null || canFindInPreLayout(position)) {
                // If a ViewHolder exists or this is a newly added item, we can defer this update
                // to post layout stage.
                // * For existing ViewHolders, we'll fake its existence in the pre-layout phase.
                // * For items that are added and removed in the same process cycle, they won't
                // have any effect in pre-layout since their add ops are already deferred to
                // post-layout pass.
                if (type == POSITION_TYPE_INVISIBLE) {
                    // Looks like we have other updates that we cannot merge with this one.
                    // Create an UpdateOp and dispatch it to LayoutManager.
                    IndexedAdapterHelper.UpdateOp newOp = obtainUpdateOp(IndexedAdapterHelper.UpdateOp.REMOVE, tmpStart, tmpCount, null);
                    dispatchAndUpdateViewHolders(newOp);
                    typeChanged = true;
                }
                type = POSITION_TYPE_NEW_OR_LAID_OUT;
            } else {
                // This update cannot be recovered because we don't have a ViewHolder representing
                // this position. Instead, post it to LayoutManager immediately
                if (type == POSITION_TYPE_NEW_OR_LAID_OUT) {
                    // Looks like we have other updates that we cannot merge with this one.
                    // Create UpdateOp op and dispatch it to LayoutManager.
                    IndexedAdapterHelper.UpdateOp newOp = obtainUpdateOp(IndexedAdapterHelper.UpdateOp.REMOVE, tmpStart, tmpCount, null);
                    postponeAndUpdateViewHolders(newOp);
                    typeChanged = true;
                }
                type = POSITION_TYPE_INVISIBLE;
            }
            if (typeChanged) {
                position -= tmpCount; // also equal to tmpStart
                tmpEnd -= tmpCount;
                tmpCount = 1;
            } else {
                tmpCount++;
            }
        }
        if (tmpCount != op.itemCount) { // all 1 effect
            recycleUpdateOp(op);
            op = obtainUpdateOp(IndexedAdapterHelper.UpdateOp.REMOVE, tmpStart, tmpCount, null);
        }
        if (type == POSITION_TYPE_INVISIBLE) {
            dispatchAndUpdateViewHolders(op);
        } else {
            postponeAndUpdateViewHolders(op);
        }
    }

    private void applyUpdate(IndexedAdapterHelper.UpdateOp op) {
        int tmpStart = op.positionStart;
        int tmpCount = 0;
        int tmpEnd = op.positionStart + op.itemCount;
        int type = -1;
        for (int position = op.positionStart; position < tmpEnd; position++) {
            RecyclerView.ViewHolder vh = mCallback.findViewHolder(position);
            if (vh != null || canFindInPreLayout(position)) { // deferred
                if (type == POSITION_TYPE_INVISIBLE) {
                    IndexedAdapterHelper.UpdateOp newOp = obtainUpdateOp(IndexedAdapterHelper.UpdateOp.UPDATE, tmpStart, tmpCount,
                            op.payload);
                    dispatchAndUpdateViewHolders(newOp);
                    tmpCount = 0;
                    tmpStart = position;
                }
                type = POSITION_TYPE_NEW_OR_LAID_OUT;
            } else { // applied
                if (type == POSITION_TYPE_NEW_OR_LAID_OUT) {
                    IndexedAdapterHelper.UpdateOp newOp = obtainUpdateOp(IndexedAdapterHelper.UpdateOp.UPDATE, tmpStart, tmpCount,
                            op.payload);
                    postponeAndUpdateViewHolders(newOp);
                    tmpCount = 0;
                    tmpStart = position;
                }
                type = POSITION_TYPE_INVISIBLE;
            }
            tmpCount++;
        }
        if (tmpCount != op.itemCount) { // all 1 effect
            Object payload = op.payload;
            recycleUpdateOp(op);
            op = obtainUpdateOp(IndexedAdapterHelper.UpdateOp.UPDATE, tmpStart, tmpCount, payload);
        }
        if (type == POSITION_TYPE_INVISIBLE) {
            dispatchAndUpdateViewHolders(op);
        } else {
            postponeAndUpdateViewHolders(op);
        }
    }

    private void dispatchAndUpdateViewHolders(IndexedAdapterHelper.UpdateOp op) {
        // tricky part.
        // traverse all postpones and revert their changes on this op if necessary, apply updated
        // dispatch to them since now they are after this op.
        if (op.cmd == IndexedAdapterHelper.UpdateOp.ADD || op.cmd == IndexedAdapterHelper.UpdateOp.MOVE) {
            throw new IllegalArgumentException("should not dispatch add or move for pre layout");
        }
        if (DEBUG) {
            Log.d(TAG, "dispatch (pre)" + op);
            Log.d(TAG, "postponed state before:");
            for (IndexedAdapterHelper.UpdateOp updateOp : mPostponedList) {
                Log.d(TAG, updateOp.toString());
            }
            Log.d(TAG, "----");
        }

        // handle each pos 1 by 1 to ensure continuity. If it breaks, dispatch partial
        // TODO Since move ops are pushed to end, we should not need this anymore
        int tmpStart = updatePositionWithPostponed(op.positionStart, op.cmd);
        if (DEBUG) {
            Log.d(TAG, "pos:" + op.positionStart + ",updatedPos:" + tmpStart);
        }
        int tmpCnt = 1;
        int offsetPositionForPartial = op.positionStart;
        final int positionMultiplier;
        switch (op.cmd) {
            case IndexedAdapterHelper.UpdateOp.UPDATE:
                positionMultiplier = 1;
                break;
            case IndexedAdapterHelper.UpdateOp.REMOVE:
                positionMultiplier = 0;
                break;
            default:
                throw new IllegalArgumentException("op should be remove or update." + op);
        }
        for (int p = 1; p < op.itemCount; p++) {
            final int pos = op.positionStart + (positionMultiplier * p);
            int updatedPos = updatePositionWithPostponed(pos, op.cmd);
            if (DEBUG) {
                Log.d(TAG, "pos:" + pos + ",updatedPos:" + updatedPos);
            }
            boolean continuous = false;
            switch (op.cmd) {
                case IndexedAdapterHelper.UpdateOp.UPDATE:
                    continuous = updatedPos == tmpStart + 1;
                    break;
                case IndexedAdapterHelper.UpdateOp.REMOVE:
                    continuous = updatedPos == tmpStart;
                    break;
            }
            if (continuous) {
                tmpCnt++;
            } else {
                // need to dispatch this separately
                IndexedAdapterHelper.UpdateOp tmp = obtainUpdateOp(op.cmd, tmpStart, tmpCnt, op.payload);
                if (DEBUG) {
                    Log.d(TAG, "need to dispatch separately " + tmp);
                }
                dispatchFirstPassAndUpdateViewHolders(tmp, offsetPositionForPartial);
                recycleUpdateOp(tmp);
                if (op.cmd == IndexedAdapterHelper.UpdateOp.UPDATE) {
                    offsetPositionForPartial += tmpCnt;
                }
                tmpStart = updatedPos; // need to remove previously dispatched
                tmpCnt = 1;
            }
        }
        Object payload = op.payload;
        recycleUpdateOp(op);
        if (tmpCnt > 0) {
            IndexedAdapterHelper.UpdateOp tmp = obtainUpdateOp(op.cmd, tmpStart, tmpCnt, payload);
            if (DEBUG) {
                Log.d(TAG, "dispatching:" + tmp);
            }
            dispatchFirstPassAndUpdateViewHolders(tmp, offsetPositionForPartial);
            recycleUpdateOp(tmp);
        }
        if (DEBUG) {
            Log.d(TAG, "post dispatch");
            Log.d(TAG, "postponed state after:");
            for (IndexedAdapterHelper.UpdateOp updateOp : mPostponedList) {
                Log.d(TAG, updateOp.toString());
            }
            Log.d(TAG, "----");
        }
    }

    void dispatchFirstPassAndUpdateViewHolders(IndexedAdapterHelper.UpdateOp op, int offsetStart) {
        mCallback.onDispatchFirstPass(op);
        switch (op.cmd) {
            case IndexedAdapterHelper.UpdateOp.REMOVE:
                mCallback.offsetPositionsForRemovingInvisible(offsetStart, op.itemCount);
                break;
            case IndexedAdapterHelper.UpdateOp.UPDATE:
                mCallback.markViewHoldersUpdated(offsetStart, op.itemCount, op.payload);
                break;
            default:
                throw new IllegalArgumentException("only remove and update ops can be dispatched"
                        + " in first pass");
        }
    }

    private int updatePositionWithPostponed(int pos, int cmd) {
        final int count = mPostponedList.size();
        for (int i = count - 1; i >= 0; i--) {
            IndexedAdapterHelper.UpdateOp postponed = mPostponedList.get(i);
            if (postponed.cmd == IndexedAdapterHelper.UpdateOp.MOVE) {
                int start, end;
                if (postponed.positionStart < postponed.itemCount) {
                    start = postponed.positionStart;
                    end = postponed.itemCount;
                } else {
                    start = postponed.itemCount;
                    end = postponed.positionStart;
                }
                if (pos >= start && pos <= end) {
                    //i'm affected
                    if (start == postponed.positionStart) {
                        if (cmd == IndexedAdapterHelper.UpdateOp.ADD) {
                            postponed.itemCount++;
                        } else if (cmd == IndexedAdapterHelper.UpdateOp.REMOVE) {
                            postponed.itemCount--;
                        }
                        // op moved to left, move it right to revert
                        pos++;
                    } else {
                        if (cmd == IndexedAdapterHelper.UpdateOp.ADD) {
                            postponed.positionStart++;
                        } else if (cmd == IndexedAdapterHelper.UpdateOp.REMOVE) {
                            postponed.positionStart--;
                        }
                        // op was moved right, move left to revert
                        pos--;
                    }
                } else if (pos < postponed.positionStart) {
                    // postponed MV is outside the dispatched OP. if it is before, offset
                    if (cmd == IndexedAdapterHelper.UpdateOp.ADD) {
                        postponed.positionStart++;
                        postponed.itemCount++;
                    } else if (cmd == IndexedAdapterHelper.UpdateOp.REMOVE) {
                        postponed.positionStart--;
                        postponed.itemCount--;
                    }
                }
            } else {
                if (postponed.positionStart <= pos) {
                    if (postponed.cmd == IndexedAdapterHelper.UpdateOp.ADD) {
                        pos -= postponed.itemCount;
                    } else if (postponed.cmd == IndexedAdapterHelper.UpdateOp.REMOVE) {
                        pos += postponed.itemCount;
                    }
                } else {
                    if (cmd == IndexedAdapterHelper.UpdateOp.ADD) {
                        postponed.positionStart++;
                    } else if (cmd == IndexedAdapterHelper.UpdateOp.REMOVE) {
                        postponed.positionStart--;
                    }
                }
            }
            if (DEBUG) {
                Log.d(TAG, "dispath (step" + i + ")");
                Log.d(TAG, "postponed state:" + i + ", pos:" + pos);
                for (IndexedAdapterHelper.UpdateOp updateOp : mPostponedList) {
                    Log.d(TAG, updateOp.toString());
                }
                Log.d(TAG, "----");
            }
        }
        for (int i = mPostponedList.size() - 1; i >= 0; i--) {
            IndexedAdapterHelper.UpdateOp op = mPostponedList.get(i);
            if (op.cmd == IndexedAdapterHelper.UpdateOp.MOVE) {
                if (op.itemCount == op.positionStart || op.itemCount < 0) {
                    mPostponedList.remove(i);
                    recycleUpdateOp(op);
                }
            } else if (op.itemCount <= 0) {
                mPostponedList.remove(i);
                recycleUpdateOp(op);
            }
        }
        return pos;
    }

    private boolean canFindInPreLayout(int position) {
        final int count = mPostponedList.size();
        for (int i = 0; i < count; i++) {
            IndexedAdapterHelper.UpdateOp op = mPostponedList.get(i);
            if (op.cmd == IndexedAdapterHelper.UpdateOp.MOVE) {
                if (findPositionOffset(op.itemCount, i + 1) == position) {
                    return true;
                }
            } else if (op.cmd == IndexedAdapterHelper.UpdateOp.ADD) {
                // TODO optimize.
                final int end = op.positionStart + op.itemCount;
                for (int pos = op.positionStart; pos < end; pos++) {
                    if (findPositionOffset(pos, i + 1) == position) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void applyAdd(IndexedAdapterHelper.UpdateOp op) {
        postponeAndUpdateViewHolders(op);
    }

    private void postponeAndUpdateViewHolders(IndexedAdapterHelper.UpdateOp op) {
        if (DEBUG) {
            Log.d(TAG, "postponing " + op);
        }
        mPostponedList.add(op);
        switch (op.cmd) {
            case IndexedAdapterHelper.UpdateOp.ADD:
                mCallback.offsetPositionsForAdd(op.positionStart, op.itemCount);
                break;
            case IndexedAdapterHelper.UpdateOp.MOVE:
                mCallback.offsetPositionsForMove(op.positionStart, op.itemCount);
                break;
            case IndexedAdapterHelper.UpdateOp.REMOVE:
                mCallback.offsetPositionsForRemovingLaidOutOrNewView(op.positionStart,
                        op.itemCount);
                break;
            case IndexedAdapterHelper.UpdateOp.UPDATE:
                mCallback.markViewHoldersUpdated(op.positionStart, op.itemCount, op.payload);
                break;
            default:
                throw new IllegalArgumentException("Unknown update op type for " + op);
        }
    }

    boolean hasPendingUpdates() {
        return mPendingUpdates.size() > 0;
    }

    boolean hasAnyUpdateTypes(int updateTypes) {
        return (mExistingUpdateTypes & updateTypes) != 0;
    }

    int findPositionOffset(int position) {
        return findPositionOffset(position, 0);
    }

    int findPositionOffset(int position, int firstPostponedItem) {
        int count = mPostponedList.size();
        for (int i = firstPostponedItem; i < count; ++i) {
            IndexedAdapterHelper.UpdateOp op = mPostponedList.get(i);
            if (op.cmd == IndexedAdapterHelper.UpdateOp.MOVE) {
                if (op.positionStart == position) {
                    position = op.itemCount;
                } else {
                    if (op.positionStart < position) {
                        position--; // like a remove
                    }
                    if (op.itemCount <= position) {
                        position++; // like an add
                    }
                }
            } else if (op.positionStart <= position) {
                if (op.cmd == IndexedAdapterHelper.UpdateOp.REMOVE) {
                    if (position < op.positionStart + op.itemCount) {
                        return -1;
                    }
                    position -= op.itemCount;
                } else if (op.cmd == IndexedAdapterHelper.UpdateOp.ADD) {
                    position += op.itemCount;
                }
            }
        }
        return position;
    }

    /**
     * @return True if updates should be processed.
     */
    boolean onItemRangeChanged(int positionStart, int itemCount, Object payload) {
        if (itemCount < 1) {
            return false;
        }
        mPendingUpdates.add(obtainUpdateOp(IndexedAdapterHelper.UpdateOp.UPDATE, positionStart, itemCount, payload));
        mExistingUpdateTypes |= IndexedAdapterHelper.UpdateOp.UPDATE;
        return mPendingUpdates.size() == 1;
    }

    /**
     * @return True if updates should be processed.
     */
    boolean onItemRangeInserted(int positionStart, int itemCount) {
        if (itemCount < 1) {
            return false;
        }
        mPendingUpdates.add(obtainUpdateOp(IndexedAdapterHelper.UpdateOp.ADD, positionStart, itemCount, null));
        mExistingUpdateTypes |= IndexedAdapterHelper.UpdateOp.ADD;
        return mPendingUpdates.size() == 1;
    }

    /**
     * @return True if updates should be processed.
     */
    boolean onItemRangeRemoved(int positionStart, int itemCount) {
        if (itemCount < 1) {
            return false;
        }
        mPendingUpdates.add(obtainUpdateOp(IndexedAdapterHelper.UpdateOp.REMOVE, positionStart, itemCount, null));
        mExistingUpdateTypes |= IndexedAdapterHelper.UpdateOp.REMOVE;
        return mPendingUpdates.size() == 1;
    }

    /**
     * @return True if updates should be processed.
     */
    boolean onItemRangeMoved(int from, int to, int itemCount) {
        if (from == to) {
            return false; // no-op
        }
        if (itemCount != 1) {
            throw new IllegalArgumentException("Moving more than 1 item is not supported yet");
        }
        mPendingUpdates.add(obtainUpdateOp(IndexedAdapterHelper.UpdateOp.MOVE, from, to, null));
        mExistingUpdateTypes |= IndexedAdapterHelper.UpdateOp.MOVE;
        return mPendingUpdates.size() == 1;
    }

    /**
     * Skips pre-processing and applies all updates in one pass.
     */
    void consumeUpdatesInOnePass() {
        // we still consume postponed updates (if there is) in case there was a pre-process call
        // w/o a matching consumePostponedUpdates.
        consumePostponedUpdates();
        final int count = mPendingUpdates.size();
        for (int i = 0; i < count; i++) {
            IndexedAdapterHelper.UpdateOp op = mPendingUpdates.get(i);
            switch (op.cmd) {
                case IndexedAdapterHelper.UpdateOp.ADD:
                    mCallback.onDispatchSecondPass(op);
                    mCallback.offsetPositionsForAdd(op.positionStart, op.itemCount);
                    break;
                case IndexedAdapterHelper.UpdateOp.REMOVE:
                    mCallback.onDispatchSecondPass(op);
                    mCallback.offsetPositionsForRemovingInvisible(op.positionStart, op.itemCount);
                    break;
                case IndexedAdapterHelper.UpdateOp.UPDATE:
                    mCallback.onDispatchSecondPass(op);
                    mCallback.markViewHoldersUpdated(op.positionStart, op.itemCount, op.payload);
                    break;
                case IndexedAdapterHelper.UpdateOp.MOVE:
                    mCallback.onDispatchSecondPass(op);
                    mCallback.offsetPositionsForMove(op.positionStart, op.itemCount);
                    break;
            }
            if (mOnItemProcessedCallback != null) {
                mOnItemProcessedCallback.run();
            }
        }
        recycleUpdateOpsAndClearList(mPendingUpdates);
        mExistingUpdateTypes = 0;
    }

    public int applyPendingUpdatesToPosition(int position) {
        final int size = mPendingUpdates.size();
        for (int i = 0; i < size; i++) {
            IndexedAdapterHelper.UpdateOp op = mPendingUpdates.get(i);
            switch (op.cmd) {
                case IndexedAdapterHelper.UpdateOp.ADD:
                    if (op.positionStart <= position) {
                        position += op.itemCount;
                    }
                    break;
                case IndexedAdapterHelper.UpdateOp.REMOVE:
                    if (op.positionStart <= position) {
                        final int end = op.positionStart + op.itemCount;
                        if (end > position) {
                            return RecyclerView.NO_POSITION;
                        }
                        position -= op.itemCount;
                    }
                    break;
                case IndexedAdapterHelper.UpdateOp.MOVE:
                    if (op.positionStart == position) {
                        position = op.itemCount; //position end
                    } else {
                        if (op.positionStart < position) {
                            position -= 1;
                        }
                        if (op.itemCount <= position) {
                            position += 1;
                        }
                    }
                    break;
            }
        }
        return position;
    }

    boolean hasUpdates() {
        return !mPostponedList.isEmpty() && !mPendingUpdates.isEmpty();
    }

    /**
     * Queued operation to happen when child views are updated.
     */
    static final class UpdateOp {

        static final int ADD = 1;

        static final int REMOVE = 1 << 1;

        static final int UPDATE = 1 << 2;

        static final int MOVE = 1 << 3;

        static final int POOL_SIZE = 30;

        int cmd;

        int positionStart;

        Object payload;

        // holds the target position if this is a MOVE
        int itemCount;

        UpdateOp(int cmd, int positionStart, int itemCount, Object payload) {
            this.cmd = cmd;
            this.positionStart = positionStart;
            this.itemCount = itemCount;
            this.payload = payload;
        }

        String cmdToString() {
            switch (cmd) {
                case ADD:
                    return "add";
                case REMOVE:
                    return "rm";
                case UPDATE:
                    return "up";
                case MOVE:
                    return "mv";
            }
            return "??";
        }

        @Override
        public String toString() {
            return Integer.toHexString(System.identityHashCode(this))
                    + "[" + cmdToString() + ",s:" + positionStart + "c:" + itemCount
                    + ",p:" + payload + "]";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof IndexedAdapterHelper.UpdateOp)) {
                return false;
            }

            IndexedAdapterHelper.UpdateOp op = (IndexedAdapterHelper.UpdateOp) o;

            if (cmd != op.cmd) {
                return false;
            }
            if (cmd == MOVE && Math.abs(itemCount - positionStart) == 1) {
                // reverse of this is also true
                if (itemCount == op.positionStart && positionStart == op.itemCount) {
                    return true;
                }
            }
            if (itemCount != op.itemCount) {
                return false;
            }
            if (positionStart != op.positionStart) {
                return false;
            }
            if (payload != null) {
                if (!payload.equals(op.payload)) {
                    return false;
                }
            } else if (op.payload != null) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int result = cmd;
            result = 31 * result + positionStart;
            result = 31 * result + itemCount;
            return result;
        }
    }

    @Override
    public IndexedAdapterHelper.UpdateOp obtainUpdateOp(int cmd, int positionStart, int itemCount, Object payload) {
        IndexedAdapterHelper.UpdateOp op = mUpdateOpPool.acquire();
        if (op == null) {
            op = new IndexedAdapterHelper.UpdateOp(cmd, positionStart, itemCount, payload);
        } else {
            op.cmd = cmd;
            op.positionStart = positionStart;
            op.itemCount = itemCount;
            op.payload = payload;
        }
        return op;
    }

    @Override
    public void recycleUpdateOp(IndexedAdapterHelper.UpdateOp op) {
        if (!mDisableRecycler) {
            op.payload = null;
            mUpdateOpPool.release(op);
        }
    }

    void recycleUpdateOpsAndClearList(List<IndexedAdapterHelper.UpdateOp> ops) {
        final int count = ops.size();
        for (int i = 0; i < count; i++) {
            recycleUpdateOp(ops.get(i));
        }
        ops.clear();
    }

    /**
     * Contract between AdapterHelper and RecyclerView.
     */
    public interface Callback {

        RecyclerView.ViewHolder findViewHolder(int position);

        void offsetPositionsForRemovingInvisible(int positionStart, int itemCount);

        void offsetPositionsForRemovingLaidOutOrNewView(int positionStart, int itemCount);

        void markViewHoldersUpdated(int positionStart, int itemCount, Object payloads);

        void onDispatchFirstPass(IndexedAdapterHelper.UpdateOp updateOp);

        void onDispatchSecondPass(IndexedAdapterHelper.UpdateOp updateOp);

        void offsetPositionsForAdd(int positionStart, int itemCount);

        void offsetPositionsForMove(int from, int to);
    }
}

