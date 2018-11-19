package me.yifeiyuan.flap;

import android.support.annotation.NonNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Fitz|mingjue on 2018/11/19.
 */
final class ReflectUtils {

    /**
     * Class<?> modelClazz = (Class<?>) types[0];
     *
     * @param obj
     *
     * @return
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
