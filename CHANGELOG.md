# 更新日志（CHANGELOG）

### TODO 计划

2. 新增 IComponentModel（Differ）
1. 尝试 Kotlin 化
3. FlapRecyclerView 完善
6. 支持 ViewBinding

### 2.2.0
1. 支持 ViewBinding;

### 2.1.0

1. 新增 Proxy.layoutName 注解方法，支持在子模块中使用，因为 layoutId 注解在子模块中不能使用；
2. 新增 ComponentFlowRegistry 接口；
3. 移除 IFlap 接口；


### 2.0.0

1. 新增 Flap Gradle Plugin 配合 ASM 实现组件的自动注册；
2. 移除 AutoRegister 注解以及 Proxy 注解的 autoRegister ，默认使用插件后会自动注册，如果不使用插件就不会注册；

### 1.6.1

1. Flap 新增 `setup()` 方法用来初始化;
2. Flap 实现 ComponentCallbacks2 接口，在调用 setup 方法后会在内存不足时清理 RecyclerViewPool 缓存；


### 1.6.0

1. 重命名 FlapComponent --> Component ，还是简洁点好；
2. 重命名注解 Flap --> Proxy，Proxy 代表背后的 ComponentProxy，更符合，而且 Flap 已经有类了，容易冲突；


### 1.5.2

1. 新增 ComponentPerformanceMonitor 用来监控组件性能；
2. Clean Code;

### 1.5.1

1. 重命名 `ComponentManager` --> `ComponentRegistry`;
2. `Component` 注解里新增`useDataBinding`，来支持 DataBinding，默认为 false; 

具体使用方法：

1. 将 `useDataBinding` 设置为 true;
2. 将组建的构造方法改为接收 ViewDataBinding 参数的构造方法（必须，否则会报错）;

```java
 @Component(layoutId = R.layout.flap_item_simple_databinding, useDataBinding = true)
 public class SimpleDataBindingComponent extends FlapComponent<SimpleDataBindingModel> {
     private FlapItemSimpleDatabindingBinding binding;
     public SimpleDataBindingComponent(@NonNull final ViewDataBinding binding) {
         super(binding.getRoot());
         this.binding = (FlapItemSimpleDatabindingBinding) binding;
     }
     @Override
     protected void onBind(@NonNull final SimpleDataBindingModel model) {
         binding.setModel(model);
         binding.executePendingBindings();
     }
 }
```

### 1.5.0
对齐脑子里的组件化设计思想，改了一堆名字，要挨骂了~

1. 移除 LayoutItemFactory（已废弃），用了反射影响性能；
2. 移除 LifecycleItem（已废弃），现在在 FlapComponent 内就能直接接收到生命周期回调；
3. 重命名 FlapItemFactory --> ComponentProxy，ComponentProxy 的职责更像一个代理人而不是一个工厂类，更加符合设计理念；
4. 重命名 FlapItem --> Component，对应"组件"概念；
5. 新增 ComponentFlowListener，可以监听关于组件的流程回调事件，方便做一些类似性能监控的事；

### 1.4.0

1. FlapItem.onBind 方法参数调整，新增 position 参数
2. Flap 注解新增`autoRegister`配置，支持是否自动注册，默认情况下 Processor 会自动注册，可以通过该参数修改
3. FlapAdapter 新增 `setDataAndNotify(@NonNull List<?> data)`方法，支持设置 data 后自动调用 `notifyDataSetChanged()` 方法。

### 1.3.0

1.3 版本新增重磅功能：在编译时完成所有注解生成的工厂类的自行注册操作，再也不需要手动写一行注解代码了！

注意：暂时只支持单模块应用。

### 1.2.0 : Add `@Component` annotation !

1.2 的主要功能是为 `Flap` 增加注解功能 `@Component` ，通过注解自动生成 FlapItem 的工厂类，帮助减少样板代码的编写。

1. Add `flap-annotations` project
2. Add `flap-compiler` project
3. Deprecate `LayoutItemFactory` class , please use `@Flap` instead

Here is a sample for `@Flap` :

```java
@Flap(layoutId = R.layout.flap_item_simple_image)
public class SimpleImageItem extends FlapItem<SimpleImageModel> {

    public SimpleImageItem(final View itemView) {
        super(itemView);
    }

    @Override
    protected void onBind(@NonNull final SimpleImageModel model, @NonNull final FlapAdapter adapter, @NonNull final List<Object> payloads) {

    }

}
```

### 1.1.0

第一版正式版，优化了架构。

### 0.9.3

1. Add DifferFlapAdapter which supports AsyncListDiffer.


### 0.9.1

1. Add global RecycledViewPool


### Previously versions

Check https://github.com/AlanCheen/Flap/releases for details.