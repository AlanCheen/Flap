package me.yifeiyuan.flap;

/**
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @version 1.0
 */
final class Preconditions {

    private Preconditions() {

    }

    static void checkNotNull(Object obj) {
        checkNotNull(obj, "");
    }

    static void checkNotNull(Object obj, String msg) {
        if (obj == null) {
            throw new NullPointerException(msg);
        }
    }

}
