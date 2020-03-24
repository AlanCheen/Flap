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

import me.yifeiyuan.flap.exceptions.ComponentProxyNotFoundException;
import me.yifeiyuan.flap.extensions.ComponentPool;
import me.yifeiyuan.flap.internal.ComponentProxy;
import me.yifeiyuan.flap.internal.DefaultComponent;

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

    private final Map<Class<?>, ComponentProxy<?, ?>> itemFactories;
    private final SparseArray<ComponentProxy<?, ?>> factoryMapping;

    private static final ComponentPool GLOBAL_POOL = new ComponentPool();

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
            Class<?> flapItemFactoryManager = Class.forName("me.yifeiyuan.flap.apt.manager.ComponentAutoRegister");
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
    public ComponentManager register(@NonNull final ComponentProxy itemFactory) {
        itemFactories.put(itemFactory.getComponentModelClass(), itemFactory);
        return this;
    }

    @Override
    public ComponentManager unregister(@NonNull final ComponentProxy itemFactory) {
        itemFactories.remove(itemFactory.getComponentModelClass());
        return this;
    }

    @Override
    public ComponentManager clearAll() {
        itemFactories.clear();
        factoryMapping.clear();
        return this;
    }

    @Override
    public int getItemViewType(@NonNull final Object model) {

        Class<?> modelClazz = model.getClass();

        ComponentProxy factory = itemFactories.get(modelClazz);
        if (null != factory) {
            int itemViewType = factory.getItemViewType(model);
            factoryMapping.put(itemViewType, factory);
            return itemViewType;
        } else {
            FlapDebug.throwIfDebugging(new ComponentProxyNotFoundException("Can't find the ComponentProxy for : " + modelClazz + " , please register first!"));
        }
        return DEFAULT_FACTORY.getItemViewType(model);
    }

    @NonNull
    @Override
    public Component onCreateViewHolder(@NonNull final LayoutInflater inflater, @NonNull final ViewGroup parent, final int viewType) {

        Component vh = null;

        ComponentProxy<?, ?> factory = factoryMapping.get(viewType);
        if (factory != null) {
            try {
                vh = factory.onCreateComponent(inflater, parent, viewType);
            } catch (Exception e) {
                e.printStackTrace();
                FlapDebug.throwIfDebugging(e);
            }
        }
        if (vh == null) {
            vh = DEFAULT_FACTORY.onCreateComponent(inflater, parent, viewType);
        }
        return vh;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull final Component component, final int position, final Object model, @NonNull final List<Object> payloads, @NonNull final FlapAdapter flapAdapter) {
        component.bind(model, position, payloads, flapAdapter);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull final Component component, @NonNull final FlapAdapter flapAdapter) {
        component.onViewAttachedToWindow(flapAdapter);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull final Component component, @NonNull final FlapAdapter flapAdapter) {
        component.onViewDetachedFromWindow(flapAdapter);
    }

    @Override
    public void onViewRecycled(@NonNull final Component component, @NonNull final FlapAdapter flapAdapter) {
        component.onViewRecycled(flapAdapter);
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull final Component component, @NonNull final FlapAdapter flapAdapter) {
        return component.onFailedToRecycleView(flapAdapter);
    }

    public static void setDebug(final boolean isDebugging) {
        FlapDebug.setDebug(isDebugging);
    }

    public ComponentPool getComponentPool() {
        return GLOBAL_POOL;
    }
}
