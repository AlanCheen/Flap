# 更新日志（CHANGELOG）



### 1.4.1

1. FlapItem.onBind 方法参数调整，新增 position 参数
2. Flap 注解新增`autoRegister`配置，支持是否自动注册，默认情况下 Processor 会自动注册，可以通过该参数修改
3. FlapAdapter 新增 `setDataAndNotify(@NonNull List<?> data)`方法，支持设置 data 后自动调用 `notifyDataSetChanged()` 方法。

### 1.3

1.3 版本新增重磅功能：在编译时完成所有注解生成的工厂类的自行注册操作，再也不需要手动写一行注解代码了！

注意：暂时只支持单模块应用。

### 1.2 : Add `@Flap` annotation !

1.2 的主要功能是为 `Flap` 增加注解功能 `@Flap` ，通过注解自动生成 FlapItem 的工厂类，帮助减少样板代码的编写。

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