package me.yifeiyuan.flap;

import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 程序亦非猿
 */
public final class Flap implements IFlap {

    private static final String TAG = "Flap";

    private static final int DEFAULT_ITEM_TYPE = -66666;

    static final int DEFAULT_ITEM_TYPE_COUNT = 32;

    private final Map<Class<?>, FlapItemFactory> itemFactories;
    private final SparseArray<FlapItemFactory> factoryMapping;

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

    private Flap() {
        this(DEFAULT_ITEM_TYPE_COUNT);
    }

    private Flap(int typeCount) {
        itemFactories = new HashMap<>(typeCount);
        factoryMapping = new SparseArray<>(typeCount);
    }

    @Override
    public ItemFactoryManager register(@NonNull final FlapItemFactory itemFactory) {
        Class<?> modelClazz = getModelClassFromItemFactory(itemFactory);
        itemFactories.put(modelClazz, itemFactory);
        return this;
    }

    @Override
    public ItemFactoryManager unregister(@NonNull final FlapItemFactory itemFactory) {
        Class<?> modelClazz = getModelClassFromItemFactory(itemFactory);
        itemFactories.remove(modelClazz);
        return this;
    }

    @Override
    public ItemFactoryManager clearAll() {
        itemFactories.clear();
        factoryMapping.clear();
        return this;
    }

    private Class<?> getModelClassFromItemFactory(final FlapItemFactory itemFactory) {
        return (Class<?>) ReflectUtils.getTypes(itemFactory)[0];
    }

    @SuppressWarnings("unchecked")
    @Override
    public int getItemViewType(@NonNull final Object model) {

        Class modelClazz = model.getClass();

        FlapItemFactory factory = itemFactories.get(modelClazz);
        if (null != factory) {
            int itemViewType = factory.getItemViewType(model);
            factoryMapping.put(itemViewType, factory);
            return itemViewType;
        } else {
            FlapDebug.throwIfDebugging(new ItemFactoryNotFoundException("Can't find the ItemFactory for : " + modelClazz + " , please register first!"));
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
                FlapDebug.throwIfDebugging(e);
            }
        }
        if (vh == null) {
            vh = onCreateDefaultViewHolder(inflater, parent, viewType);
        }
        return vh;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull final FlapItem holder, final Object model, @NonNull final FlapAdapter flapAdapter, @NonNull final List<Object> payloads) {
        holder.bind(model, flapAdapter, payloads);
    }

    @NonNull
    @Override
    public FlapItem onCreateDefaultViewHolder(@NonNull final LayoutInflater inflater, @NonNull final ViewGroup parent, final int viewType) {
        return new DefaultFlapItem(new View(parent.getContext()));
    }

    public static void setDebug(final boolean isDebugging) {
        FlapDebug.setDebug(isDebugging);
    }
}
