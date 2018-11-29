package me.yifeiyuan.flapdev.simpleimage;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.yifeiyuan.flap.FlapViewHolder;
import me.yifeiyuan.flap.ItemFactory;
import me.yifeiyuan.flapdev.R;

/**
 * Created by 程序亦非猿
 */
public class SimpleImageItemFactory implements ItemFactory<SimpleImageModel> {

    @NonNull
    @Override
    public FlapViewHolder onCreateViewHolder(@NonNull final LayoutInflater inflater, @NonNull final ViewGroup parent, final int viewType) {
        return new SimpleImageVH(inflater.inflate(viewType,parent,false));
    }

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
