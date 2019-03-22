package me.yifeiyuan.flapdev.testann;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import me.yifeiyuan.flap.FlapAdapter;
import me.yifeiyuan.flap.FlapItem;
import me.yifeiyuan.flap.LayoutItemFactory;
import me.yifeiyuan.flap.annotations.FlapItemFactory;
import me.yifeiyuan.flapdev.R;

/**
 * Created by 程序亦非猿 on 2019-03-22.
 */

@FlapItemFactory(itemViewType = R.layout.flap_item_ann)
public class AnnItem extends FlapItem<AnnModel> {

    private static final String TAG = "SimpleTextItem";

    private TextView tvContent;

    public AnnItem(final View itemView) {
        super(itemView);
//        tvContent = findViewById(R.id.tv_content);
    }

    @Override
    protected void onBind(@NonNull final AnnModel model, @NonNull final FlapAdapter adapter, @NonNull final List<Object> payloads) {
        Log.d(TAG, "onBind: " + getAdapterPosition());
    }

    public static class Factory extends LayoutItemFactory<AnnModel, AnnItem> {

        @Override
        protected int getLayoutResId(final AnnModel model) {
            return R.layout.flap_item_simple_text;
        }

//        @Override
//        protected int getLayoutResId() {
//            return R.layout.flap_item_simple_text;
//        }
    }

}
