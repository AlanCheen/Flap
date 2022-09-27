package me.yifeiyuan.flap.widget;


import java.util.List;

/**
 * 代码基于 StaggeredGridLayoutManager 用到的 androidx.recyclerview.widget.OpReorderer 修改
 * Created by 程序亦非猿 on 2022/9/22.
 */
class IndexedOpReorderer {

    final IndexedOpReorderer.Callback mCallback;

    IndexedOpReorderer(IndexedOpReorderer.Callback callback) {
        mCallback = callback;
    }

    void reorderOps(List<IndexedAdapterHelper.UpdateOp> ops) {
        // since move operations breaks continuity, their effects on ADD/RM are hard to handle.
        // we push them to the end of the list so that they can be handled easily.
        int badMove;
        while ((badMove = getLastMoveOutOfOrder(ops)) != -1) {
            swapMoveOp(ops, badMove, badMove + 1);
        }
    }

    private void swapMoveOp(List<IndexedAdapterHelper.UpdateOp> list, int badMove, int next) {
        final IndexedAdapterHelper.UpdateOp moveOp = list.get(badMove);
        final IndexedAdapterHelper.UpdateOp nextOp = list.get(next);
        switch (nextOp.cmd) {
            case IndexedAdapterHelper.UpdateOp.REMOVE:
                swapMoveRemove(list, badMove, moveOp, next, nextOp);
                break;
            case IndexedAdapterHelper.UpdateOp.ADD:
                swapMoveAdd(list, badMove, moveOp, next, nextOp);
                break;
            case IndexedAdapterHelper.UpdateOp.UPDATE:
                swapMoveUpdate(list, badMove, moveOp, next, nextOp);
                break;
        }
    }

    void swapMoveRemove(List<IndexedAdapterHelper.UpdateOp> list, int movePos, IndexedAdapterHelper.UpdateOp moveOp,
                        int removePos, IndexedAdapterHelper.UpdateOp removeOp) {
        IndexedAdapterHelper.UpdateOp extraRm = null;
        // check if move is nulled out by remove
        boolean revertedMove = false;
        final boolean moveIsBackwards;

        if (moveOp.positionStart < moveOp.itemCount) {
            moveIsBackwards = false;
            if (removeOp.positionStart == moveOp.positionStart
                    && removeOp.itemCount == moveOp.itemCount - moveOp.positionStart) {
                revertedMove = true;
            }
        } else {
            moveIsBackwards = true;
            if (removeOp.positionStart == moveOp.itemCount + 1
                    && removeOp.itemCount == moveOp.positionStart - moveOp.itemCount) {
                revertedMove = true;
            }
        }

        // going in reverse, first revert the effect of add
        if (moveOp.itemCount < removeOp.positionStart) {
            removeOp.positionStart--;
        } else if (moveOp.itemCount < removeOp.positionStart + removeOp.itemCount) {
            // move is removed.
            removeOp.itemCount--;
            moveOp.cmd = IndexedAdapterHelper.UpdateOp.REMOVE;
            moveOp.itemCount = 1;
            if (removeOp.itemCount == 0) {
                list.remove(removePos);
                mCallback.recycleUpdateOp(removeOp);
            }
            // no need to swap, it is already a remove
            return;
        }

        // now affect of add is consumed. now apply effect of first remove
        if (moveOp.positionStart <= removeOp.positionStart) {
            removeOp.positionStart++;
        } else if (moveOp.positionStart < removeOp.positionStart + removeOp.itemCount) {
            final int remaining = removeOp.positionStart + removeOp.itemCount
                    - moveOp.positionStart;
            extraRm = mCallback.obtainUpdateOp(IndexedAdapterHelper.UpdateOp.REMOVE, moveOp.positionStart + 1, remaining, null);
            removeOp.itemCount = moveOp.positionStart - removeOp.positionStart;
        }

        // if effects of move is reverted by remove, we are done.
        if (revertedMove) {
            list.set(movePos, removeOp);
            list.remove(removePos);
            mCallback.recycleUpdateOp(moveOp);
            return;
        }

        // now find out the new locations for move actions
        if (moveIsBackwards) {
            if (extraRm != null) {
                if (moveOp.positionStart > extraRm.positionStart) {
                    moveOp.positionStart -= extraRm.itemCount;
                }
                if (moveOp.itemCount > extraRm.positionStart) {
                    moveOp.itemCount -= extraRm.itemCount;
                }
            }
            if (moveOp.positionStart > removeOp.positionStart) {
                moveOp.positionStart -= removeOp.itemCount;
            }
            if (moveOp.itemCount > removeOp.positionStart) {
                moveOp.itemCount -= removeOp.itemCount;
            }
        } else {
            if (extraRm != null) {
                if (moveOp.positionStart >= extraRm.positionStart) {
                    moveOp.positionStart -= extraRm.itemCount;
                }
                if (moveOp.itemCount >= extraRm.positionStart) {
                    moveOp.itemCount -= extraRm.itemCount;
                }
            }
            if (moveOp.positionStart >= removeOp.positionStart) {
                moveOp.positionStart -= removeOp.itemCount;
            }
            if (moveOp.itemCount >= removeOp.positionStart) {
                moveOp.itemCount -= removeOp.itemCount;
            }
        }

        list.set(movePos, removeOp);
        if (moveOp.positionStart != moveOp.itemCount) {
            list.set(removePos, moveOp);
        } else {
            list.remove(removePos);
        }
        if (extraRm != null) {
            list.add(movePos, extraRm);
        }
    }

