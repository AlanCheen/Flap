package me.yifeiyuan.flap;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by 程序亦非猿 on 2019/1/2.
 *
 * A delegate class that delegates some methods of FlapAdapter.
 */
@SuppressWarnings("ALL")
interface FlapAdapterDelegate {

    int getItemViewType(@NonNull Object model);

    @NonNull
    FlapItem onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType);

    void onBindViewHolder(@NonNull final FlapItem flapItem, final int position, Object model, @NonNull List<Object> payloads, @NonNull FlapAdapter flapAdapter);

    void onViewAttachedToWindow(@NonNull FlapItem flapItem, @NonNull FlapAdapter flapAdapter);

    void onViewDetachedFromWindow(@NonNull FlapItem flapItem, @NonNull FlapAdapter flapAdapter);

    void onViewRecycled(@NonNull FlapItem flapItem, @NonNull FlapAdapter flapAdapter);

    boolean onFailedToRecycleView(@NonNull FlapItem flapItem, @NonNull FlapAdapter flapAdapter);
}
