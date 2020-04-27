package me.yifeiyuan.flap.exceptions;

import android.support.annotation.RestrictTo;

/**
 * An ItemFactoryNotFoundException will be thrown when trying to create a FlapItem when there is no matching FlapItemFactory.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 *
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 2018/12/13
 * @since 1.1
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public final class ComponentProxyNotFoundException extends IllegalArgumentException {

    public ComponentProxyNotFoundException(final String s) {
        super(s);
    }

}
