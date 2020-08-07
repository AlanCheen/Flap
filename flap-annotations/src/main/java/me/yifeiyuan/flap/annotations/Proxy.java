package me.yifeiyuan.flap.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记一个类为组件，并配置组件的一些基本信息
 *
 * @author 程序亦非猿
 * @since 1.1.0
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Proxy {

    /**
     * 组件的布局 ID
     * @return the layout res id
     *
     * @since 1.1.0
     */
    int layoutId() default -1;

    /**
     * 标记一个组件是否需要自动注册，如果为 true 会自动注册到 Flap
     *
     * @return true then Flap will auto register this component.
     *
     * @since 1.1.0
     */
    boolean autoRegister() default true;

    /**
     * 是否使用 DataBinding，假如使用 DataBinding,那么组件的构造方法需要做一定的修改
     *
     * @return 如果要使用 DataBinding 则设置 true
     *
     * @since 1.5.1
     */
    boolean useDataBinding() default false;
}
