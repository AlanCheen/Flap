# Flap

[ ![Download](https://api.bintray.com/packages/alancheen/maven/flap/images/download.svg?version=0.3.0) ](https://bintray.com/alancheen/maven/flap/0.2.0/link)

Flap is an Android library that make RecyclerView.Adapter more easier to use.

Especially when you have to support lots of different type ViewHolders.

Flap will make you enjoy developing ViewHolders.

And you will save a lot of time by avoiding writing lots of boilerplate code with Flap.

Btw , Flap integrated with Lifecycle , you can get the lifecycle callback easily which is very helpful.

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

A model class can be a POJO or Java bean.

```java
public class SimpleTextModel {

    @NonNull
    public String content;

    public SimpleTextModel(@NonNull final String content) {
        this.content = content;
    }
}
```

#### Step 2 : Create a `FlapViewHolder` and  `LayoutTypeItemFactory` :

`FlapViewHolder` is the base ViewHolder class that Flap is using which provides useful methods.

`LayoutTypeItemFactory` is a `ItemFactory` that tells Flap how to create a `FlapViewHolder`.

Here is a sample :

```java
public class SimpleTextItemViewHolder extends FlapViewHolder<SimpleTextModel> {

    private TextView tvContent;

    public SimpleTextItemViewHolder(final View itemView) {
        super(itemView);
        tvContent = findViewById(R.id.tv_content);
    }

    @Override
    protected void onBind(final SimpleTextModel model) {
        tvContent.setText(model.content);
    }

    public static class SimpleTextItemFactory extends LayoutTypeItemFactory<SimpleTextModel, SimpleTextItemViewHolder> {
        @Override
        protected int getLayoutResId(final SimpleTextModel model) {
            return R.layout.flap_item_simple_text;
        }
    }

}
```

#### Step 3 : Create a `FlapAdapter` and register the `LayoutTypeItemFactory`

Create your `FlapAdapter` and register the `SimpleTextItemFactory` that we already created , setup the models :

```java
    RecyclerView recyclerView = findViewById(R.id.rv_items);

    FlapAdapter adapter = new FlapAdapter();

    adapter.registerItemFactory(new SimpleTextItemFactory());

    List<Object> models = new ArrayList<>();

    models.add(new SimpleTextModel("Android"));
    models.add(new SimpleTextModel("Java"));
    models.add(new SimpleTextModel("Kotlin"));
    adapter.setModels(models);

    recyclerView.setAdapter(adapter);
```

You are good to go!

For more feature please refer [More Feature](https://github.com/AlanCheen/Flap#more-feature)

![](art/flap-simple-showcase.png)

## More Feature

I add some features for FlapViewHolder : 

1. Access a context directly by field `context`
2. Call `findViewById()` instead of `itemView.findViewById`

What's more , here are some methods for you that you can override if you need :

1. Overrider `onBind(final T model, final FlapAdapter adapter, final List<Object> payloads)` If you wanna access your adapter or payloads.
2. Overrider `onViewAttachedToWindow` & `onViewDetachedFromWindow` so that you can do something like pause or resume a video.
3. Overrider lifecycle callbacks : `onResume` 、`onPause`、`onStop`、`onDestroy` when you care about the lifecycle , FlapAdapter binds the LifecycleOwner automatically.

## Change Log

Check [Releases](https://github.com/AlanCheen/Flap/releases) for details.

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
