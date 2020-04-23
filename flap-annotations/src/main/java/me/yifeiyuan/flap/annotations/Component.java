package me.yifeiyuan.flap.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记一个类为组件
 * @author 程序亦非猿
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Component {

    /**
     * @return the layout res id
     */
    int layoutId() default -1;

    /**
     * @return true then Flap will auto register this component.
     */
    boolean autoRegister() default true;
}
