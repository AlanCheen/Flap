package me.yifeiyuan.flap.extensions;

import android.content.Context;
import android.util.AttributeSet;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import me.yifeiyuan.flap.FlapAdapter;

/**
 * 封装了 Flap 的 RecyclerView，未测试，暂时请不要使用。
 * todo
 * <p>
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 *
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 2019-10-08 15:00
 * @since 1.5
 */
public class FlapRecyclerView extends RecyclerView {

    private FlapAdapter adapter;

    public FlapRecyclerView(@NonNull final Context context) {
        super(context);
        init(context, null, 0);
    }

    public FlapRecyclerView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public FlapRecyclerView(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyle) {
        adapter = new FlapAdapter();
        setAdapter(adapter);
    }

    public void setData(List<?> data) {
        adapter.setDataAndNotify(data);
    }
}
