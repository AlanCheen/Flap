package me.yifeiyuan.flap;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import me.yifeiyuan.flap.extensions.LifecycleItem;

/**
 * FlapItem is used by Flap as the base ViewHolder , which provides some useful and convenient abilities as well.
 *
 * By extending the LifecycleItem you can receive the lifecycle callbacks.
 *
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @version 1.0
 * @see LifecycleItem also.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 */
@SuppressWarnings({"EmptyMethod", "WeakerAccess", "unused"})
public abstract class FlapItem<T> extends RecyclerView.ViewHolder {

    protected final Context context;

    private boolean isVisible = false;

    public FlapItem(View itemView) {
        super(itemView);
        context = itemView.getContext();
    }

    final void bind(@NonNull final T model, int position, @NonNull final List<Object> payloads, @NonNull final FlapAdapter adapter) {
        onBind(model, position, payloads, adapter);
    }

    /**
     * Overriding `onBind` to bind your model to your FlapItem.
     *
     * @param model    The model that you need to bind.
     * @param position position
     * @param adapter  Your adapter.
     * @param payloads The payloads you may need.
     */
    protected void onBind(@NonNull final T model, int position, @NonNull final List<Object> payloads, @NonNull final FlapAdapter adapter) {
        onBind(model);
    }

    /**
     * Overriding `onBind` to bind your model to your FlapItem.
     *
     * @param model The model that you need to bind.
     */
    protected abstract void onBind(@NonNull final T model);

    @SuppressWarnings("unchecked")
    protected final <V extends View> V findViewById(@IdRes int viewId) {
        return (V) itemView.findViewById(viewId);
    }

    /**
     * @param flapAdapter The adapter which is using your FlapItem.
     *
     * @see FlapAdapter#onViewAttachedToWindow(FlapItem)
     */
    protected void onViewAttachedToWindow(final FlapAdapter flapAdapter) {
        onVisibilityChanged(true);
    }

    /**
     * @param flapAdapter The adapter which is using your FlapItem.
     *
     * @see FlapAdapter#onViewDetachedFromWindow(FlapItem)
     */
    protected void onViewDetachedFromWindow(final FlapAdapter flapAdapter) {
        onVisibilityChanged(false);
    }

    /**
     * @param visible if component is visible
     */
    protected void onVisibilityChanged(final boolean visible) {
        isVisible = visible;
    }

    /**
     * @return true if component is visible
     */
    public boolean isVisible() {
        return isVisible;
    }

    /**
     * @param flapAdapter The adapter which is using your FlapItem.
     *
     * @see FlapAdapter#onViewRecycled(FlapItem)
     */
    protected void onViewRecycled(final FlapAdapter flapAdapter) {
    }

    /**
     * @param flapAdapter The adapter which is using your FlapItem.
     *
     * @return True if the View should be recycled, false otherwise.
     *
     * @see FlapAdapter#onFailedToRecycleView(FlapItem)
     */
    protected boolean onFailedToRecycleView(final FlapAdapter flapAdapter) {
        return false;
    }
}
