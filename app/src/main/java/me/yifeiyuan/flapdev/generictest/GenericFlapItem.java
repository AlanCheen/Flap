package me.yifeiyuan.flapdev.generictest;

import android.support.annotation.NonNull;
import android.view.View;

import java.util.List;

import me.yifeiyuan.flap.FlapAdapter;
import me.yifeiyuan.flap.annotations.FlapItemFactory;
import me.yifeiyuan.flapdev.R;
import me.yifeiyuan.flapdev.base.BaseFlapItem;

/**
 * Created by 程序亦非猿 on 2019/1/29.
 */

@FlapItemFactory(itemViewType = R.layout.flap_item_generic_type)
public class GenericFlapItem extends BaseFlapItem<GenericModel> {

    public GenericFlapItem(final View itemView) {
        super(itemView);
    }

    @Override
    protected void onBind(@NonNull final GenericModel model, @NonNull final FlapAdapter adapter, @NonNull final List<Object> payloads) {

    }

//    public static class Factory extends LayoutItemFactory<GenericModel, GenericFlapItem> {
//        @Override
//        protected int getLayoutResId(final GenericModel model) {
//            return R.layout.flap_item_generic_type;
//        }
//    }

}
