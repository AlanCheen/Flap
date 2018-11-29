package me.yifeiyuan.flapdev.simpleimage;

import android.view.View;

import me.yifeiyuan.flap.FlapViewHolder;
import me.yifeiyuan.flap.LayoutTypeItemFactory;
import me.yifeiyuan.flapdev.R;

/**
 * Created by 程序亦非猿
 */
public class SimpleImageItemFactory extends LayoutTypeItemFactory<SimpleImageModel,SimpleImageItemFactory.SimpleImageVH> {

    @Override
    public int getItemViewType(final SimpleImageModel model) {
        return R.layout.item_simple_image;
    }

    public static class SimpleImageVH extends FlapViewHolder<SimpleImageModel> {

        public SimpleImageVH(final View itemView) {
            super(itemView);
        }

        @Override
        protected void onBindData(final SimpleImageModel model) {

        }
    }

}
