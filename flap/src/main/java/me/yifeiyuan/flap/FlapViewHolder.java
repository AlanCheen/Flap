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
 * Created by Fitz|mingjue on 2018/11/19.
 */
public abstract class FlapViewHolder<T> extends RecyclerView.ViewHolder implements LifecycleObserver {

    protected final Context context;

    public FlapViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
    }

    final void bindData(T model, FlapAdapter adapter, final List<Object> payloads) {
        beforeBindData(model, adapter, payloads);
        onBindData(model, adapter, payloads);
        afterBindData(model, adapter, payloads);
    }

    protected void onBindData(final T model, final FlapAdapter adapter, final List<Object> payloads) {
        onBindData(model);
    }

    protected abstract void onBindData(final T model);

    protected void afterBindData(final T model, final FlapAdapter adapter, final List<Object> payloads) {
    }

    protected void beforeBindData(final T model, final FlapAdapter adapter, final List<Object> payloads) {
    }

    protected <V extends View> V findViewById(@IdRes int viewId) {
        return (V) itemView.findViewById(viewId);
    }

    public void onViewDetachedFromWindow() {
    }

    public void onViewAttachedToWindow() {
    }

    public void onViewRecycled() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate() {
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
