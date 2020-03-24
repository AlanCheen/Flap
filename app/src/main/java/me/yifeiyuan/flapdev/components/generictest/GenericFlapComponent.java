package me.yifeiyuan.flapdev.components.generictest;

import android.support.annotation.NonNull;
import android.view.View;

import me.yifeiyuan.flap.annotations.ComponentConfig;
import me.yifeiyuan.flapdev.R;
import me.yifeiyuan.flapdev.components.base.BaseFlapComponent;

/**
 * Created by 程序亦非猿 on 2019/1/29.
 */
@ComponentConfig(layoutId = R.layout.flap_item_generic_type)
public class GenericFlapComponent extends BaseFlapComponent<GenericModel> {

    public GenericFlapComponent(final View itemView) {
        super(itemView);
    }

    @Override
    protected void onBind(@NonNull final GenericModel model) {

    }

}
