package me.yifeiyuan.flapdev.simpletext;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import me.yifeiyuan.flap.FlapAdapter;
import me.yifeiyuan.flap.FlapItem;
import me.yifeiyuan.flap.LayoutItemFactory;
import me.yifeiyuan.flapdev.R;

/**
 * Created by 程序亦非猿 on 2018/12/4.
 */
public class SimpleTextItem extends FlapItem<SimpleTextModel> {

    private static final String TAG = "SimpleTextItem";

    private TextView tvContent;

    public SimpleTextItem(final View itemView) {
        super(itemView);
        tvContent = findViewById(R.id.tv_content);
    }

    @Override
    protected void onBind(@NonNull final SimpleTextModel model, @NonNull final FlapAdapter adapter, @NonNull final List<Object> payloads) {
        Log.d(TAG, "onBind() called with: model = [" + model + "], adapter = [" + adapter + "], payloads = [" + payloads + "]");
        tvContent.setText(model.content);
    }

    public static class SimpleTextItemFactory extends LayoutItemFactory<SimpleTextModel, SimpleTextItem> {

        @Override
        protected int getLayoutResId(final SimpleTextModel model) {
            return R.layout.flap_item_simple_text;
        }
    }

}
