package me.yifeiyuan.flap;

import android.support.annotation.NonNull;

import me.yifeiyuan.flap.internal.FlapItemFactory;

/**
 * A manager for managing ItemFactories.
 * You can register or unregister your ItemFactory.
 *
 * All the ItemFactories would be used by Flap internally.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @version 1.0
 */
@SuppressWarnings("unused")
interface ItemFactoryManager {

    /**
     * Register a ItemFactory to ItemFactoryManager.
     *
     * @param itemFactory The ItemFactory creates FlapItem.
     *
     * @return An ItemFactoryManager usually it could be `this`.
     */
    ItemFactoryManager register(@NonNull final FlapItemFactory itemFactory);

    /**
     * Unregister a ItemFactory from ItemFactoryManager.
     *
     * @param itemFactory The ItemFactory you register to ItemFactoryManager.
     *
     * @return An ItemFactoryManager usually it could be `this`.
     */
    ItemFactoryManager unregister(@NonNull final FlapItemFactory itemFactory);

    /**
     * Clear all the ItemFactories that you have registered.
     *
     * @return An ItemFactoryManager usually it could be `this`.
     */
    ItemFactoryManager clearAll();
}
