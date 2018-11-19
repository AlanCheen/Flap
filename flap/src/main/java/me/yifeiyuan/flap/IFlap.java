package me.yifeiyuan.flap;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by Fitz|mingjue on 2018/11/16.
 */
public interface IFlap extends ItemFactoryManager {

    int getItemViewType(@NonNull Object model);

    @NonNull
    FlapViewHolder createViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType);

    @NonNull
    FlapViewHolder onCreateDefaultViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType);
}
