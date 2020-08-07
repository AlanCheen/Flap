package me.yifeiyuan.flap.internal;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import me.yifeiyuan.flap.Component;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;

/**
 * Proxy for component.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 *
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 1.1
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public interface ComponentProxy<T, VH extends Component<T>> {

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
<<<<<<< HEAD
     * 返回组件的 itemViewType，默认会使用组件的 layoutId
     *
=======
     * 组件的 itemViewType，建议使用 layoutId
>>>>>>> @{-1}
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
