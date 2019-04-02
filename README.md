# Flap

[![Build Status](https://travis-ci.org/AlanCheen/Flap.svg?branch=master)](https://travis-ci.org/AlanCheen/Flap) ![RecyclerView](https://img.shields.io/badge/RecyclerView-28.0.0-brightgreen.svg) ![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat) [![license](https://img.shields.io/github/license/AlanCheen/Flap.svg)](./LICENSE) [![Author](https://img.shields.io/badge/%E4%BD%9C%E8%80%85-%E7%A8%8B%E5%BA%8F%E4%BA%A6%E9%9D%9E%E7%8C%BF-blue.svg)](https://github.com/AlanCheen) [![PRs welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](https://github.com/AlanCheen/Flap/pulls)

------



`Flap` 是一个专门优化 `RecyclerView.Adapter` 使用体验的库，**解决许多开发中遇到的痛点**，让你轻松而优雅的面对各种需求。

[README_EN](./README_EN.md)

### Flap 的优点与特性

  

Flap 重点类先知：

- `FlapItem`，它是 `Flap` 对原生 `ViewHolder` 的封装扩展，提供了更多优秀的功能以及便捷的方法，将你原来的 `ViewHolder` 继承它即可（可以理解为一个有更多功能的 `ViewHolder`）；
- `FlapAdapter` 是对 `RecyclerView.Adapter` 的封装扩展，优化了大量逻辑，如解耦了 `ViewHolder` 创建以及绑定等相关逻辑，让 `Adapter` 更加易用；



### Flap 的优点



可以说 `Flap` 的每一行代码都是我经过我深思熟虑而写下的，它拥有**精心设计的架构**，它遵守 SOLID 设计原则，与设计模式完美融合，做到**高内聚低耦合，易扩展易维护**；并且**最大程度上帮助开发者避免编写样板代码**，让开发者关注绑定逻辑即可；同时提供了**非常多的实用特性**，难能可贵的是它还做到了**简单易用、无门槛**；我相信你一定能够做到「一分钟入门」，并且我也相信 `Flap` 一定会是你的得力助手。



1. `Flap` 优化了 `FlapItem` 与 `ItemViewType` 的绑定逻辑，**默认使用 ViewHolder 的 布局Id（也即 layoutId）作为它的 ItemViewType**，并做自动关联，你再也不需要自定义多余且烦人的常量！！；
2. **使用工厂模式为 FlapItem 的创建提供支持**：而且，你可以**跟**  `new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.xxx, parent, false)));` **这种没营养但是又烦人的样板代码说再见！**；
3. **优化 FlapItem 的创建过程**：FlapItem 的创建过程不像传统的 ViewHolder 那样混乱，真正做到了「开闭原则」，让你**不再迷失于混乱的 if else 、switch 堆中**，轻松面对各种加类型的需求；
4. **绑定 ViewHolder 的最佳实践**：遵从**关注点分离**，**完全解耦 FlapItem 的创建以及绑定过程**，你不需要关注 FlapItem 是怎么创建的，而只需要关注 `onBind()` 方法来处理你的数据绑定逻辑即可；



#### Flap 的功能特性



1. `FlapItem` 内置成员变量 `context` 可以轻松获取 `context` 对象；
2. `FlapItem` 内置 `findViewById(@IdRes int viewId)` 方法，替代`itemView.findViewById`，提高效率；
3. `FlapItem` 提供 `onViewAttachedToWindow` 和 `onViewDetachedFromWindow` 方法的回调，你可以通过重写轻松获取；
4. 内置 `DifferFlapAdapter` 支持 `AsyncListDiffer` ——目前 RecyclerView **最高效的刷新数据的方式**，让你一次集成就达到最佳的优化效果;
5. 默认设置全局的 `RecycledViewPool` ，并支持自定义设置：让每个创建过的 `FlapItem` 得到充分使用;
6. 内置 `LifecycleItem` ：支持感知 Activity/Fragment 的生命周期事件，**让你轻松面对类似 暂停/重播视频 这种依赖于生命周期的需求**;



### 你没集成过的全新版本


| module  | flap                                                         | flap-annotations                                             | flap-compiler                                                |
| ------- | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| Version | [![Download](https://api.bintray.com/packages/alancheen/maven/flap/images/download.svg)](https://bintray.com/alancheen/maven/flap/_latestVersion) | [![Download](https://api.bintray.com/packages/alancheen/maven/flap-annotations/images/download.svg)](https://bintray.com/alancheen/maven/flap-annotations/_latestVersion) | [![Download](https://api.bintray.com/packages/alancheen/maven/flap-compiler/images/download.svg)](https://bintray.com/alancheen/maven/flap-compiler/_latestVersion) |



## 一分钟入门指南



### 集成 Flap

添加 `Flap` 的各个模块的最新版本到你的依赖：

```groovy
dependencies {
  implementation 'me.yifeiyuan.flap:flap:$lastest_version'
  
  implementation 'me.yifeiyuan.flap:flap-annotations:$lastest_version'
    
  annotationProcessor 'me.yifeiyuan.flap:flap-compiler:$lastest_version'
}
```



### Flap 基本使用教程




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

#### Step 2 : 创建一个 `FlapItem` 并用`@Flap`修饰 :

注：`FlapItem` 是一个 `ViewHolder` ，在 `Flap` 内部使用 ，是 `Flap` 的基础，把你原来的 `ViewHolder` 继承它即可。

需要在 @Flap 注解中给 `layoutId` 赋值为该 Item 的布局 id ，这样你就不需要自己写 ViewHolder 的实例化啦。

举个🌰 ：

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



#### Step 3 : 创建你的 `FlapAdapter` 并设置 data


创建你的 `FlapAdapter` 并设置好 data 即可。（FlapAdapter 是一个内置的 Adapter）

```java
//创建你的 FlapAdapter
FlapAdapter adapter = new FlapAdapter();

List<Object> models = new ArrayList<>();

models.add(new SimpleTextModel("Android"));
models.add(new SimpleTextModel("Java"));
models.add(new SimpleTextModel("Kotlin"));

//设置你的 data
adapter.setData(models);

recyclerView.setAdapter(adapter);
```

这样就完全 OK 啦！ 咱们跑起来看看：

<div align=center><img width="360" height="640" src="assets/flap-simple-showcase.png"/></div>



怎么样？超简单吧？！



### 与 Lifecycle 完美结合的产物：LifecycleItem



在一些业务场景下我们在 `ViewHolder` 中需要感知生命周期，`Flap` 内置了一个 `LifecycleItem` ，通过继承它你就可以得到`onResume` 、`onPause`、`onStop`、`onDestroy`  的回调。



如果觉得不够，你也加更多的方法，甚至你可以让你的 FlapItem 实现 `LifecycleObserver` 接口，`FlapAdapter` 会帮你自动绑定 `LifecycleOwner` ，生命周期问题从此不再出现。



相关的方法：



1. `FlapAdapter.setLifecycleEnable(boolean lifecycleEnable) `   默认开启
2. `FlapAdapter.setLifecycleOwner(@NonNull final LifecycleOwner lifecycleOwner)`



### AsyncListDiffer 完全支持



`AsyncListDiffer` 能够非常高效的刷新数据的能力， `Flap`  内部提供一个 `DifferFlapAdapter` ，支持了 `AsyncListDiffer` ，你只需要继承 `DifferFlapAdapter` 就可以同时享受 `Flap` 跟 ` AsyncListDiffer` 带来的强大的能力。



## 变更日志



版本变更详情请看： [CHANGELOG](./CHANGELOG.md) 。





## FAQ

#### 1. 如何设置 FlapItem 的点击事件？

答：Flap 并没有提供一个全局的点击事件处理方法，而是推荐在 FlapItem 的 onBind 方法里给 itemView 设置 onClick 事件，这样更清晰。

#### 2. 我想在 FlapItem 里用 context 怎么办？

答：`FlapItem` 有个字段 `context` 你可以直接访问使用。

#### 3. Flap 有上拉加载、Header/Footer的功能吗？

答：没有，Flap 的目标不在此，可以自行扩展。



### 谁在使用 Flap ？

如果你在你的 App 使用了 Flap 开发，请一定要联系我，将会在这里展示哟。




## 贡献

- 发现 `Flap` 有 Bug？提 [issue](https://github.com/AlanCheen/Flap/issues) 告诉我！
- 发现 `Flap` 超好用？**star 一波，安利给所有的小伙伴！**
- 发现 有需要的功能 `Flap` 不具有？ 提 [issue](https://github.com/AlanCheen/Flap/issues) 告诉我！
- 任何意见和建议都可以提喔~



## 贡献者列表



感谢以下人员对 `Flap` 提供的帮助：

- [dreamkong](https://github.com/dreamkong)
- [Fitz](https://github.com/finalrose7)
- [Halouyao](https://github.com/doooyao)
- [码小猪](https://www.hchstudio.cn/)
- [大脑好饿](http://www.imliujun.com/)
- [zhousysu](https://github.com/zhousysu)
- [阿呆](http://blogyudan.online/)



## 联系我

我是程序亦非猿，阿里巴巴资深无线开发工程师一枚，如果有任何想法也非常欢迎通过公众号联系我，谢谢。

<div align=center><img width="215" height="215" src="assets/public-wechat.jpeg"/></div>

