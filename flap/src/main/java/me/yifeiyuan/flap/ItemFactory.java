package me.yifeiyuan.flap;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by Fitz|mingjue on 2018/11/19.
 */
public interface ItemFactory<T> {

    @NonNull
    RecyclerView.ViewHolder createViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType);

    int provideItemViewType(T model);
}
