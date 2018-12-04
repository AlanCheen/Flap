package me.yifeiyuan.flap;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

/**
 * Created by 程序亦非猿
 *
 * The base ViewHolder provides some useful convenient abilities.
 */
public abstract class FlapViewHolder<T> extends RecyclerView.ViewHolder implements LifecycleObserver {

    protected final Context context;

    public FlapViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
    }

    final void bind(T model, FlapAdapter adapter, final List<Object> payloads) {
        onBind(model, adapter, payloads);
    }

    protected void onBind(final T model, final FlapAdapter adapter, final List<Object> payloads) {
        onBind(model);
    }

    protected abstract void onBind(final T model);

    protected final <V extends View> V findViewById(@IdRes int viewId) {
        return (V) itemView.findViewById(viewId);
    }

    /**
     * @see FlapAdapter#onViewAttachedToWindow(FlapViewHolder)
     */
    void onViewAttachedToWindow() {
    }

    /**
     * @see FlapAdapter#onViewDetachedFromWindow(FlapViewHolder)
     */
    void onViewDetachedFromWindow() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
    }

}
