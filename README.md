# Flap

[ ![Download](https://api.bintray.com/packages/alancheen/maven/flap/images/download.svg?version=0.2.0) ](https://bintray.com/alancheen/maven/flap/0.2.0/link)
 
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


### Step 1 : Create a model class , e.g. :

```java
public class SimpleTextModel {

    @NonNull
    public String content;

    public SimpleTextModel(@NonNull final String content) {
        this.content = content;
    }
}
```

### Step 2 : Create a ViewHolder class extends `FlapViewHolder` and override `onBind` method , BTW create a inner class
 SimpleTextItemFactory extends `LayoutTypeItemFactory`:

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

### Step 3 : create your `FlapAdapter` and register the `SimpleTextItemFactory` that we already created , setup the models :

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

![](art/flap-simple-showcase.png)

## More Feature

todo

## Change Log

//todo

## Contact Me

//todo

## Thanks

This project is using https://github.com/StefMa/bintray-release to upload aar to jCenter.

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
