package me.yifeiyuan.flap;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by 程序亦非猿
 */
public interface ItemFactory<T> {

    @NonNull
    FlapItem onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType);

    int getItemViewType(T model);
}
