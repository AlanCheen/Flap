package me.yifeiyuan.flap.internal;

import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import me.yifeiyuan.flap.FlapItem;

/**
 * An abstraction of Factory used for creating FlapItem.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 *
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @version 1.0
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public interface FlapItemFactory<T, VH extends FlapItem<T>> {

    /**
     * Create a new instance of FlapItem.
     *
     * @param inflater LayoutInflater
     * @param parent   RecyclerView
     * @param viewType itemViewType
     *
     * @return your FlapItem
     */
    @NonNull
    VH onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType);

    /**
     * @param model your model to bind with the FlapItem.
     *
     * @return the itemViewType of the FlapItem you are gonna create.
     */
    int getItemViewType(T model);

    /**
     * @return the class of Model
     */
    @NonNull
    Class<T> getItemModelClass();

}
