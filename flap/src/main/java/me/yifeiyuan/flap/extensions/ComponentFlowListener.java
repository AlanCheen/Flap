package me.yifeiyuan.flap.extensions;

import me.yifeiyuan.flap.Component;
import me.yifeiyuan.flap.internal.ComponentProxy;
/**
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 *
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 2019-12-31 11:14
 * @since 1.5
 */
public interface ComponentFlowListener {

    /**
     * 在创建组件前调用
     *
     * @param proxy 组件代理
     */
    void onStartCreateComponent(final ComponentProxy proxy);

    /**
     * 在组件创建完毕后调用
     *
     * @param proxy
     * @param component
     */
    void onComponentCreated(final ComponentProxy proxy, final Component component);

    /**
     * 在绑定组件之前调用
     *
     * @param component
     * @param position
     * @param model
     */
    void onStartBindComponent(final Component component, final int position, Object model);

    /**
     * 在组件绑定完毕后回调
     *
     * @param component
     * @param position
     * @param model
     */
    void onComponentBound(final Component component, final int position, Object model);

}
