package me.yifeiyuan.flap.internal;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
/**
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 * todo
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 2020/3/24 4:35 PM
 * @since 1.5
 */
public interface IComponentModel {

    boolean areItemsTheSame(@NonNull IComponentModel other);

    boolean areContentsTheSame(@NonNull IComponentModel other);

    @Nullable
    Object getChangePayload(@NonNull IComponentModel newItem);
}
