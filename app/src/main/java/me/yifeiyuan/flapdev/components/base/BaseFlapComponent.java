package me.yifeiyuan.flapdev.components.base;

import android.view.View;

import org.jetbrains.annotations.NotNull;

import me.yifeiyuan.flap.FlapAdapter;
import me.yifeiyuan.flap.Component;

/**
 * Created by 程序亦非猿 on 2018/12/29.
 *
 * 可选
 */
public abstract class BaseFlapComponent<T extends BaseModel> extends Component<T> {

    public BaseFlapComponent(final View itemView) {
        super(itemView);
    }

}
