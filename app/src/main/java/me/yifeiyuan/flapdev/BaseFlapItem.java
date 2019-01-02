package me.yifeiyuan.flapdev;

import android.support.annotation.NonNull;
import android.view.View;

import java.util.List;

import me.yifeiyuan.flap.FlapAdapter;
import me.yifeiyuan.flap.FlapItem;

/**
 * Created by 程序亦非猿 on 2018/12/29.
 */
public class BaseFlapItem<T> extends FlapItem<T> {

    public BaseFlapItem(final View itemView) {
        super(itemView);
    }

    @Override
    protected void onBind(@NonNull final T model, @NonNull final FlapAdapter adapter, @NonNull final List<Object> payloads) {

    }

    @Override
    protected void onViewAttachedToWindow(final FlapAdapter flapAdapter) {
        super.onViewAttachedToWindow(flapAdapter);
    }

    @Override
    protected void onViewDetachedFromWindow(final FlapAdapter flapAdapter) {
        super.onViewDetachedFromWindow(flapAdapter);
    }

    @Override
    protected void onViewRecycled(final FlapAdapter flapAdapter) {
        super.onViewRecycled(flapAdapter);
    }

    @Override
    protected boolean onFailedToRecycleView(final FlapAdapter flapAdapter) {
        return super.onFailedToRecycleView(flapAdapter);
    }
}
