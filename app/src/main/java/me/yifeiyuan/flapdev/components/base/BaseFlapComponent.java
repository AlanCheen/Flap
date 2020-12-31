package me.yifeiyuan.flapdev.components.base;

import android.view.View;

import org.jetbrains.annotations.NotNull;

import me.yifeiyuan.flap.FlapAdapter;
import me.yifeiyuan.flap.Component;

/**
 * Created by 程序亦非猿 on 2018/12/29.
 */
public abstract class BaseFlapComponent<T extends BaseModel> extends Component<T> {

    public BaseFlapComponent(final View itemView) {
        super(itemView);
    }

    @Override
    protected void onViewAttachedToWindow(@NotNull final FlapAdapter flapAdapter) {
        super.onViewAttachedToWindow(flapAdapter);
    }

    @Override
    protected void onViewDetachedFromWindow(@NotNull final FlapAdapter flapAdapter) {
        super.onViewDetachedFromWindow(flapAdapter);
    }

    @Override
    protected void onViewRecycled(@NotNull final FlapAdapter flapAdapter) {
        super.onViewRecycled(flapAdapter);
    }

    @Override
    protected boolean onFailedToRecycleView(@NotNull final FlapAdapter flapAdapter) {
        return super.onFailedToRecycleView(flapAdapter);
    }
}
