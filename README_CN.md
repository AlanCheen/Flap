# Flap


[![Download](https://api.bintray.com/packages/alancheen/maven/flap/images/download.svg?version=1.0.0)](https://bintray.com/alancheen/maven/flap/1.0.0/link) [![Build Status](https://travis-ci.org/AlanCheen/Flap.svg?branch=master)](https://travis-ci.org/AlanCheen/Flap) ![RecyclerView](https://img.shields.io/badge/RecyclerView-28.0.0-brightgreen.svg) ![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat) [![license](https://img.shields.io/github/license/AlanCheen/Flap.svg)](./LICENSE) [![Author](https://img.shields.io/badge/%E4%BD%9C%E8%80%85-%E7%A8%8B%E5%BA%8F%E4%BA%A6%E9%9D%9E%E7%8C%BF-blue.svg)](https://github.com/AlanCheen) [![PRs welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](https://github.com/AlanCheen/Flap/pulls)

## Flap 介绍

**Flap** 是一个专门优化 `RecyclerView.Adapter` 使用体验的库，**解决各种开发中遇到的痛点**，让你轻松而优雅的面对各种需求。



**Flap** 有超多的优点与特性，包括但不限于以下几点：



0. **简单、简洁、易懂以及无门槛**：Flap 在保留原生 RecyclerView 的各种概念基础之上，提供超级简单的 API，非常易于使用，可以说毫无门槛；
1. **清晰而优秀的架构**：Flap 的**每一行代码都是我经过我深思熟虑而诞生**，遵守 SOLID 设计原则
2. **完全解耦 ViewHolder 的创建以及绑定过程**：让你**不再迷失于混乱的创建以及绑定逻辑之中**，**把你从if else switch 中解救出来**，轻松面对各种加类型的需求；
3. **优化 ViewHolder 与 ItemViewType 的对应关系以及其绑定**：默认**使用 ViewHolder 的 布局Id 作为 ItemViewType** , 并做自动关联，**你再也不需要自定义多余且烦人的常量**；
4. **使用工厂模式为 ViewHolder 的创建提供支持**：而且，你可以**跟**  `new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.xxx, parent, false)));` **这种没营养但是又烦人的样板代码说再见！**；
5. **默认提供 ViewHolder 的绑定的最佳实践**：你只需要关注 `onBind()` 方法来处理你的绑定逻辑；
6. **完美支持最高效的刷新数据的方法**：让你一次集成达到最佳的优化效果；
7. **完美结合 AAC 框架中的 Lifecycle**：赋予 ViewHolder 感知生命周期的能力，**让你轻松面对类似 暂停/重启视频 这种依赖于生命周期的需求**；
8. **默认提供全局 RecycledViewPool** ：让每个创建过的 ViewHolder 得到充分使用；
9. 更多优点等你发现喔；



还在等什么，马上来试试吧！



## 一分钟入门指南



### 集成 Flap

添加 Flap 到你的依赖：

```groovy
dependencies {
    implementation 'me.yifeiyuan.flap:flap:$lastest_version'
}
```



### 基本使用




#### Step 1 : 创建你的 Model 类 :

```java
public class SimpleTextModel {

    @NonNull
    public String content;

    public SimpleTextModel(@NonNull final String content) {
        this.content = content;
    }
}
```

#### Step 2 : 创建一个 `FlapItem` 和它的 `LayoutItemFactory` :

注：`FlapItem` 是一个 `ViewHolder` ，在 Flap 内部使用 ，是 Flap 的基础，把你原来的 ViewHolder 继承它即可。

举个🌰 ：

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

#### Step 3 : 注册你的 `LayoutItemFactory` and 创建你的 `FlapAdapter` 并设置 data

将你的`LayoutItemFactory` 注册到 Flap，创建你的 `FlapAdapter` 并设置好 data。

**注：**`LayoutItemFactory` **只需要被注册一次**，所以你可以把注册的逻辑放到你的 `Application.onCreate` 中去。

```java
//注册你的 Factory 到 Flap
Flap.getDefault().register(new SimpleTextItem.Factory());

FlapAdapter adapter = new FlapAdapter();

List<Object> models = new ArrayList<>();

models.add(new SimpleTextModel("Android"));
models.add(new SimpleTextModel("Java"));
models.add(new SimpleTextModel("Kotlin"));

//设置你的 data
adapter.setData(models);

recyclerView.setAdapter(adapter);
```



这样就完全 OK啦！ 怎么样？超简单吧？！

欢迎使用喔！



## 更多其他的功能



结合实际开发的情况，我在 `FlapItem` 类中还添加了一些比较实用的功能，比如：



1. FlapItem 中有 `context` 字段，你可以直接拿到 context ，轻松；
2. FlapItem 提供 `findViewById()` 方法，你不需要写  `itemView.findViewById` 那么长，省事；
3. FlapItem 提供  `onViewAttachedToWindow` & `onViewDetachedFromWindow` 的回调，可以用于暂停/重播视频等功能，方便；



### 与 Lifecycle 完美结合的产物：LifecycleItem



在一些业务场景下我们在 `ViewHolder` 中需要感知生命周期，`Flap` 内置了一个 `LifecycleItem` ，通过继承它你就可以得到`onResume` 、`onPause`、`onStop`、`onDestroy`  的回调。



如果觉得不够，你也加更多的方法，甚至你可以让你的 FlapItem 实现 `LifecycleObserver` 接口，`FlapAdapter` 会帮你自动绑定 `LifecycleOwner` ，生命周期问题从此不再出现。



相关的方法：



1. `FlapAdapter.setLifecycleEnable(boolean lifecycleEnable) `   默认开启
2. `FlapAdapter.setLifecycleOwner(@NonNull final LifecycleOwner lifecycleOwner)`



### AsyncListDiffer 支持

AsyncListDiffer 能够非常高效的刷新数据的能力， `Flap`  内部提供一个 `DifferFlapAdapter` ，支持了 `AsyncListDiffer` ，你只需要继承 `DifferFlapAdapter` 就可以同时享受 `Flap` 跟 ` AsyncListDiffer` 带来的强大的能力。



## 变更日志

版本变更详情请看： [Releases](https://github.com/AlanCheen/Flap/releases) 。



## 功能列表

- [x] 支持 AsyncListDiffer，见 DifferFlapAdapter;
- [x] 支持设置全局的 RecycledViewPool;
- [x] 支持 Lifecycle;



## 贡献



- 发现 Flap 有 Bug？提 issue 告诉我！

- 发现 Flap 超好用？star 一波，安利给所有的好伙伴！
- 发现 有需要的功能 Flap 不具有？ 提 issue 告诉我！
- 任何意见和建议都可以提喔~



## 联系关注我

也非常欢迎关注我的公众号啦：

<p style="text-align: center;"><img alt="" src="https://cdn.nlark.com/yuque/0/2019/jpeg/138547/1546863515827-bd9dabf9-3e4b-4ea1-910f-e2f549b981cd.jpeg#align=left&display=inline&height=215&linkTarget=_blank&originHeight=430&originWidth=430&size=0&width=215" style="max-width: 250px; width: 215px;" /></p>

