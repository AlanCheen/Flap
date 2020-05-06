package me.yifeiyuan.flap.internal;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import me.yifeiyuan.flap.FlapComponent;

/**
 * Proxy for component.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 *
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 1.1
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public interface ComponentProxy<T, VH extends FlapComponent<T>> {

    /**
     * Create a new instance of component.
     *
     * @param inflater LayoutInflater
     * @param parent   RecyclerView
     * @param viewType itemViewType
     *
     * @return your component
     */
    @NonNull
    VH createComponent(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType);

    /**
     * 组件的 itemViewType，一般使用 layoutId
     * @param model your model to bind with the component.
     *
     * @return the itemViewType of the component you are gonna create.
     */
    int getItemViewType(@Nullable T model);

    /**
     * 指明 Component 将会绑定的模型的类
     *
     * @return the class of Model
     */
    @NonNull
    Class<T> getComponentModelClass();

}
