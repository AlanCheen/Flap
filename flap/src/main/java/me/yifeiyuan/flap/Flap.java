package me.yifeiyuan.flap;

import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.yifeiyuan.flap.exceptions.ItemFactoryNotFoundException;
import me.yifeiyuan.flap.internal.DefaultComponent;
import me.yifeiyuan.flap.internal.FlapItemFactory;

/**
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 *
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @version 1.0
 */
@SuppressWarnings("unchecked")
public final class Flap implements IFlap {

    private static final String TAG = "Flap";

    static final int DEFAULT_ITEM_TYPE_COUNT = 32;

    private final Map<Class<?>, FlapItemFactory> itemFactories;
    private final SparseArray<FlapItemFactory> factoryMapping;

    private static final FlapItemPool GLOBAL_POOL = new FlapItemPool();

    private static final DefaultComponent.Factory DEFAULT_FACTORY = new DefaultComponent.Factory();

    private static volatile Flap sInstance;

    public static Flap getDefault() {
        if (sInstance == null) {
            synchronized (Flap.class) {
                if (sInstance == null) {
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
        injectFactories(this);
    }

    private void injectFactories(final Flap flap) {

        try {
            Class<?> flapItemFactoryManager = Class.forName("me.yifeiyuan.flap.apt.manager.FlapItemFactoryManager");
            Method method = flapItemFactoryManager.getMethod("inject", Flap.class);
            method.setAccessible(true);
            method.invoke(null, flap);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ItemFactoryManager register(@NonNull final FlapItemFactory itemFactory) {
        itemFactories.put(itemFactory.getItemModelClass(), itemFactory);
        return this;
    }

    @Override
    public ItemFactoryManager unregister(@NonNull final FlapItemFactory itemFactory) {
        itemFactories.remove(itemFactory.getItemModelClass());
        return this;
    }

    @Override
    public ItemFactoryManager clearAll() {
        itemFactories.clear();
        factoryMapping.clear();
        return this;
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
            FlapDebug.throwIfDebugging(new ItemFactoryNotFoundException("Can't find the ItemFactory for : " + modelClazz + " , please register first!"));
        }
        return DEFAULT_FACTORY.getItemViewType(model);
    }

    @NonNull
    @Override
    public Component onCreateViewHolder(@NonNull final LayoutInflater inflater, @NonNull final ViewGroup parent, final int viewType) {

        Component vh = null;

        FlapItemFactory factory = factoryMapping.get(viewType);
        if (factory != null) {
            try {
                vh = factory.onCreateViewHolder(inflater, parent, viewType);
            } catch (Exception e) {
                e.printStackTrace();
                FlapDebug.throwIfDebugging(e);
            }
        }
        if (vh == null) {
            vh = DEFAULT_FACTORY.onCreateViewHolder(inflater, parent, viewType);
        }
        return vh;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull final Component flapItem, final int position, final Object model, @NonNull final List<Object> payloads, @NonNull final FlapAdapter flapAdapter) {
        flapItem.bind(model, position, payloads, flapAdapter);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull final Component flapItem, @NonNull final FlapAdapter flapAdapter) {
        flapItem.onViewAttachedToWindow(flapAdapter);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull final Component flapItem, @NonNull final FlapAdapter flapAdapter) {
        flapItem.onViewDetachedFromWindow(flapAdapter);
    }

    @Override
    public void onViewRecycled(@NonNull final Component flapItem, @NonNull final FlapAdapter flapAdapter) {
        flapItem.onViewRecycled(flapAdapter);
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull final Component flapItem, @NonNull final FlapAdapter flapAdapter) {
        return flapItem.onFailedToRecycleView(flapAdapter);
    }

    public static void setDebug(final boolean isDebugging) {
        FlapDebug.setDebug(isDebugging);
    }

    public FlapItemPool getFlapItemPool() {
        return GLOBAL_POOL;
    }
}
