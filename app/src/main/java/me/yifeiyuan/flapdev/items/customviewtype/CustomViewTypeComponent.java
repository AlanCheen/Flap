package me.yifeiyuan.flapdev.items.customviewtype;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.yifeiyuan.flap.Component;
import me.yifeiyuan.flap.internal.FlapItemFactory;
import me.yifeiyuan.flapdev.R;

/**
 * Created by 程序亦非猿 on 2019/1/18.
 */
public class CustomViewTypeComponent extends Component<CustomModel> {

    private static final int CUSTOM_ITEM_VIEW_TYPE = 466;

    public CustomViewTypeComponent(final View itemView) {
        super(itemView);
    }

    @Override
    protected void onBind(@NonNull final CustomModel model) {

    }

    public static class Factory implements FlapItemFactory<CustomModel, CustomViewTypeComponent> {

        @NonNull
        @Override
        public CustomViewTypeComponent onCreateViewHolder(@NonNull final LayoutInflater inflater, @NonNull final ViewGroup parent, final int viewType) {
            return new CustomViewTypeComponent(inflater.inflate(R.layout.flap_item_custom_type, parent, false));
        }

        @Override
        public int getItemViewType(final CustomModel model) {
            return CUSTOM_ITEM_VIEW_TYPE;
        }

        @Override
        public Class<CustomModel> getItemModelClass() {
            return CustomModel.class;
        }
    }

}
