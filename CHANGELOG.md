# 更新日志（CHANGELOG）



### 1.3.1


### 1.3

1.3 版本新增重磅功能：在编译时完成所有注解生成的工厂类的自行注册操作，再也不需要手动写一行注解代码了！

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