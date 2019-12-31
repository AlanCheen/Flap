package me.yifeiyuan.flapdev.items.simpleimage;

import android.support.annotation.NonNull;
import android.view.View;

import me.yifeiyuan.flap.FlapItem;
import me.yifeiyuan.flap.annotations.Component;
import me.yifeiyuan.flapdev.R;

/**
 * Created by 程序亦非猿 on 2018/12/4.
 */
@Component(layoutId = R.layout.flap_item_simple_image)
public class SimpleImageComponent extends FlapItem<SimpleImageModel> {

    public SimpleImageComponent(final View itemView) {
        super(itemView);
    }

    @Override
    protected void onBind(@NonNull final SimpleImageModel model) {

    }

}
