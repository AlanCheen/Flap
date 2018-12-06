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

    private final Map<Class<?>, FlapItemFactory> itemFactories;
    private final SparseArray<FlapItemFactory> factoryMapping;

    private static volatile Flap sInstance;

    static Flap getDefault() {
        if (null == sInstance) {
            synchronized (Flap.class) {
                if (null == sInstance) {
                    sInstance = new Flap();
                }
            }
        }
        return sInstance;
    }

    private Flap() {
        this(DEFAULT_ITEM_TYPE_COUNT);
    }

    private Flap(int typeCount) {
        itemFactories = new HashMap<>(typeCount);
        factoryMapping = new SparseArray<>(typeCount);
    }

    @Override
    public ItemFactoryManager registerItemFactory(@NonNull final FlapItemFactory itemFactory) {
        Class<?> modelClazz = getModelClassFromItemFactory(itemFactory);
        itemFactories.put(modelClazz, itemFactory);
        return this;
    }

    @Override
    public ItemFactoryManager unregisterItemFactory(@NonNull final FlapItemFactory itemFactory) {
        Class<?> modelClazz = getModelClassFromItemFactory(itemFactory);
        itemFactories.remove(modelClazz);
        return this;
    }

    private Class<?> getModelClassFromItemFactory(final FlapItemFactory itemFactory) {
        return (Class<?>) ReflectUtils.getTypes(itemFactory)[0];
    }

    @Override
    public int getItemViewType(@NonNull final Object model) {

        Class modelClazz = model.getClass();

        FlapItemFactory factory = itemFactories.get(modelClazz);
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
    public FlapItem onCreateViewHolder(@NonNull final LayoutInflater inflater, @NonNull final ViewGroup parent, final int viewType) {

        FlapItem vh = null;

        FlapItemFactory factory = factoryMapping.get(viewType);
        if (null != factory) {
            try {
                vh = factory.onCreateViewHolder(inflater, parent, viewType);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Something went wrong when creating item by ItemFactory:" + factory.getClass().getSimpleName());
            }
        }
        //In case that we get a null view holder , create a default one ,so won't crash the app
        if (vh == null) {
            vh = onCreateDefaultViewHolder(inflater, parent, viewType);
        }
        return vh;
    }

    @NonNull
    @Override
    public FlapItem onCreateDefaultViewHolder(@NonNull final LayoutInflater inflater, @NonNull final ViewGroup parent, final int viewType) {
        return new DefaultFlapItem(new View(parent.getContext()));
    }
}
