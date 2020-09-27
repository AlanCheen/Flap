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
     * 返回组件的布局 ID，也会被当做组件的 itemViewType
     * <p>
     * 注意：layoutId 只支持在 App 模块中使用，如果要在子模块中使用，请用 layoutName
     *
     * @return the layout res id
     * @since 1.1.0
     */
    int layoutId() default -1;

    /**
     * 该组件的布局文件的名字，例如一个布局文件叫 a_b_c.xml ，那么该值为 a_b_c，不需要后缀 .xml
     *
     * @return 组件布局的名字
     * @since 2.1.0
     */
    String layoutName() default "";

    /**
     * 是否使用 DataBinding，假如使用 DataBinding,那么组件的构造方法需要做一定的修改;
     * DataBinding 的优先级大于 ViewBinding
     *
     * @return 如果要使用 DataBinding 则设置 true
     * @since 1.5.1
     */
    boolean useDataBinding() default false;

    /**
     * todo 未实现
     * <p>
     * 是否使用 ViewBinding，假如使用 ViewBinding,那么组件的构造方法需要做一定的修改
     * DataBinding 的优先级大于 ViewBinding
     *
     * @return 如果要使用 ViewBinding 则设置 true
     */
    @Deprecated
    boolean useViewBinding() default false;

    /**
     * todo 未实现
     * 设置组件的缓存的最大数值
     * RecyclerView.RecycledViewPool.setMaxRecycledViews(int,int)
     * @return 组件最大的缓存数量
     */
    @Deprecated
    int maxRecycledViews() default -1;
}
