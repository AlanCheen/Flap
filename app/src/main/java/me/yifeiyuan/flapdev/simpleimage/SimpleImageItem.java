package me.yifeiyuan.flapdev.simpleimage;

import android.support.annotation.NonNull;
import android.view.View;

import java.util.List;

import me.yifeiyuan.flap.FlapAdapter;
import me.yifeiyuan.flap.FlapItem;
import me.yifeiyuan.flap.LayoutItemFactory;
import me.yifeiyuan.flapdev.R;

/**
 * Created by 程序亦非猿 on 2018/12/4.
 */
public class SimpleImageItem extends FlapItem<SimpleImageModel> {

    public SimpleImageItem(final View itemView) {
        super(itemView);
    }

    @Override
    protected void onBind(@NonNull final SimpleImageModel model, @NonNull final FlapAdapter adapter, @NonNull final List<Object> payloads) {

    }

    public static class SimpleImageItemFactory extends LayoutItemFactory<SimpleImageModel, SimpleImageItem> {

        @Override
        protected int getLayoutResId(final SimpleImageModel model) {
            return R.layout.flap_item_simple_image;
        }
    }

}
