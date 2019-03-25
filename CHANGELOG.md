# 更新日志




### 1.2 : Support @Flap annotation now!

1.2 的主要功能是为 Flap 增加注解功能 `@Flap` ，通过注解自动生成 FlapItem 的工厂类，帮助减少样板代码的编写。

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