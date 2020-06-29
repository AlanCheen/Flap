package me.yifeiyuan.flapdev.components.customviewtype;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import me.yifeiyuan.flap.FlapComponent;
import me.yifeiyuan.flap.internal.ComponentProxy;
import me.yifeiyuan.flapdev.R;

/**
 * Created by 程序亦非猿 on 2019/1/18.
 */
public class CustomViewTypeComponent extends FlapComponent<CustomModel> {

    private static final int CUSTOM_ITEM_VIEW_TYPE = 466;

    public CustomViewTypeComponent(final View itemView) {
        super(itemView);
    }

    @Override
    protected void onBind(@NonNull final CustomModel model) {

    }

    public static class Factory implements ComponentProxy<CustomModel, CustomViewTypeComponent> {

        @NonNull
        @Override
        public CustomViewTypeComponent createComponent(@NotNull @NonNull final LayoutInflater inflater, @NotNull @NonNull final ViewGroup parent, final int viewType) {
            return new CustomViewTypeComponent(inflater.inflate(R.layout.flap_item_custom_type, parent, false));
        }

        @Override
        public int getItemViewType(final CustomModel model) {
            return CUSTOM_ITEM_VIEW_TYPE;
        }

        @NotNull
        @Override
        public Class<CustomModel> getComponentModelClass() {
            return CustomModel.class;
        }
    }

}
