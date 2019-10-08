package me.yifeiyuan.flap;

import android.support.annotation.NonNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @version 1.0
 */
final class ReflectUtils {

    private ReflectUtils() {

    }

    /**
     * Class<?> modelClazz = (Class<?>) types[0];
     *
     * @param obj An obj instance.
     *
     * @return Type[] of the class.
     */
    static Type[] getTypes(@NonNull Object obj) {
        Type[] types;
        if (obj.getClass().getGenericSuperclass() instanceof ParameterizedType) {
            types = ((ParameterizedType) (obj.getClass().getGenericSuperclass())).getActualTypeArguments();
        } else {
            Type[] interfaces = obj.getClass().getGenericInterfaces();
            types = ((ParameterizedType) interfaces[0]).getActualTypeArguments();
        }
        return types;
    }
}
