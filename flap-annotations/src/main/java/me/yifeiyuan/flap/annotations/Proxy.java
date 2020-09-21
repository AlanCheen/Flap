package me.yifeiyuan.flap.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记一个类为组件，并配置组件的一些基本信息。
 * <p>
 * 被 @Proxy 标记的组件，会生成一个 Proxy 类，并被自动注册到 Flap 中。
 *
 * @author 程序亦非猿
 * @since 1.1.0
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Proxy {

    /**
     *
     * 已废弃，请使用 layoutName
     *
     * 组件的布局 ID，也会被当做组件的 itemViewType
     *
     * @return the layout res id
     * @since 1.1.0
     */
    @Deprecated
    int layoutId() default -1;

    /**
     * 该组件的布局文件的名字，例如一个布局文件叫 a_b_c.xml ，那么该值为 a_b_c，不需要.xml
     *
     * @return 组件布局的名字
     */
    String layoutName() default "";

    /**
     * 是否使用 DataBinding，假如使用 DataBinding,那么组件的构造方法需要做一定的修改
     *
     * @return 如果要使用 DataBinding 则设置 true
     * @since 1.5.1
     */
    boolean useDataBinding() default false;
}
