package me.yifeiyuan.flap;

import android.support.annotation.NonNull;

/**
 * Created by Fitz|mingjue on 2018/11/19.
 */
public interface ItemFactoryManager {

    void registerItemFactory(@NonNull ItemFactory itemFactory);

    void unregisterItemFactory(@NonNull ItemFactory itemFactory);
}
