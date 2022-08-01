package me.yifeiyuan.flap.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * 被 @Delegate 注解的组件，会生成一个 AdapterDelegate 类，生成的类的命名规则：组件类名+AdapterDelegate。
 *
 *
 * Created by 程序亦非猿 on 2021/9/22.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 *
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 2020/9/22
 * @since 3.0.0
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Delegate {

    /**
     * 返回组件的布局 ID，也会被当做组件的 itemViewType
     * <p>
     * 注意：layoutId 只支持在 App 模块中使用，如果要在子模块中使用，请用 layoutName
     *
     * @see #layoutName()
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
     * @see #useViewBinding()
     *
     * @return 如果要使用 DataBinding 则设置 true
     * @since 1.5.1
     */
    boolean useDataBinding() default false;

    /**
     * 是否使用 ViewBinding，假如使用 ViewBinding,那么组件的构造方法需要做一定的修改
     * DataBinding 的优先级大于 ViewBinding
     *
     * @see #useDataBinding()
     *
     * @return 如果要使用 ViewBinding 则设置 true
     * @since 2.2.0
     */
    boolean useViewBinding() default false;

}
