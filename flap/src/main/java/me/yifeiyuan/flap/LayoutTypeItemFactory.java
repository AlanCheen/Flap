package me.yifeiyuan.flap;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Fitz|mingjue on 2018/11/19.
 */
public abstract class LayoutTypeItemFactory<T, VH extends FlapViewHolder> implements ItemFactory<T> {

    @NonNull
    @Override
    public VH createViewHolder(@NonNull final LayoutInflater inflater, @NonNull final ViewGroup parent, final int viewType) {

        View view = inflater.inflate(viewType, parent, false);

        Class clazz = getModelClassFromItemFactory(this);

        VH vh = null;
        try {
            Constructor constructor = clazz.getConstructor(View.class);
            constructor.setAccessible(true);
            vh = (VH) constructor.newInstance(view);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return vh;
    }

    private Class<?> getModelClassFromItemFactory(final Object obj) {
        Type[] types = ((ParameterizedType) (obj.getClass().getGenericSuperclass())).getActualTypeArguments();
        return (Class<?>) types[1];
    }

}
