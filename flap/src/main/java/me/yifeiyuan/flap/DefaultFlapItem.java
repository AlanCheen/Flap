package me.yifeiyuan.flap;

import android.support.annotation.NonNull;
import android.view.View;

import java.util.List;

/**
 * Created by 程序亦非猿
 *
 * It's the default ViewHolder would be used when something wrong was happened so that we won't get a crash.
 */
final class DefaultFlapItem extends FlapItem {

    DefaultFlapItem(final View itemView) {
        super(itemView);
    }

    @Override
    protected void onBind(@NonNull final Object model, @NonNull final FlapAdapter adapter, @NonNull final List payloads) {

    }

}
