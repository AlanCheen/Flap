package me.yifeiyuan.flap;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.AsyncListDiffer;
import android.support.v7.util.DiffUtil;

/**
 * Created by 程序亦非猿 on 2019/1/4.
 */
public class DifferFlapAdapter<T> extends FlapAdapter {

    private final DiffUtil.ItemCallback<T> DEFAULT_ITEM_CALLBACK = new DiffUtil.ItemCallback<T>() {
        @Override
        public boolean areItemsTheSame(@NonNull final T o, @NonNull final T t1) {
            if (itemCallback != null) {
                return itemCallback.areItemsTheSame(o, t1);
            }
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull final T o, @NonNull final T t1) {
            if (itemCallback != null) {
                return itemCallback.areContentsTheSame(o, t1);
            }
            return false;
        }
    };

    private AsyncListDiffer<T> differ = new AsyncListDiffer<T>(this, DEFAULT_ITEM_CALLBACK);

    private DiffUtil.ItemCallback<T> itemCallback;

    public void setDiffItemCallback(final DiffUtil.ItemCallback<T> itemCallback) {
        this.itemCallback = itemCallback;
    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }

}
