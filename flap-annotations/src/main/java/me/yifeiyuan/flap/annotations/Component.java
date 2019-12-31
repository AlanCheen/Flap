package me.yifeiyuan.flap.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Component {

    /**
     * @return the layout res id
     */
    int layoutId() default -1;

    /**
     * @return true then FlapProcessor will auto register the Factory.
     */
    boolean autoRegister() default true;
}
