package me.yifeiyuan.flap;

import android.support.annotation.NonNull;

/**
 * Created by 程序亦非猿
 */
public interface ItemFactoryManager {

    void registerItemFactory(@NonNull ItemFactory itemFactory);

    void unregisterItemFactory(@NonNull ItemFactory itemFactory);
}
