# Flap

[![Download](https://api.bintray.com/packages/alancheen/maven/flap/images/download.svg?version=0.6.0)](https://bintray.com/alancheen/maven/flap/0.6.0/link) [![Build Status](https://travis-ci.org/AlanCheen/Flap.svg?branch=master)](https://travis-ci.org/AlanCheen/Flap) ![RecyclerView](https://img.shields.io/badge/RecyclerView-28.0.0-brightgreen.svg) ![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat) [![license](https://img.shields.io/github/license/AlanCheen/Flap.svg)](./LICENSE)

**WARNING: Flap is still under development.**

Flap is a library that makes `RecyclerView.Adapter` more easier to use , especially when you have to support lots of different type ViewHolders.

Flap can save your day by keeping you from writing boilerplate codes.

Have a try , thanks !

## Integrate Flap

Add the latest Flap to your dependencies:

```groovy
dependencies {
    implementation 'me.yifeiyuan.flap:flap:$lastest_version'
}
```

## Usage


#### Step 1 : Create a model class :

A model class can be a POJO or Java Bean.

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

`FlapItem` is the base `ViewHolder` that `Flap` is used internally.

`FlapItemFactory` tells Flap how to create a  `FlapItem` as you wish.

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

    public static class SimpleTextItemFactory extends LayoutItemFactory<SimpleTextModel, SimpleTextItem> {

        @Override
        protected int getLayoutResId(final SimpleTextModel model) {
            return R.layout.flap_item_simple_text;
        }
    }

}
```



#### Step 3 : Register the `FlapItemFactory` and create your `FlapAdapter`

Create your `FlapAdapter` and register the `SimpleTextItemFactory` that we already created , setup the models :

```java
//register your ItemFactory to Flap
Flap.getDefault().register(SimpleTextModel.class, new SimpleTextItem.SimpleTextItemFactory());

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


## Todo List

- Support AsyncListDiffer
- Support Lifecycle

## Contribution

Any feedback would be helpful , thanks.



## Contact Me

I'm Fitz , an Engineer working at Alibaba living in China .

Follow me on :

- 微信公众号：chengxuyifeiyuan （程序亦非猿的拼音）
- [知乎](https://www.zhihu.com/people/yifeiyuan/activities)
- [新浪微博](https://www.weibo.com/alancheeen)
- [简书](https://www.jianshu.com/u/ec59bd61433a)
- [掘金](https://juejin.im/user/558cc8dae4b0de86abc9cfda)

Feel free to contact me.



## Apps are using Flap

Contact me if you are using Flap in your App.



## Thanks

I'm using [StefMa/bintray-release](https://github.com/StefMa/bintray-release) to publish Flap to jCenter.



## License

```
   Copyright 2018 程序亦非猿

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
