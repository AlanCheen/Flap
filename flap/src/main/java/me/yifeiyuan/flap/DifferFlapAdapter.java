package me.yifeiyuan.flap;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.AsyncListDiffer;
import android.support.v7.util.DiffUtil;

import java.util.List;

/**
 * Created by 程序亦非猿 on 2019/1/4.
 */
@SuppressWarnings("unchecked")
public class DifferFlapAdapter<T> extends FlapAdapter {

    private AsyncListDiffer<T> differ;

    public DifferFlapAdapter(final @NonNull DiffUtil.ItemCallback<T> itemCallback) {
        this.differ = new AsyncListDiffer(this, itemCallback);
    }

    @Override
    public FlapAdapter setData(@NonNull final List<?> data) {
        this.differ.submitList((List<T>) data);
        return this;
    }

    @Override
    protected T getItem(int position) {
        return this.differ.getCurrentList().get(position);
    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }

    @NonNull
    @Override
    public List<?> getData() {
        return differ.getCurrentList();
    }
}
