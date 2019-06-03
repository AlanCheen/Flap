package me.yifeiyuan.flap.exceptions;

import android.support.annotation.RestrictTo;

/**
 * Created by 程序亦非猿 on 2018/12/13.
 *
 * An ItemFactoryNotFoundException will be thrown when trying to create a FlapItem when there is no matching FlapItemFactory.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public final class ItemFactoryNotFoundException extends IllegalArgumentException {

    public ItemFactoryNotFoundException(final String s) {
        super(s);
    }

}
