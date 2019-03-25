package me.yifeiyuan.flapdev.base;

import android.view.View;

import me.yifeiyuan.flap.FlapAdapter;
import me.yifeiyuan.flap.FlapItem;

/**
 * Created by 程序亦非猿 on 2018/12/29.
 */
public abstract class BaseFlapItem<T extends BaseModel> extends FlapItem<T> {

    public BaseFlapItem(final View itemView) {
        super(itemView);
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
