package me.yifeiyuan.flapdev.components.simpletext;

import androidx.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import me.yifeiyuan.flap.Component;
import me.yifeiyuan.flap.annotations.Proxy;
import me.yifeiyuan.flapdev.R;

/**
 * Created by 程序亦非猿 on 2018/12/4.
 */
@Proxy(layoutId = R.layout.flap_item_simple_text)
public class SimpleTextComponent extends Component<SimpleTextModel> {

    private static final String TAG = "SimpleTextItem";

    private TextView tvContent;

    public SimpleTextComponent(final View itemView) {
        super(itemView);
        tvContent = findViewById(R.id.tv_content);
    }

    @Override
    protected void onBind(@NonNull final SimpleTextModel model) {
        Log.d(TAG, "onBind: " + getAdapterPosition());
        tvContent.setText(model.content);
    }

}
