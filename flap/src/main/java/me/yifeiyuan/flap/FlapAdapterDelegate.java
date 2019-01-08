package me.yifeiyuan.flap;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by 程序亦非猿 on 2019/1/2.
 *
 * A delegate of FlapAdapter.
 */
@SuppressWarnings("ALL")
interface FlapAdapterDelegate {

    int getItemViewType(@NonNull Object model);

    @NonNull
    FlapItem onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType);

    void onBindViewHolder(@NonNull final FlapItem holder, Object model, @NonNull FlapAdapter flapAdapter, @NonNull List<Object> payloads);

    void onViewAttachedToWindow(@NonNull FlapItem holder,@NonNull  FlapAdapter flapAdapter);

    void onViewDetachedFromWindow(@NonNull FlapItem holder,@NonNull  FlapAdapter flapAdapter);

    void onViewRecycled(@NonNull FlapItem holder,@NonNull  FlapAdapter flapAdapter);
}
