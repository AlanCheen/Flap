package me.yifeiyuan.flap;

import android.support.annotation.NonNull;

/**
 * Created by 程序亦非猿
 */
public interface ItemFactoryManager {

    ItemFactoryManager registerItemFactory(@NonNull ItemFactory itemFactory);

    ItemFactoryManager unregisterItemFactory(@NonNull ItemFactory itemFactory);
}
