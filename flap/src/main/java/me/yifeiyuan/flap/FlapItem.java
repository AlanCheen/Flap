package me.yifeiyuan.flap;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

/**
 * Created by 程序亦非猿
 *
 * FlapItem is used by Flap as the base ViewHolder , which provides some useful and convenient abilities as well.
 *
 * By extending the LifecycleItem you can receive the lifecycle callbacks.
 *
 * @see LifecycleItem also.
 */
public abstract class FlapItem<T> extends RecyclerView.ViewHolder {

    protected final Context context;

    public FlapItem(View itemView) {
        super(itemView);
        context = itemView.getContext();
    }

    final void bind(@NonNull final T model, @NonNull final FlapAdapter adapter, @NonNull final List<Object> payloads) {
        onBind(model, adapter, payloads);
    }

    /**
     * Overriding `onBind` to bind your model to your FlapItem.
     * @param model The model that you need to bind.
     * @param adapter Your adapter.
     * @param payloads The payloads you may need.
     */
    protected abstract void onBind(@NonNull final T model, @NonNull final FlapAdapter adapter, @NonNull final List<Object> payloads);

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
    }

    /**
     * @param flapAdapter The adapter which is using your FlapItem.
     *
     * @see FlapAdapter#onViewDetachedFromWindow(FlapItem)
     */
    protected void onViewDetachedFromWindow(final FlapAdapter flapAdapter) {
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
     * @return
     * @see FlapAdapter#onFailedToRecycleView(FlapItem)
     */
    protected boolean onFailedToRecycleView(final FlapAdapter flapAdapter) {
        return false;
    }
}
