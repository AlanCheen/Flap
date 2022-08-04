package me.yifeiyuan.ktx.foundation.othermodule;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import me.yifeiyuan.flap.annotations.Delegate;
import me.yifeiyuan.flap.delegate.AdapterDelegate;
import me.yifeiyuan.flap.Component;
import me.yifeiyuan.flap.FlapAdapter;

/**
 * Created by 程序亦非猿 on 2021/9/22.
 */
public class JavaModuleComponentDelegate implements AdapterDelegate<JavaModuleModel, JavaModuleComponent> {

    @Override
    public boolean delegate(@Nullable Object model) {
        return model.getClass() == JavaModuleModel.class;
    }

    @NotNull
    @Override
    public JavaModuleComponent onCreateViewHolder(@NotNull LayoutInflater inflater, @NotNull ViewGroup parent, int viewType) {
        return new JavaModuleComponent(inflater.inflate(viewType, parent, false));
    }

    @Override
    public long getItemId(@NotNull Object model) {
        return 0;
    }

    @Override
    public int getItemViewType(@NotNull Object model) {
        return R.layout.java_module_component;
    }

    @Override
    public void onBindViewHolder(@NotNull Component<?> component, @NotNull Object data, int position, @NotNull List<?> payloads, @NotNull FlapAdapter adapter) {
        component.bindData(data, position, payloads, adapter, this);
    }

    @Override
    public void onViewAttachedToWindow(@NotNull FlapAdapter adapter, @NotNull Component<?> component) {
        component.onViewAttachedToWindow(adapter);
    }

    @Override
    public void onViewDetachedFromWindow(@NotNull FlapAdapter adapter, @NotNull Component<?> component) {
        component.onViewDetachedFromWindow(adapter);
    }
}
