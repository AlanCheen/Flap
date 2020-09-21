package me.yifeiyuan.flap;

import me.yifeiyuan.flap.extensions.ComponentFlowListener;

/**
 * Created by 程序亦非猿 on 2020/9/21.
 */
public interface ComponentFlowRegistry {

    void registerFlowListener(ComponentFlowListener componentFlowListener);

    void unregisterFlowListener(ComponentFlowListener componentFlowListener);
}
