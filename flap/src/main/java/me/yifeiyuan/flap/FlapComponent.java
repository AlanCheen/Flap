package me.yifeiyuan.flap;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import android.content.Context;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import java.util.List;

/**
 * Component is used by Flap as the base ViewHolder , which provides some useful and convenient abilities as well.
 *
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @version 1.0
 * @since 1.0
 * <p>
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 */
@SuppressWarnings({"EmptyMethod", "WeakerAccess", "unused"})
public abstract class FlapComponent<T> extends RecyclerView.ViewHolder implements LifecycleObserver {

    @NonNull
    protected final Context context;

    private boolean isVisible = false;

    public FlapComponent(@NonNull View itemView) {
        super(itemView);
        context = itemView.getContext();
    }

    final void bind(@NonNull final T model, int position, @NonNull final List<Object> payloads, @NonNull final FlapAdapter adapter) {
        onBind(model, position, payloads, adapter);
    }

    /**
     * Overriding `onBind` to bind your model to your Component.
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
     * Overriding `onBind` to bind your model to your Component.
     *
     * @param model The model that you need to bind.
     */
    protected abstract void onBind(@NonNull final T model);

    @SuppressWarnings("unchecked")
    protected final <V extends View> V findViewById(@IdRes int viewId) {
        return (V) itemView.findViewById(viewId);
    }

    /**
     * @param flapAdapter The adapter which is using your Component.
     * @see FlapAdapter#onViewAttachedToWindow(FlapComponent)
     */
    protected void onViewAttachedToWindow(@NonNull final FlapAdapter flapAdapter) {
        onVisibilityChanged(true, flapAdapter);
    }

    /**
     * @param flapAdapter The adapter which is using your FlapItem.
     * @see FlapAdapter#onViewDetachedFromWindow(FlapComponent)
     */
    protected void onViewDetachedFromWindow(@NonNull final FlapAdapter flapAdapter) {
        onVisibilityChanged(false, flapAdapter);
    }

    /**
     * @param visible     if component is visible
     * @param flapAdapter
     */
    protected void onVisibilityChanged(final boolean visible, FlapAdapter flapAdapter) {
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
     * @see FlapAdapter#onViewRecycled(FlapComponent)
     */
    protected void onViewRecycled(@NonNull final FlapAdapter flapAdapter) {
    }

    /**
     * @param flapAdapter The adapter which is using your FlapItem.
     * @return True if the View should be recycled, false otherwise.
     * @see FlapAdapter#onFailedToRecycleView(FlapComponent)
     */
    protected boolean onFailedToRecycleView(@NonNull final FlapAdapter flapAdapter) {
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
        if (context instanceof LifecycleOwner) {
            ((LifecycleOwner) context).getLifecycle().removeObserver(this);
        }
    }

}
