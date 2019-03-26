# Flap



`Flap` is a library that makes `RecyclerView.Adapter` much more easier to use , by keeping you from writing boilerplate codes and providing lots advance features , especially when you have to support lots of different type items.



------



[![Download](https://api.bintray.com/packages/alancheen/maven/flap/images/download.svg?version=1.2.0)](https://bintray.com/alancheen/maven/flap/1.2.0/link) [![Build Status](https://travis-ci.org/AlanCheen/Flap.svg?branch=master)](https://travis-ci.org/AlanCheen/Flap) ![RecyclerView](https://img.shields.io/badge/RecyclerView-28.0.0-brightgreen.svg) ![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat) [![license](https://img.shields.io/github/license/AlanCheen/Flap.svg)](./LICENSE) [![Author](https://img.shields.io/badge/%E4%BD%9C%E8%80%85-%E7%A8%8B%E5%BA%8F%E4%BA%A6%E9%9D%9E%E7%8C%BF-blue.svg)](https://github.com/AlanCheen) [![PRs welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](https://github.com/AlanCheen/Flap/pulls)

[中文指南看这里(点我点我)](./README_CN.md)



### Lastest Version



| module  | flap                                                         | flap-annotations                                             | flap-compiler                                                |
| ------- | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| Version | [![Download](https://api.bintray.com/packages/alancheen/maven/flap/images/download.svg?version=1.2.0)](https://bintray.com/alancheen/maven/flap/1.2.0/link) | [![Download](https://api.bintray.com/packages/alancheen/maven/flap-annotations/images/download.svg)](https://bintray.com/alancheen/maven/flap-annotations/_latestVersion) | [![Download](https://api.bintray.com/packages/alancheen/maven/flap-compiler/images/download.svg)](https://bintray.com/alancheen/maven/flap-compiler/_latestVersion) |



## Getting Started



### Integrate Flap



Add the latest `Flap` to your dependencies:

```groovy
dependencies {
  
  implementation 'me.yifeiyuan.flap:flap:$lastest_version'
  
  implementation 'me.yifeiyuan.flap:flap-annotations:$lastest_version'
    
  annotationProcessor 'me.yifeiyuan.flap:flap-compiler:$lastest_version'
}
```



### Usage



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



#### Step 2 : Create a custom `FlapItem`  :



`FlapItem` is the base `ViewHolder` that `Flap` is using internally.

Here is a sample :

```java
@Flap(layoutId = R.layout.flap_item_simple_text)
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
}
```



#### Step 3 : Register `SimpleTextItemFactory` and create your `FlapAdapter`



Create your `FlapAdapter` and register the `Factory`  , setup the data by the way :

NOTE: Flap will generate a Factory for your FlapItem automatically , so build after creating a FlapItem first.

```java
//register your ItemFactory to Flap
// Flap will generate SimpleTextItemFactory class for you.
Flap.getDefault().register(new SimpleTextItemFactory());

FlapAdapter adapter = new FlapAdapter();

List<Object> models = new ArrayList<>();

models.add(new SimpleTextModel("Android"));
models.add(new SimpleTextModel("Java"));
models.add(new SimpleTextModel("Kotlin"));

//set your models to FlapAdapter
adapter.setData(models);

recyclerView.setAdapter(adapter);
```

NOTE: Just register Factory one time is enough , so you can register your Factories in your custom Application.

Yeah , you are good to go!

<div align=center><img width="360" height="640" src="assets/flap-simple-showcase.png"/></div>



## More Advanced Features



`Flap` adds some features for `FlapItem` : 

1. Access a context directly by field `context`.
2. Call `findViewById()`  directlly instead of `itemView.findViewById` when you want to find a view.
3. Override `onViewAttachedToWindow` & `onViewDetachedFromWindow` so that you can do something like pausing or resuming a video.



### Enable Lifecycle



By extending `LifecycleItem`  , a lifecycle-aware `ViewHolder`  , you can get the lifecycle callbacks : `onResume` 、`onPause`、`onStop`、`onDestroy`  by default , when you care about the lifecycle , `FlapAdapter` binds the `LifecycleOwner` automatically.


Releated methods :

1. `FlapAdapter.setLifecycleEnable(boolean lifecycleEnable) `   enabled by default

2. `FlapAdapter.setLifecycleOwner(@NonNull final LifecycleOwner lifecycleOwner)`



### AsyncListDiffer supported



`Flap` provides a build-in adapter `DifferFlapAdapter` that supports `AsyncListDiffer` feature.



## Change Log

Check [Releases](https://github.com/AlanCheen/Flap/releases) for details.



## Feature List

- [x] Support AsyncListDiffer feature;
- [x] Support setup global RecycledViewPool;
- [x] Support Lifecycle for FlapItem;
- [x] Decouple RecyclerView.Adapter and ViewHolder's creating and binding logic.



## Contribution

Any feedback would be helpful , thanks!