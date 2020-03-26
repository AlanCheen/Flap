# 更新日志（CHANGELOG）


### 2.0.x 计划 TODO

1. 尝试 Kotlin 化
2. 迁移 androidx 

### 1.6.x 计划 TODO

1. 增加 DataBinding 支持
2. 新增 IComponentModel（待定 TODO）

### 1.5.x 

#### 1.5.0
对齐脑子里的组件化设计思想，改了一堆名字，要挨骂了~

1. 移除 LayoutItemFactory（已废弃），用了反射影响性能；
2. 移除 LifecycleItem（已废弃），现在在 FlapComponent 内就能直接接收到生命周期回调；
3. 重命名 FlapItemFactory --> ComponentProxy，ComponentProxy 的职责更像一个代理人而不是一个工厂类，更加符合设计理念；
4. 重命名 FlapItem --> Component，对应"组件"概念；
5. 新增 ComponentFlowListener，可以监听关于组件的流程回调事件，方便做一些类似性能监控的事；

### 1.4.x 计划

#### 1.4.1

1. FlapItem.onBind 方法参数调整，新增 position 参数
2. Flap 注解新增`autoRegister`配置，支持是否自动注册，默认情况下 Processor 会自动注册，可以通过该参数修改
3. FlapAdapter 新增 `setDataAndNotify(@NonNull List<?> data)`方法，支持设置 data 后自动调用 `notifyDataSetChanged()` 方法。

### 1.3

1.3 版本新增重磅功能：在编译时完成所有注解生成的工厂类的自行注册操作，再也不需要手动写一行注解代码了！

注意：暂时只支持单模块应用。

### 1.2 : Add `@Component` annotation !

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

Make Flap project arch better.


### 0.9.3

1. Add DifferFlapAdapter which supports AsyncListDiffer.


### 0.9.1

1. Add global RecycledViewPool


### Previously versions

Check https://github.com/AlanCheen/Flap/releases for details.