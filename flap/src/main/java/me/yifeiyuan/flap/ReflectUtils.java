package me.yifeiyuan.flap;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.res.TypedArrayUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by 程序亦非猿
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

    /**
     * @param instance obj instance
     * @param targetClazz super-class or interface
     * @return Generic types of targetClazz
     */
    @Nullable
    static Type[] getTypes(@NonNull Object instance,@NonNull Class targetClazz){
        Type type = findGenericsClass(instance.getClass(),targetClazz);
        if (type instanceof ParameterizedType){
            return ((ParameterizedType)type).getActualTypeArguments();
        }else {
            return null;
        }
    }

    /**
     * @param child subClass
     * @param target superClass or interface
     * @return genericsClass
     */
    @Nullable
    private static Type findGenericsClass(@NonNull Class child, @NonNull Class target){
        boolean isInterface = target.isInterface();
        Class[] interfaces = child.getInterfaces();
        Class father = child.getSuperclass();
        if (father == null&&(!isInterface || interfaces.length == 0))
            return null;
        if (father == target)
            return child.getGenericSuperclass();
        Type type;
        if (father!=null&&(type = findGenericsClass(father,target))!=null)
            return type;
        if (isInterface){
            int index = findIndex(interfaces,target);
            if (index!=-1)
                return child.getGenericInterfaces()[index];
            for (Class clazz:interfaces){
                if ((type = findGenericsClass(clazz,target))!=null)
                    return type;
            }
        }
        return null;
    }

    private static <T> int findIndex(@NonNull T[] ts, @NonNull T t){
        for (int i = 0;i<ts.length;i++){
            if (t == ts[i])
                return i;
        }
        return -1;
    }
}
