package me.yifeiyuan.ktx.foundation.othermodule;

import android.view.View;

import androidx.annotation.NonNull;

import me.yifeiyuan.flap.Component;
import me.yifeiyuan.flap.annotations.Proxy;

/**
 * Created by 程序亦非猿 on 2020/9/21.
 */
@Proxy(layoutName = "java_module_component")
public class JavaModuleComponent extends Component<JavaModuleModel> {
    public JavaModuleComponent(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    protected void onBind(@NonNull JavaModuleModel model) {

    }
}
