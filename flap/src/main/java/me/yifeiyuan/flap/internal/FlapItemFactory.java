package me.yifeiyuan.flap.internal;

import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import me.yifeiyuan.flap.FlapItem;

/**
 * Created by 程序亦非猿
 *
 * An abstraction of Factory for creating FlapItem.
 *
 * Recommends using LayoutItemFactory.
 *
 * @see me.yifeiyuan.flap.LayoutItemFactory
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public interface FlapItemFactory<T, VH extends FlapItem<T>> {

    /**
     * Create a instance of FlapItem.
     *
     * @param inflater LayoutInflater
     * @param parent RecyclerView
     * @param viewType itemViewType
     * @return your FlapItem
     */
    @NonNull
    VH onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType);

    /**
     *
     * @param model your model to bind with your FlapItem.
     * @return the itemViewType.
     */
    int getItemViewType(T model);
}
