# Flap


[![Build Status](https://travis-ci.org/AlanCheen/Flap.svg?branch=master)](https://travis-ci.org/AlanCheen/Flap) ![AndroidX](https://img.shields.io/badge/AndroidX-Migrated-brightgreen) ![RecyclerView](https://img.shields.io/badge/RecyclerView-1.1.0-brightgreen.svg) ![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat) [![license](https://img.shields.io/github/license/AlanCheen/Flap.svg)](./LICENSE) [![Author](https://img.shields.io/badge/%E4%BD%9C%E8%80%85-%E7%A8%8B%E5%BA%8F%E4%BA%A6%E9%9D%9E%E7%8C%BF-blue.svg)](https://github.com/AlanCheen) [![PRs welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](https://github.com/AlanCheen/Flap/pulls)

------
[中文指南看这里(点我点我)](./README.md)

`Flap` is a library that makes `RecyclerView.Adapter` much more easier to use , by keeping you from writing boilerplate codes and providing lots advance features , especially when you have to support lots of different type items.


### Latest Version


| module  | flap                                                         | flap-annotations                                             | flap-compiler                                                | plugin                                                       |
| ------- | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| Version | [![Download](https://api.bintray.com/packages/alancheen/maven/flap/images/download.svg)](https://bintray.com/alancheen/maven/flap/_latestVersion) | [![Download](https://api.bintray.com/packages/alancheen/maven/flap-annotations/images/download.svg)](https://bintray.com/alancheen/maven/flap-annotations/_latestVersion) | [![Download](https://api.bintray.com/packages/alancheen/maven/flap-compiler/images/download.svg)](https://bintray.com/alancheen/maven/flap-compiler/_latestVersion) | [![Download](https://api.bintray.com/packages/alancheen/maven/plugin/images/download.svg)](https://bintray.com/alancheen/maven/plugin/_latestVersion) |


## Getting Started

### Integrate Flap

1)Add the latest `Flap` to your dependencies:

```groovy
dependencies {
  //add recyclerview also
  implementation 'androidx.recyclerview:recyclerview:1.1.0'

  implementation "me.yifeiyuan.flap:flap:$lastest_version"
  implementation "me.yifeiyuan.flap:flap-annotations:$lastest_version"
  annotationProcessor "me.yifeiyuan.flap:flap-compiler:$lastest_version"
}
```

If you are using kotlin , replace `annotationProcessor` with `kapt` :

```groovy
apply plugin: 'kotlin-android'
apply plugin: 'kotlin-android-extensions'
apply plugin: 'kotlin-kapt'

dependencies {
  implementation "me.yifeiyuan.flap:flap:$lastest_version"
  implementation "me.yifeiyuan.flap:flap-annotations:$lastest_version"
  kapt "me.yifeiyuan.flap:flap-compiler:$lastest_version"
}
```



2) Add Flap Gradle Plugin:

Modify project/build.gradle :

```groovy
buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        //添加插件
        classpath "me.yifeiyuan.flap:plugin:$lastest_version"
    }
}
```

Modify app/build.gradle：

```groovy
apply plugin: 'me.yifeiyuan.flap.plugin'	
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



#### Step 2: Create a layout :



Create a layout of a component ,let's say "flap_item_simple_text". Copy the name BTW.



#### Step 3 : Create a custom `Component`  :



Create a component class and add @Proxy annotation：

```java
@Proxy(layoutName = "flap_item_simple_text")
public class SimpleTextComponent extends Component<SimpleTextModel> {

    private TextView tvContent;

    public SimpleTextComponent(final View itemView) {
        super(itemView);
        tvContent = findViewById(R.id.tv_content);
    }

    @Override
    protected void onBind(@NonNull final SimpleTextModel model) {
        tvContent.setText(model.content);
    }
}
```


#### Step 3 : Create a `FlapAdapter` and setup your data


Create your `FlapAdapter` and setup data :


```java

FlapAdapter adapter = new FlapAdapter();

List<Object> models = new ArrayList<>();

models.add(new SimpleTextModel("Android"));
models.add(new SimpleTextModel("Java"));
models.add(new SimpleTextModel("Kotlin"));

//set your models to FlapAdapter
adapter.setData(models);

recyclerView.setAdapter(adapter);
```

Done , you are good to go!

<div align=center><img width="360" height="640" src="assets/flap-simple-showcase.png"/></div>


## More Advanced Features

`Flap` adds some features for `Component` : 

```java
protected final Context context；
  
protected abstract void onBind(@NonNull final T model)
  
protected void onBind(@NonNull final T model, int position, @NonNull final List<Object> payloads, @NonNull final FlapAdapter adapter)

protected void onViewAttachedToWindow(final FlapAdapter flapAdapter)
protected void onViewDetachedFromWindow(final FlapAdapter flapAdapter)

protected void onVisibilityChanged(final boolean visible)
public boolean isVisible()

protected void onViewRecycled(final FlapAdapter flapAdapter)
protected boolean onFailedToRecycleView(final FlapAdapter flapAdapter)

protected final <V extends View> V findViewById(@IdRes int viewId)
```



### Enable Lifecycle

You can get the lifecycle callbacks : `onResume` 、`onPause`、`onStop`、`onDestroy`  by default , `FlapAdapter` binds the `LifecycleOwner` automatically.


Releated methods :

1. `FlapAdapter.setLifecycleEnable(boolean lifecycleEnable) `   enabled by default

2. `FlapAdapter.setLifecycleOwner(@NonNull final LifecycleOwner lifecycleOwner)`



#### Databinding

If you'd like to use Databinding in a component , just set `useDataBinding=true` and modify the constructor of component：

```java
//1. Set useDataBinding = true
@Proxy(layoutName ="flap_item_simple_databinding", useDataBinding = true)
public class SimpleDataBindingComponent extends Component<SimpleDataBindingModel> {

    private FlapItemSimpleDatabindingBinding binding;
	  
   //2. Modify the constructor
    public SimpleDataBindingComponent(@NonNull final FlapItemSimpleDatabindingBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    @Override
    protected void onBind(@NonNull final SimpleDataBindingModel model) {
        binding.setModel(model);
        binding.executePendingBindings();
    }
}
```



### AsyncListDiffer supported



`Flap` provides a build-in adapter `DifferFlapAdapter` that supports `AsyncListDiffer` features.



## Change Log

Check [Releases](https://github.com/AlanCheen/Flap/releases) for details.



## Feature List

- [x] Support AsyncListDiffer feature;
- [x] Support setup global RecycledViewPool;
- [x] Support Lifecycle for FlapComponent;
- [x] Decouple RecyclerView.Adapter and ViewHolder's creating and binding logic.



## Contribution

Any feedback would be helpful , thanks!