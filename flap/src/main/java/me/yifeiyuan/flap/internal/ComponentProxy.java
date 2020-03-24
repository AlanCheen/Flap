package me.yifeiyuan.flap.internal;

import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import me.yifeiyuan.flap.Component;

/**
 * Proxy for component.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 *
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
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
    VH onCreateComponent(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType);

    /**
     * @param model your model to bind with the component.
     *
     * @return the itemViewType of the component you are gonna create.
     */
    int getItemViewType(T model);

    /**
     * @return the class of Model
     */
    @NonNull
    Class<T> getItemModelClass();

}
