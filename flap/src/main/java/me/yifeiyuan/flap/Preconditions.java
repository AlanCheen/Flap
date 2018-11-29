package me.yifeiyuan.flap;

/**
 * Created by 程序亦非猿
 */
final class Preconditions {

    private Preconditions() {

    }

    static void checkNotNull(Object obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
    }

}
