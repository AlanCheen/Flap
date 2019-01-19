package me.yifeiyuan.flap.extensions;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.view.View;

import me.yifeiyuan.flap.FlapItem;

/**
 * Created by 程序亦非猿 on 2018/12/5.
 *
 * LifecycleItem is a lifecycle aware FlapItem so that can get the LifecycleEvent.
 *
 * You also can create your own lifecycle aware FlapItem by implementing LifecycleObserver.
 *
 * @see LifecycleObserver
 */
@SuppressWarnings({"EmptyMethod", "unused"})
public abstract class LifecycleItem<T> extends FlapItem<T> implements LifecycleObserver {

    public LifecycleItem(final View itemView) {
        super(itemView);
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
