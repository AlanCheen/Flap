package me.yifeiyuan.flap;

import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 程序亦非猿
 */
public class Flap implements IFlap {

    private static final String TAG = "Flap";

    private static final int DEFAULT_ITEM_TYPE = -66666;

    private static final int DEFAULT_ITEM_TYPE_COUNT = 32;

    private final Map<Class<?>, ItemFactory> itemFactories;
    private final SparseArray<ItemFactory> factoryMapping;

    private static volatile Flap sInstance;

    public static Flap getDefault() {
        if (null == sInstance) {
            synchronized (Flap.class) {
                if (null == sInstance) {
                    sInstance = new Flap();
                }
            }
        }
        return sInstance;
    }

    public Flap() {
        this(DEFAULT_ITEM_TYPE_COUNT);
    }

    public Flap(int typeCount) {
        itemFactories = new HashMap<>(typeCount);
        factoryMapping = new SparseArray<>(typeCount);
    }

    @Override
    public void registerItemFactory(@NonNull final ItemFactory itemFactory) {
        Class<?> modelClazz = getModelClassFromItemFactory(itemFactory);
        itemFactories.put(modelClazz, itemFactory);
    }

    private Class<?> getModelClassFromItemFactory(final ItemFactory itemFactory) {
        return (Class<?>) ReflectUtils.getTypes(itemFactory)[0];
    }

    @Override
    public void unregisterItemFactory(@NonNull final ItemFactory itemFactory) {
        Class<?> modelClazz = getModelClassFromItemFactory(itemFactory);
        itemFactories.remove(modelClazz);
    }

    @Override
    public int getItemViewType(@NonNull final Object model) {

        Class modelClazz = model.getClass();

        ItemFactory factory = itemFactories.get(modelClazz);
        if (null != factory) {
            int itemViewType = factory.getItemViewType(model);
            factoryMapping.put(itemViewType, factory);
            return itemViewType;
        } else {
            Log.e(TAG, "Can't find the ItemFactory for class: " + modelClazz + " ,please register first");
        }
        return DEFAULT_ITEM_TYPE;
    }

    @NonNull
    @Override
    public FlapViewHolder onCreateViewHolder(@NonNull final LayoutInflater inflater, @NonNull final ViewGroup parent, final int viewType) {

        FlapViewHolder vh = null;

        ItemFactory factory = factoryMapping.get(viewType);
        if (null != factory) {
            try {
                vh = factory.onCreateViewHolder(inflater, parent, viewType);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Something went wrong when creating item by ItemFactory:" + factory.getClass().getSimpleName());
            }
        }
        //in case that we get a null view holder , create a default one ,so won't crash the app
        if (vh == null) {
            vh = onCreateDefaultViewHolder(inflater, parent, viewType);
        }
        return vh;
    }

    @NonNull
    @Override
    public FlapViewHolder onCreateDefaultViewHolder(@NonNull final LayoutInflater inflater, @NonNull final ViewGroup parent, final int viewType) {
        return new DefaultViewHolder(new View(parent.getContext()));
    }
}
