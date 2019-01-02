package me.yifeiyuan.flap;

/**
 * Created by 程序亦非猿 on 2018/12/13.
 *
 * An ItemFactoryNotFoundException will be thrown when trying to create a FlapItem if there is no matching FlapItemFactory.
 */
final class ItemFactoryNotFoundException extends IllegalArgumentException {

    ItemFactoryNotFoundException(final String s) {
        super(s);
    }

}
