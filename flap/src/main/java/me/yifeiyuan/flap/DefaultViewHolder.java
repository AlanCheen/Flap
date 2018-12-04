package me.yifeiyuan.flap;

import android.view.View;

/**
 * Created by 程序亦非猿
 *
 * It's the default ViewHolder would be used when something wrong was happened so that we won't get a crash.
 */
class DefaultViewHolder extends FlapViewHolder {

    DefaultViewHolder(final View itemView) {
        super(itemView);
    }

    @Override
    protected void onBind(final Object model) {

    }

}
