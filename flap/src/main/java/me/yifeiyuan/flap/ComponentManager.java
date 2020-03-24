package me.yifeiyuan.flap;

import android.support.annotation.NonNull;

import me.yifeiyuan.flap.internal.ComponentProxy;

/**
 * A manager for managing ComponentProxies.
 * You can register or unregister your ComponentProxy.
 *
 * All the ComponentProxies would be used by Flap internally.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 *
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @version 1.0
 */
@SuppressWarnings("unused")
interface ComponentManager {

    /**
     * Register a ComponentProxy.
     */
    ComponentManager register(@NonNull final ComponentProxy componentProxy);

    /**
     * Unregister a ComponentProxy.
     */
    ComponentManager unregister(@NonNull final ComponentProxy componentProxy);

    /**
     * Clear all the ComponentProxies that you have registered.
     */
    ComponentManager clearAll();
}
