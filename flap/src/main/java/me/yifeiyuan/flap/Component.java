package me.yifeiyuan.flap;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

/**
 * FlapItem is used by Flap as the base ViewHolder , which provides some useful and convenient abilities as well.
 *
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @version 1.0
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 */
@SuppressWarnings({"EmptyMethod", "WeakerAccess", "unused"})
public abstract class Component<T> extends RecyclerView.ViewHolder implements LifecycleObserver {

    protected final Context context;

    private boolean isVisible = false;

    public Component(View itemView) {
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
     * @see FlapAdapter#onViewAttachedToWindow(Component)
     */
    protected void onViewAttachedToWindow(final FlapAdapter flapAdapter) {
        onVisibilityChanged(true);
    }

    /**
     * @param flapAdapter The adapter which is using your FlapItem.
     *
     * @see FlapAdapter#onViewDetachedFromWindow(Component)
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
     * @see FlapAdapter#onViewRecycled(Component)
     */
    protected void onViewRecycled(final FlapAdapter flapAdapter) {
    }

    /**
     * @param flapAdapter The adapter which is using your FlapItem.
     *
     * @return True if the View should be recycled, false otherwise.
     *
     * @see FlapAdapter#onFailedToRecycleView(Component)
     */
    protected boolean onFailedToRecycleView(final FlapAdapter flapAdapter) {
        return false;
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
