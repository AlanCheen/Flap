package me.yifeiyuan.flap;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * A delegate class that delegates some methods of FlapAdapter.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 *
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @version 1.0
 * @since 2019/1/2
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
