package me.yifeiyuan.flap;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * A delegate class that delegates some methods of FlapAdapter.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 *
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 2019/1/2
 * @since 1.1
 */
@SuppressWarnings("ALL")
interface AdapterDelegate {

    int getItemViewType(@NonNull Object model);

    @NonNull
    Component<?> onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType);

    void onBindViewHolder(@NonNull final Component component, final int position, Object model, @NonNull List<Object> payloads, @NonNull final FlapAdapter flapAdapter);

    void onViewAttachedToWindow(@NonNull final Component<?> component, @NonNull final FlapAdapter flapAdapter);

    void onViewDetachedFromWindow(@NonNull final Component<?> component, @NonNull final FlapAdapter flapAdapter);

    void onViewRecycled(@NonNull final Component<?> component, @NonNull final FlapAdapter flapAdapter);

    boolean onFailedToRecycleView(@NonNull final Component<?> component, @NonNull final FlapAdapter flapAdapter);
}
