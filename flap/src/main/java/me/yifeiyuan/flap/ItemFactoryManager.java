package me.yifeiyuan.flap;

import android.support.annotation.NonNull;

/**
 * Created by 程序亦非猿
 *
 * A manager for ItemFactories.
 */
public interface ItemFactoryManager {

    /**
     * Register a ItemFactory to ItemFactoryManager
     *
     * @param modelClazz  The class of your Model which is used in your FlapItem.
     * @param itemFactory The ItemFactory creates FlapItem.
     *
     * @return An ItemFactoryManager usually it could be `this`.
     */
    ItemFactoryManager register(@NonNull final Class<?> modelClazz, @NonNull final FlapItemFactory itemFactory);

    /**
     * @param modelClazz The class of your Model which is used in your FlapItem.
     *
     * @return An ItemFactoryManager usually it could be `this`.
     */
    ItemFactoryManager unregister(@NonNull final Class<?> modelClazz);
}
