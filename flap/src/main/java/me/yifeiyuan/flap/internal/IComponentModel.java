package me.yifeiyuan.flap.internal;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
/**
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 *
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 2020/3/24 4:35 PM
 * @since 1.0
 */
public interface IComponentModel {

    boolean areItemsTheSame(@NonNull Object var1, @NonNull Object var2);

    boolean areContentsTheSame(@NonNull Object var1, @NonNull Object var2);

    @Nullable
    Object getChangePayload(@NonNull Object oldItem, @NonNull Object newItem);
}
