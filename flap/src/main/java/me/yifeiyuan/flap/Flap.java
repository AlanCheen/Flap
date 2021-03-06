package me.yifeiyuan.flap;

import androidx.annotation.NonNull;

import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.yifeiyuan.flap.exceptions.ComponentProxyNotFoundException;
import me.yifeiyuan.flap.extensions.ComponentFlowListener;
import me.yifeiyuan.flap.extensions.ComponentPerformanceMonitor;
import me.yifeiyuan.flap.extensions.ComponentPool;
import me.yifeiyuan.flap.internal.ComponentProxy;
import me.yifeiyuan.flap.internal.DefaultComponent.Proxy;

/**
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 *
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @version 1.0
 * @since 1.1
 */
@SuppressWarnings("ALL")
public final class Flap implements ComponentProxyRegistry, AdapterDelegate, ComponentFlowRegistry, ComponentCallbacks2 {

    private static final String TAG = "Flap";

    static final int DEFAULT_ITEM_TYPE_COUNT = 32;
    /**
     * Model class 与 ComponentProxy 的映射关系
     */
    private final Map<Class<?>, ComponentProxy<?, ?>> componentProxyMap;
    /**
     * itemViewType 与 ComponentProxy 的映射关系
     */
    private final SparseArray<ComponentProxy<?, ?>> viewTypeProxyMapping;

    private final List<ComponentFlowListener> flowListeners = Collections.synchronizedList(new ArrayList<ComponentFlowListener>());

    private static final ComponentPool GLOBAL_POOL = new ComponentPool();

    private static final ComponentProxy DEFAULT_FACTORY = new Proxy();

    private static volatile Flap sInstance;

    public static void setup(@NonNull Context ctx) {
        Context applicationContext = ctx.getApplicationContext();
        applicationContext.registerComponentCallbacks(Flap.getDefault());
    }

    public static void setDebug(final boolean isDebugging) {
        FlapDebug.setDebug(isDebugging);
    }

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
        componentProxyMap = new HashMap<>(typeCount);
        viewTypeProxyMapping = new SparseArray<>(typeCount);
        registerFlowListener(new ComponentPerformanceMonitor());
//        injectFactories(this);
        injectProxiesByPlugin(this);
    }

    private void injectProxiesByPlugin(@NonNull final Flap flap) {

    }

//    private void injectFactories(@NonNull final Flap flap) {
//        try {
//            Class<?> flapItemFactoryManager = Class.forName("me.yifeiyuan.flap.apt.manager.ComponentAutoRegister");
//            Method method = flapItemFactoryManager.getMethod("inject", Flap.class);
//            method.setAccessible(true);
//            method.invoke(null, flap);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public ComponentProxyRegistry register(@NonNull final ComponentProxy itemFactory) {
        componentProxyMap.put(itemFactory.getComponentModelClass(), itemFactory);
        return this;
    }

    @Override
    public ComponentProxyRegistry unregister(@NonNull final ComponentProxy itemFactory) {
        componentProxyMap.remove(itemFactory.getComponentModelClass());
        return this;
    }

    @Override
    public ComponentProxyRegistry clearAll() {
        componentProxyMap.clear();
        viewTypeProxyMapping.clear();
        return this;
    }

    @Override
    public int getItemViewType(@NonNull final Object model) {
        Class<?> modelClazz = model.getClass();

        ComponentProxy factory = componentProxyMap.get(modelClazz);
        if (null != factory) {
            int itemViewType = factory.getItemViewType(model);
            viewTypeProxyMapping.put(itemViewType, factory);
            return itemViewType;
        } else {
            FlapDebug.throwIfDebugging(new ComponentProxyNotFoundException("Can't find the ComponentProxy for : " + modelClazz + " , please register first!"));
        }
        return DEFAULT_FACTORY.getItemViewType(model);
    }

    @NonNull
    @Override
    public Component<?> onCreateViewHolder(@NonNull final LayoutInflater inflater, @NonNull final ViewGroup parent, final int viewType) {
        Component<?> vh = null;
        ComponentProxy<?, ?> factory = viewTypeProxyMapping.get(viewType);
        dispatchBeforeCreateComponentEvent(factory, viewType);
        if (factory != null) {
            try {
                vh = factory.createComponent(inflater, parent, viewType);
            } catch (Exception e) {
                e.printStackTrace();
                FlapDebug.throwIfDebugging(e);
            }
        }
        if (vh == null) {
            vh = DEFAULT_FACTORY.createComponent(inflater, parent, viewType);
        }
        dispatchAfterCreateComponentEvent(factory, viewType, vh);
        return vh;
    }

    private void dispatchBeforeCreateComponentEvent(final ComponentProxy<?, ?> factory, final int viewType) {
        for (final ComponentFlowListener flowListener : flowListeners) {
            flowListener.onStartCreateComponent(factory);
        }
    }

    private void dispatchAfterCreateComponentEvent(final ComponentProxy<?, ?> factory, final int viewType, final Component<?> vh) {
        for (final ComponentFlowListener flowListener : flowListeners) {
            flowListener.onComponentCreated(factory, vh);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull final Component component, final int position, final Object model, @NonNull final List<Object> payloads, @NonNull final FlapAdapter flapAdapter) {
        dispatchOnBeforeBindComponent(component, position, model);
        component.bind(model, position, payloads, flapAdapter);
        dispatchOnComponentBound(component, position, model);
    }

    private void dispatchOnBeforeBindComponent(@NonNull final Component<?> component, final int position, final Object model) {
        for (final ComponentFlowListener flowListener : flowListeners) {
            flowListener.onStartBindComponent(component, position, model);
        }
    }

    private void dispatchOnComponentBound(@NonNull final Component<?> component, final int position, final Object model) {
        for (final ComponentFlowListener flowListener : flowListeners) {
            flowListener.onComponentBound(component, position, model);
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull final Component<?> component, @NonNull final FlapAdapter flapAdapter) {
        component.onViewAttachedToWindow(flapAdapter);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull final Component<?> component, @NonNull final FlapAdapter flapAdapter) {
        component.onViewDetachedFromWindow(flapAdapter);
    }

    @Override
    public void onViewRecycled(@NonNull final Component<?> component, @NonNull final FlapAdapter flapAdapter) {
        component.onViewRecycled(flapAdapter);
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull final Component<?> component, @NonNull final FlapAdapter flapAdapter) {
        return component.onFailedToRecycleView(flapAdapter);
    }

    public ComponentPool getComponentPool() {
        return GLOBAL_POOL;
    }

    @Override
    public void registerFlowListener(final ComponentFlowListener componentFlowListener) {
        flowListeners.add(componentFlowListener);
    }

    @Override
    public void unregisterFlowListener(final ComponentFlowListener componentFlowListener) {
        flowListeners.remove(componentFlowListener);
    }

    @Override
    public void onTrimMemory(int level) {
        GLOBAL_POOL.onTrimMemory(level);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        GLOBAL_POOL.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        GLOBAL_POOL.onLowMemory();
    }
}
