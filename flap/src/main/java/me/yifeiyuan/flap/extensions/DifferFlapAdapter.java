package me.yifeiyuan.flap.extensions;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.AsyncDifferConfig;
import android.support.v7.recyclerview.extensions.AsyncListDiffer;
import android.support.v7.util.AdapterListUpdateCallback;
import android.support.v7.util.DiffUtil;

import java.util.List;

import me.yifeiyuan.flap.FlapAdapter;

/**
 * DifferFlapAdapter supports AsyncListDiffer feature.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 *
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @version 1.0
 * @since 2019/1/4
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
