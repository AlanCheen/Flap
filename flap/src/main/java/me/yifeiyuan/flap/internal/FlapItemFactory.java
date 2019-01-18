package me.yifeiyuan.flap;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by 程序亦非猿
 *
 * An FlapItemFactory is used for creating FlapItem.
 */
interface FlapItemFactory<T, VH extends FlapItem<T>> {

    @NonNull
    VH onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType);

    int getItemViewType(T model);
}
