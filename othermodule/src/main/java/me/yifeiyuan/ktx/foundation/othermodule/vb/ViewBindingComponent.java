package me.yifeiyuan.ktx.foundation.othermodule.vb;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewbinding.ViewBinding;

import me.yifeiyuan.flap.Component;
import me.yifeiyuan.flap.annotations.Proxy;
import me.yifeiyuan.ktx.foundation.othermodule.databinding.FlapItemVbBinding;

/**
 * Created by 程序亦非猿 on 2020/9/30.
 */
@Proxy(layoutName = "flap_item_vb", useViewBinding = true)
public class ViewBindingComponent extends Component<VBModel> {

     FlapItemVbBinding binding;
    public ViewBindingComponent(@NonNull FlapItemVbBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    @Override
    protected void onBind(@NonNull VBModel model) {
        binding.tvContent.setText("ViewBinding Sample");
    }
}
