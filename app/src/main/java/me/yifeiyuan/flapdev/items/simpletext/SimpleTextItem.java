package me.yifeiyuan.flapdev.items.simpletext;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import me.yifeiyuan.flap.FlapAdapter;
import me.yifeiyuan.flap.FlapItem;
import me.yifeiyuan.flap.annotations.Flap;
import me.yifeiyuan.flapdev.R;

/**
 * Created by 程序亦非猿 on 2018/12/4.
 */
@Flap(layoutId = R.layout.flap_item_simple_text, autoRegister = true)
public class SimpleTextItem extends FlapItem<SimpleTextModel> {

    private static final String TAG = "SimpleTextItem";

    private TextView tvContent;

    public SimpleTextItem(final View itemView) {
        super(itemView);
        tvContent = findViewById(R.id.tv_content);
    }

    @Override
    protected void onBind(@NonNull final SimpleTextModel model, final int position, @NonNull final List<Object> payloads, @NonNull final FlapAdapter adapter) {
        Log.d(TAG, "onBind: " + getAdapterPosition());
        tvContent.setText(model.content);
    }

}
