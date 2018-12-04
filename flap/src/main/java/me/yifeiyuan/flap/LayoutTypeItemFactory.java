package me.yifeiyuan.flap;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by 程序亦非猿
 */
public abstract class LayoutTypeItemFactory<T, VH extends FlapViewHolder> implements ItemFactory<T> {

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull final LayoutInflater inflater, @NonNull final ViewGroup parent, final int viewType) {

        View view = inflater.inflate(viewType, parent, false);

        Class clazz = (Class<?>) ReflectUtils.getTypes(this)[1];

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

    @Override
    public final int getItemViewType(final T model) {
        return getLayoutResId(model);
    }

    @LayoutRes
    protected abstract int getLayoutResId(final T model);
}
