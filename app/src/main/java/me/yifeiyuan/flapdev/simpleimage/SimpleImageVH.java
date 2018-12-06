package me.yifeiyuan.flapdev.simpleimage;

import android.view.View;

import me.yifeiyuan.flap.FlapItem;
import me.yifeiyuan.flap.LayoutItemFactory;
import me.yifeiyuan.flapdev.R;

/**
 * Created by 程序亦非猿 on 2018/12/4.
 */
public class SimpleImageVH extends FlapItem<SimpleImageModel> {

    public SimpleImageVH(final View itemView) {
        super(itemView);
    }

    @Override
    protected void onBind(final SimpleImageModel model) {

    }

    public static class SimpleImageItemFactory extends LayoutItemFactory<SimpleImageModel, SimpleImageVH> {

        @Override
        protected int getLayoutResId(final SimpleImageModel model) {
            return R.layout.flap_item_simple_image;
        }
    }

}
