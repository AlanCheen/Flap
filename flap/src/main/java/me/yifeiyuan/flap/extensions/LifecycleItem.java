package me.yifeiyuan.flap.extensions;

import android.arch.lifecycle.LifecycleObserver;
import android.view.View;

import me.yifeiyuan.flap.Component;

/**
 * LifecycleItem is a lifecycle-aware FlapItem so that can receive the LifecycleEvents.
 *
 * You also can create your own lifecycle-aware FlapItem by implementing LifecycleObserver.
 *
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @version 1.0
 * @see LifecycleObserver
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 * @since 2018/12/5
 */
@SuppressWarnings({"EmptyMethod", "unused"})
@Deprecated
public abstract class LifecycleItem<T> extends Component<T> implements LifecycleObserver {

    public LifecycleItem(final View itemView) {
        super(itemView);
    }

}
