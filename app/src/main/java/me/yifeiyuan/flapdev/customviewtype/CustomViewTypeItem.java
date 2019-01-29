package me.yifeiyuan.flapdev.customviewtype;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import me.yifeiyuan.flap.FlapAdapter;
import me.yifeiyuan.flap.FlapItem;
import me.yifeiyuan.flap.internal.FlapItemFactory;
import me.yifeiyuan.flapdev.R;

/**
 * Created by 程序亦非猿 on 2019/1/18.
 */
public class CustomViewTypeItem extends FlapItem<CustomModel> {

    private static final int CUSTOM_ITEM_VIEW_TYPE = 466;

    public CustomViewTypeItem(final View itemView) {
        super(itemView);
    }

    @Override
    protected void onBind(@NonNull final CustomModel model, @NonNull final FlapAdapter adapter, @NonNull final List<Object> payloads) {

    }

    public static class Factory implements FlapItemFactory<CustomModel, CustomViewTypeItem> {

        @NonNull
        @Override
        public CustomViewTypeItem onCreateViewHolder(@NonNull final LayoutInflater inflater, @NonNull final ViewGroup parent, final int viewType) {
            return new CustomViewTypeItem(inflater.inflate(R.layout.flap_item_custom_type, parent, false));
        }

        @Override
        public int getItemViewType(final CustomModel model) {
            return CUSTOM_ITEM_VIEW_TYPE;
        }
    }

}
