package me.yifeiyuan.flap.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface ComponentConfig {

    /**
     * @return the layout res id
     */
    int layoutId() default -1;

    /**
     * @return true then Flap will auto register this component.
     */
    boolean autoRegister() default true;
}
