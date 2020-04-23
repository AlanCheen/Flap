package me.yifeiyuan.flap.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记一个类为组件
 * @author 程序亦非猿
 * @since 1.1.0
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Component {

    /**
     * @return the layout res id
     *
     * @since 1.1.0
     */
    int layoutId() default -1;

    /**
     * @return true then Flap will auto register this component.
     *
     * @since
     */
    boolean autoRegister() default true;

    /**
     * 是否使用 DataBinding，假如使用 DataBinding,那么组件的构造方法需要做一定的修改：
     * <pre>
     * @Component(layoutId = R.layout.flap_item_simple_databinding, useDataBinding = true)
     * public class SimpleDataBindingComponent extends FlapComponent<SimpleDataBindingModel> {
     *
     *     private FlapItemSimpleDatabindingBinding binding;
     *
     *     public SimpleDataBindingComponent(@NonNull final ViewDataBinding binding) {
     *         super(binding.getRoot());
     *         this.binding = (FlapItemSimpleDatabindingBinding) binding;
     *     }
     *
     *     @Override
     *     protected void onBind(@NonNull final SimpleDataBindingModel model) {
     *         binding.setModel(model);
     *         binding.executePendingBindings();
     *     }
     * }
     * </pre>
     *
     * @return 如果要使用 DataBinding 则设置 true
     *
     * @since 1.5.1
     */
    boolean useDataBinding() default false;
}