    private void swapMoveAdd(List<IndexedAdapterHelper.UpdateOp> list, int move, IndexedAdapterHelper.UpdateOp moveOp, int add,
                             IndexedAdapterHelper.UpdateOp addOp) {
        int offset = 0;
        // going in reverse, first revert the effect of add
        if (moveOp.itemCount < addOp.positionStart) {
            offset--;
        }
        if (moveOp.positionStart < addOp.positionStart) {
            offset++;
        }
        if (addOp.positionStart <= moveOp.positionStart) {
            moveOp.positionStart += addOp.itemCount;
        }
        if (addOp.positionStart <= moveOp.itemCount) {
            moveOp.itemCount += addOp.itemCount;
        }
        addOp.positionStart += offset;
        list.set(move, addOp);
        list.set(add, moveOp);
    }

    void swapMoveUpdate(List<IndexedAdapterHelper.UpdateOp> list, int move, IndexedAdapterHelper.UpdateOp moveOp, int update,
                        IndexedAdapterHelper.UpdateOp updateOp) {
        IndexedAdapterHelper.UpdateOp extraUp1 = null;
        IndexedAdapterHelper.UpdateOp extraUp2 = null;
        // going in reverse, first revert the effect of add
        if (moveOp.itemCount < updateOp.positionStart) {
            updateOp.positionStart--;
        } else if (moveOp.itemCount < updateOp.positionStart + updateOp.itemCount) {
            // moved item is updated. add an update for it
            updateOp.itemCount--;
            extraUp1 = mCallback.obtainUpdateOp(IndexedAdapterHelper.UpdateOp.UPDATE, moveOp.positionStart, 1, updateOp.payload);
        }
        // now affect of add is consumed. now apply effect of first remove
        if (moveOp.positionStart <= updateOp.positionStart) {
            updateOp.positionStart++;
        } else if (moveOp.positionStart < updateOp.positionStart + updateOp.itemCount) {
            final int remaining = updateOp.positionStart + updateOp.itemCount
                    - moveOp.positionStart;
            extraUp2 = mCallback.obtainUpdateOp(
                    IndexedAdapterHelper.UpdateOp.UPDATE, moveOp.positionStart + 1, remaining,
                    updateOp.payload);
            updateOp.itemCount -= remaining;
        }
        list.set(update, moveOp);
        if (updateOp.itemCount > 0) {
            list.set(move, updateOp);
        } else {
            list.remove(move);
            mCallback.recycleUpdateOp(updateOp);
        }
        if (extraUp1 != null) {
            list.add(move, extraUp1);
        }
        if (extraUp2 != null) {
            list.add(move, extraUp2);
        }
    }

    private int getLastMoveOutOfOrder(List<IndexedAdapterHelper.UpdateOp> list) {
        boolean foundNonMove = false;
        for (int i = list.size() - 1; i >= 0; i--) {
            final IndexedAdapterHelper.UpdateOp op1 = list.get(i);
            if (op1.cmd == IndexedAdapterHelper.UpdateOp.MOVE) {
                if (foundNonMove) {
                    return i;
                }
            } else {
                foundNonMove = true;
            }
        }
        return -1;
    }

    public interface Callback {

        IndexedAdapterHelper.UpdateOp obtainUpdateOp(int cmd, int startPosition, int itemCount, Object payload);

        void recycleUpdateOp(IndexedAdapterHelper.UpdateOp op);
    }
}
