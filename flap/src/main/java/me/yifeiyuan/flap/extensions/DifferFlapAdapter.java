package me.yifeiyuan.flap.extensions;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AdapterListUpdateCallback;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import me.yifeiyuan.flap.FlapAdapter;

/**
 * DifferFlapAdapter supports AsyncListDiffer feature.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 *
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 2019/1/4
 * @since 0.9.3
 */
@SuppressWarnings({"unchecked", "unused"})
public class DifferFlapAdapter<T> extends FlapAdapter {

    private final AsyncListDiffer<T> differ;

    public DifferFlapAdapter(final @NonNull DiffUtil.ItemCallback<T> itemCallback) {
        differ = new AsyncListDiffer(this, itemCallback);
    }

    public DifferFlapAdapter(@NonNull AsyncDifferConfig<T> config) {
        differ = new AsyncListDiffer(new AdapterListUpdateCallback(this), config);
    }

    @Override
    public DifferFlapAdapter setData(@NonNull final List<?> data) {
        differ.submitList((List<T>) data);
        return this;
    }

    @Override
    protected T getItem(int position) {
        return differ.getCurrentList().get(position);
    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }

    @NonNull
    @Override
    public List<T> getData() {
        return differ.getCurrentList();
    }
}
