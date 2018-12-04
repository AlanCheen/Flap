package me.yifeiyuan.flapdev.simpletext;

import android.view.View;
import android.widget.TextView;

import me.yifeiyuan.flap.FlapViewHolder;
import me.yifeiyuan.flap.LayoutTypeItemFactory;
import me.yifeiyuan.flapdev.R;

/**
 * Created by 程序亦非猿 on 2018/12/4.
 */
public class SimpleTextItemViewHolder extends FlapViewHolder<SimpleTextModel> {

    private TextView tvContent;

    public SimpleTextItemViewHolder(final View itemView) {
        super(itemView);
        tvContent = findViewById(R.id.tv_content);
    }

    @Override
    protected void onBind(final SimpleTextModel model) {
        tvContent.setText(model.content);
    }

    public static class SimpleTextItemFactory extends LayoutTypeItemFactory<SimpleTextModel, SimpleTextItemViewHolder> {

        @Override
        protected int getLayoutResId(final SimpleTextModel model) {
            return R.layout.flap_item_simple_text;
        }

    }

}
