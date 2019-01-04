# Flap

[![Download](https://api.bintray.com/packages/alancheen/maven/flap/images/download.svg?version=0.9.0)](https://bintray.com/alancheen/maven/flap/0.9.0/link) [![Build Status](https://travis-ci.org/AlanCheen/Flap.svg?branch=master)](https://travis-ci.org/AlanCheen/Flap) ![RecyclerView](https://img.shields.io/badge/RecyclerView-28.0.0-brightgreen.svg) ![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat) [![license](https://img.shields.io/github/license/AlanCheen/Flap.svg)](./LICENSE) ![Author](https://img.shields.io/badge/%E4%BD%9C%E8%80%85-%E7%A8%8B%E5%BA%8F%E4%BA%A6%E9%9D%9E%E7%8C%BF-blue.svg)

**[WIP]WARNING: Flap is still under development.**


## What is Flap

`Flap` is a library that makes `RecyclerView.Adapter` much more easier to use , by keeping you from writing boilerplate codes and providing lots advance features , especially when you have to support lots of different type items.

Have a try !

## How to integrate Flap

Add the latest Flap to your dependencies:

```groovy
dependencies {
    implementation 'me.yifeiyuan.flap:flap:$lastest_version'
}
```

## How to use Flap


#### Step 1 : Create a model class :

```java
public class SimpleTextModel {

    @NonNull
    public String content;

    public SimpleTextModel(@NonNull final String content) {
        this.content = content;
    }
}
```

#### Step 2 : Create a `FlapItem` and a `FlapItemFactory` :

`FlapItem` is the base `ViewHolder` that `Flap` is using internally.

Here is a sample :

```java
public class SimpleTextItem extends FlapItem<SimpleTextModel> {

    private static final String TAG = "SimpleTextItem";

    private TextView tvContent;

    public SimpleTextItem(final View itemView) {
        super(itemView);
        tvContent = findViewById(R.id.tv_content);
    }

    @Override
    protected void onBind(@NonNull final SimpleTextModel model, @NonNull final FlapAdapter adapter, @NonNull final List<Object> payloads) {
        tvContent.setText(model.content);
    }

    public static class Factory extends LayoutItemFactory<SimpleTextModel, SimpleTextItem> {

        @Override
        protected int getLayoutResId(final SimpleTextModel model) {
            return R.layout.flap_item_simple_text;
        }
    }

}
```

#### Step 3 : Register `FlapItemFactory` and create your `FlapAdapter`

Create your `FlapAdapter` and register the `SimpleTextItemFactory` that we already created , setup the models :

```java
//register your ItemFactory to Flap
Flap.getDefault().register(new SimpleTextItem.Factory());

FlapAdapter adapter = new FlapAdapter();

List<Object> models = new ArrayList<>();

models.add(new SimpleTextModel("Android"));
models.add(new SimpleTextModel("Java"));
models.add(new SimpleTextModel("Kotlin"));

//set your models to FlapAdapter
adapter.setModels(models);

recyclerView.setAdapter(adapter);
```

Yeah , you are good to go!

<div align=center><img width="360" height="640" src="art/flap-simple-showcase.png"/></div>


## More Feature

Flap adds some features for `FlapItem` : 

1. Access a context directly by field `context`.
2. Call `findViewById()`  directlly instead of `itemView.findViewById` when you want to find a view.
3. Override `onViewAttachedToWindow` & `onViewDetachedFromWindow` so that you can do something like pausing or resuming a video.



### Enable Lifecycle



By extending `LifecycleItem`  , a lifecycle aware `ViewHolder`  , you can get the lifecycle callbacks : `onResume` 、`onPause`、`onStop`、`onDestroy`  by default , when you care about the lifecycle , `FlapAdapter` binds the `LifecycleOwner` automatically.


Releated methods :

1. `FlapAdapter.setLifecycleEnable(boolean lifecycleEnable) `   enabled by default

2. `FlapAdapter.setLifecycleOwner(@NonNull final LifecycleOwner lifecycleOwner)`



## Change Log

Check [Releases](https://github.com/AlanCheen/Flap/releases) for details.

## Feature List

- [ ] Loadmore feature;
- [ ] Empty and error status;
- [ ] Support AsyncListDiffer feature;
- [ ] Support setup global RecycledViewPool;
- [x] Support Lifecycle for FlapItem;
- [x] Decouple RecyclerView.Adapter and ViewHolder's creating and binding logic.

## Contact Me

Any feedback could be helpful , thanks.

I'm Fitz , an Engineer working at Alibaba living in China .

Follow me on :

- 微信公众号：chengxuyifeiyuan （程序亦非猿的拼音）
- [知乎](https://www.zhihu.com/people/yifeiyuan/activities)
- [新浪微博](https://www.weibo.com/alancheeen)
- [简书](https://www.jianshu.com/u/ec59bd61433a)
- [掘金](https://juejin.im/user/558cc8dae4b0de86abc9cfda)

Feel free to contact me. 