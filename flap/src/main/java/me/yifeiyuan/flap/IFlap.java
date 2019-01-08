package me.yifeiyuan.flap;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by 程序亦非猿
 *
 * IFlap is the core interface that define what Flap can do and how Flap works.
 */
interface IFlap extends ItemFactoryManager, FlapAdapterDelegate {

    @SuppressWarnings("unused")
    @NonNull
    FlapItem onCreateDefaultViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType);

}
