# Flap

[![Build Status](https://travis-ci.org/AlanCheen/Flap.svg?branch=master)](https://travis-ci.org/AlanCheen/Flap) ![AndroidX](https://img.shields.io/badge/AndroidX-Migrated-brightgreen) ![RecyclerView](https://img.shields.io/badge/RecyclerView-1.1.0-brightgreen.svg) ![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat) [![license](https://img.shields.io/github/license/AlanCheen/Flap.svg)](./LICENSE) [![Author](https://img.shields.io/badge/%E4%BD%9C%E8%80%85-%E7%A8%8B%E5%BA%8F%E4%BA%A6%E9%9D%9E%E7%8C%BF-blue.svg)](https://github.com/AlanCheen) [![PRs welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](https://github.com/AlanCheen/Flap/pulls)

------

`Flap` 是一个专门针对页面组件化方案的库，并且提供页面组件化的能力，提供更加强大的 `FlapComponent` 替代 `ViewHolder`，`FlapAdapter` 替代 `Adapter`，优化了很多较差的使用体验，**解决许多开发中遇到的痛点**，让你轻松而优雅的面对各种需求。

[README_EN](./README_EN.md)

## 一分钟入门指南



### 你没集成过的全新版本


| module  | flap                                                         | flap-annotations                                             | flap-compiler                                                | Flap-plugin                                                  |
| ------- | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| Version | [![Download](https://api.bintray.com/packages/alancheen/maven/flap/images/download.svg)](https://bintray.com/alancheen/maven/flap/_latestVersion) | [![Download](https://api.bintray.com/packages/alancheen/maven/flap-annotations/images/download.svg)](https://bintray.com/alancheen/maven/flap-annotations/_latestVersion) | [![Download](https://api.bintray.com/packages/alancheen/maven/flap-compiler/images/download.svg)](https://bintray.com/alancheen/maven/flap-compiler/_latestVersion) | [![Download](https://api.bintray.com/packages/alancheen/maven/flap-plugin/images/download.svg)](https://bintray.com/alancheen/maven/flap-plugin/_latestVersion) |



### 集成 Flap

1) 添加 `Flap` 的各个模块的最新版本到你的依赖：

```groovy
dependencies {  
  //recyclerview
  implementation 'androidx.recyclerview:recyclerview:1.1.0'

  implementation "me.yifeiyuan.flap:flap:$lastest_version"
  implementation "me.yifeiyuan.flap:flap-annotations:$lastest_version"
  annotationProcessor "me.yifeiyuan.flap:flap-compiler:$lastest_version"
}
```

注意，如果你使用 Kotlin 来写组件，那么你需要使用 `kapt` 来替代 `annotationProcessor`，否则注解将不能正确地生成类。

具体修改如下：

```groovy
//添加 kapt 插件
apply plugin: 'kotlin-kapt'

dependencies {
  implementation "me.yifeiyuan.flap:flap:$lastest_version"
  implementation "me.yifeiyuan.flap:flap-annotations:$lastest_version"
  kapt "me.yifeiyuan.flap:flap-compiler:$lastest_version"
}
```

2) 添加 flap-plugin 

在你的项目 build.gradle 添加插件的 classpath ：
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

在 app/build.gradle 中应用插件：
```groovy
apply plugin: 'me.yifeiyuan.flap.plugin'
```

### Flap 基本使用教程


#### Step 1 : 创建一个 Model 类 :

```java
public class SimpleTextModel {

    @NonNull
    public String content;

    public SimpleTextModel(@NonNull final String content) {
        this.content = content;
    }
}
```

#### Step 2 : 创建一个组件布局 layout 文件："flap_item_simple_text"

复制该文件的名字，下一步需要用。

#### Step 3 : 创建一个类继承 `Component` 并用 `@Proxy` 注解修饰 :

`Component` 是一个封装过的 `ViewHolder` ，把你原来的 `ViewHolder` 类继承它即可。

在 `@Proxy` 注解中给 `layoutName` 赋值为该组件的布局名字（不需要带 xml 后缀），并在 `onBind()` 方法里写绑定逻辑。

举个🌰 ：

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

#### Step 4 : 创建你的 `FlapAdapter` 并设置 data


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


## 进阶使用教程


### Component 高级用法


#### 更便捷好用的属性与回调方法


1. 内置成员变量 `context` 可以轻松获取 `context` 对象；
2. 内置 `findViewById(@IdRes int viewId)` 方法，替代`itemView.findViewById`，提高效率；
3. 提供 `onViewAttachedToWindow` 和 `onViewDetachedFromWindow` 方法的回调，你可以通过重写轻松获取；


更多属性与方法，具体请以实际代码为准：

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


#### Component 感知生命周期


在一些业务场景下我们需要在 `ViewHolder` 中需要感知生命周期，在 `Component` 中你可以重写`onResume` 、`onPause`、`onStop`、`onDestroy`  方法，得到回调，**让你轻松面对类似 暂停/重播视频 这种依赖于生命周期的需求**。

如果觉得不够，你也加更多的方法。


`FlapAdapter` 会帮你自动绑定 `LifecycleOwner` ，生命周期问题从此不再出现。

相关的方法：

1. `FlapAdapter.setLifecycleEnable(boolean lifecycleEnable) `   默认开启
2. `FlapAdapter.setLifecycleOwner(@NonNull final LifecycleOwner lifecycleOwner)`

### 想用 AsyncListDiffer ？完全支持！

`AsyncListDiffer` 能够非常高效的刷新数据的能力， `Flap`  内部提供一个 `DifferFlapAdapter` ，支持了 `AsyncListDiffer` ，你只需要继承 `DifferFlapAdapter` 就可以同时享受 `Flap` 跟 ` AsyncListDiffer` 带来的强大的能力。

## Flap 的优点

可以说 `Flap` 的每一行代码都是我经过我深思熟虑而写下的，它拥有**精心设计的架构**，它遵守 SOLID 设计原则，与设计模式完美融合，做到**高内聚低耦合，易扩展易维护**；并且**最大程度上帮助开发者避免编写样板代码**，让开发者关注绑定逻辑即可；同时提供了**非常多的实用特性**，难能可贵的是它还做到了**简单易用、无门槛**；我相信你一定能够做到「一分钟入门」，并且我也相信 `Flap` 一定会是你的得力助手。

1. `Flap` 优化了 `Component` 与 `ItemViewType` 的绑定逻辑，**默认使用 ViewHolder 的 布局Id（也即 layoutId）作为它的 ItemViewType**，并做自动关联，你再也不需要自定义多余且烦人的常量！！；
2. **使用工厂模式为 Component 的创建提供支持**：而且，你可以**跟**  `new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.xxx, parent, false)));` **这种没营养但是又烦人的样板代码说再见！**；
3. **优化 Component 的创建过程**：Component 的创建过程不像传统的 ViewHolder 那样混乱，真正做到了「开闭原则」，让你**不再迷失于混乱的 if else 、switch 堆中**，轻松面对各种加类型的需求；
4. **绑定 ViewHolder 的最佳实践**：遵从**关注点分离**，**完全解耦 Component 的创建以及绑定过程**，你不需要关注 Component 是怎么创建的，而只需要关注 `onBind()` 方法来处理你的数据绑定逻辑即可；

## 变更日志

版本变更详情请看： [CHANGELOG](./CHANGELOG.md) 。

## FAQ

#### 1. 如何设置 Component 的点击事件？

答：Flap 暂时没有提供一个全局的点击事件处理方法，而是推荐在 Component 的 `onBind` 方法里给 itemView 设置 onClick 事件，这样更清晰。

#### 2. 我想在 Component 里用 context 怎么办？

答：`Component` 有个属性 `context` 你可以直接访问使用。

#### 3. Flap 有上拉加载、Header/Footer的功能吗？

答：没有，Flap 的目标不在此，可以自行扩展。


## 谁在使用 Flap ？

如果你在你的 App 使用了 Flap 开发，请一定要联系我，将会在这里展示哟。

## 加群交流

请**优先加钉钉群**，因为阿里不让装微信，微信看得少。


钉钉群：


<div align=center><img width="373" height="481" src="./assets/flap_dingding_group.jpg"/></div>


还可以关注我的公众号：程序亦非猿

## TODO

- [x] P0 , 针对 Library 类型的 Module 处理，让 Flap 也能工作；
- [ ] 【P2】Kotlin 改造；
- [ ] 【P1】做一个 FlapRecyclerView 封装 FlapAdapter，进一步降低使用成本；
- [x] 支持 gradle plugin 实现组件的自动注册;
- [x] AndroidX 迁移；
- [x] Component 注解支持使用 DataBinding；
- [x] 使用 APT 自动生成 Component 相关样板代码；
- [x] 支持组件全局缓存；
- [x] 支持组件监听生命周期事件，Lifecycle 接入；
- [x] 支持 AsyncListDiffer；
- [x] 优化布局实例化样板代码；
- [x] 支持组件与 layoutId 绑定；


## 贡献

- 发现 `Flap` 有 Bug？提 [issue](https://github.com/AlanCheen/Flap/issues) 告诉我！
- 发现 `Flap` 超好用？**star 一波，安利给所有的小伙伴！**
- 发现有需要的功能 `Flap` 不具有？ 提 [issue](https://github.com/AlanCheen/Flap/issues) 告诉我！
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

## License

Apache 2.0