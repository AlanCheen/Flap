package me.yifeiyuan.flapdev.simpletext;

import android.view.View;
import android.widget.TextView;

import me.yifeiyuan.flap.FlapViewHolder;
import me.yifeiyuan.flap.LayoutTypeItemFactory;
import me.yifeiyuan.flapdev.R;

/**
 * Created by 程序亦非猿
 */
public class SimpleTextItemFactory extends LayoutTypeItemFactory<SimpleTextModel, SimpleTextItemFactory.SimpleTextItemVH> {

    @Override
    public int getItemViewType(final SimpleTextModel model) {
        return R.layout.flap_item_simple_text;
    }

    public static class SimpleTextItemVH extends FlapViewHolder<SimpleTextModel> {

        TextView tvContent;
        public SimpleTextItemVH(final View itemView) {
            super(itemView);
            tvContent = findViewById(R.id.tv_content);
        }

        @Override
        protected void onBindData(final SimpleTextModel model) {
            tvContent.setText(model.content);
        }
    }

}
