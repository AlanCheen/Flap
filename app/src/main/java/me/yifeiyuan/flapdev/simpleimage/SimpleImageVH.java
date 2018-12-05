package me.yifeiyuan.flapdev.simpleimage;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.yifeiyuan.flap.FlapItem;
import me.yifeiyuan.flap.ItemFactory;
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

    public static class SimpleImageItemFactory implements ItemFactory<SimpleImageModel> {

        @NonNull
        @Override
        public FlapItem onCreateViewHolder(@NonNull final LayoutInflater inflater, @NonNull final ViewGroup parent, final int viewType) {
            return new SimpleImageVH(inflater.inflate(viewType, parent, false));
        }

        @Override
        public int getItemViewType(final SimpleImageModel model) {
            return R.layout.item_simple_image;
        }
    }

}
