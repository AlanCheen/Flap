package me.yifeiyuan.flapdev.components.databindingsample;

import androidx.databinding.ViewDataBinding;
import androidx.annotation.NonNull;

import me.yifeiyuan.flap.FlapComponent;
import me.yifeiyuan.flap.annotations.Component;
import me.yifeiyuan.flapdev.R;
import me.yifeiyuan.flapdev.databinding.FlapItemSimpleDatabindingBinding;
/**
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 *
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 2020/3/26 3:28 PM
 * @since 1.0
 */
@Component(layoutId = R.layout.flap_item_simple_databinding, useDataBinding = true)
public class SimpleDataBindingComponent extends FlapComponent<SimpleDataBindingModel> {

    private FlapItemSimpleDatabindingBinding binding;

    public SimpleDataBindingComponent(@NonNull final ViewDataBinding binding) {
        super(binding.getRoot());
        this.binding = (FlapItemSimpleDatabindingBinding) binding;
    }

    @Override
    protected void onBind(@NonNull final SimpleDataBindingModel model) {
        binding.setModel(model);
        binding.executePendingBindings();
    }
}
