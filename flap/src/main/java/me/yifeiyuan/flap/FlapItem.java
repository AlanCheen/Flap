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
 * The base ViewHolder provides some useful and convenient abilities.
 *
 * By extending the LifecycleItem you can receive the lifecycle callbacks.
 *
 * @see LifecycleItem
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

    protected abstract void onBind(@NonNull final T model, @NonNull final FlapAdapter adapter, @NonNull final List<Object> payloads);

    @SuppressWarnings("unchecked")
    protected final <V extends View> V findViewById(@IdRes int viewId) {
        return (V) itemView.findViewById(viewId);
    }

    /**
     * @see FlapAdapter#onViewAttachedToWindow(FlapItem)
     */
    protected void onViewAttachedToWindow() {
    }

    /**
     * @see FlapAdapter#onViewDetachedFromWindow(FlapItem)
     */
    protected void onViewDetachedFromWindow() {
    }

}
