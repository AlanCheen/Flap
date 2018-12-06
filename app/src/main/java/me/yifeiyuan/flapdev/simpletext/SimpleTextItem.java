package me.yifeiyuan.flapdev.simpletext;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.yifeiyuan.flap.FlapItem;
import me.yifeiyuan.flap.FlapItemFactory;
import me.yifeiyuan.flapdev.R;

/**
 * Created by 程序亦非猿 on 2018/12/4.
 */
public class SimpleTextItem extends FlapItem<SimpleTextModel> {

    private TextView tvContent;

    public SimpleTextItem(final View itemView) {
        super(itemView);
        tvContent = findViewById(R.id.tv_content);
    }

    @Override
    protected void onBind(final SimpleTextModel model) {
        tvContent.setText(model.content);
    }

    public static class SimpleTextItemFactory implements FlapItemFactory<SimpleTextModel> {

        @NonNull
        @Override
        public FlapItem onCreateViewHolder(@NonNull final LayoutInflater inflater, @NonNull final ViewGroup parent, final int viewType) {
            return new SimpleTextItem(inflater.inflate(viewType, parent, false));
        }

        @Override
        public int getItemViewType(final SimpleTextModel model) {
            return R.layout.flap_item_simple_text;
        }
    }

}
