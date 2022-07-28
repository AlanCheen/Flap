# Flap(灵动)

[![Build Status](https://travis-ci.org/AlanCheen/Flap.svg?branch=master)](https://travis-ci.org/AlanCheen/Flap) ![AndroidX](https://img.shields.io/badge/AndroidX-Migrated-brightgreen) ![RecyclerView](https://img.shields.io/badge/RecyclerView-1.1.0-brightgreen.svg) ![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat) [![license](https://img.shields.io/github/license/AlanCheen/Flap.svg)](./LICENSE) [![Author](https://img.shields.io/badge/%E4%BD%9C%E8%80%85-%E7%A8%8B%E5%BA%8F%E4%BA%A6%E9%9D%9E%E7%8C%BF-blue.svg)](https://github.com/AlanCheen) [![PRs welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](https://github.com/AlanCheen/Flap/pulls)

------

`Flap` 是一个基于 `RecyclerView` 的页面组件化解决方案，提供更好的开发体验和更强大的功能，让你更关注业务，帮你提高开发效率。


在语雀写了体验更好的文档，可以前往语雀查看。

> - [Flap 接入文档](https://www.yuque.com/cxyfy/blog/ghsc4b)；
> - [Flap 更新日志](https://www.yuque.com/cxyfy/blog/ehnxdy)；
> - [Flap 设计理念](https://www.yuque.com/cxyfy/blog/ehnxdy)；



## Flap 的特点

0. **功能强大**：在保留 `RecyclerView` 原有的基本开发思路基础之上加了许多强大的功能，例如更好用的 `ViewHolder` 封装类 `Component`，更贴合实际开发需求;

1. **组件化**：页面组件化让页面的开发效率更上一层楼；

2. **高效**：Flap 最大程度上减少了样板代码，例如无需自定义 `itemViewType` ，也不需要自己实例化组件， 你可以和 `new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.xxx, parent, false)));` 这种没营养但是又烦人的样板代码彻底说再见！；

3. **关注点分离**：Flap 努力让开发者聚焦于业务，开发者只需要关注 `onBind()` 方法来处理你的数据绑定逻辑即可；

4. **优良的架构**：精心设计的架构，遵守 SOLID 设计原则，做到高内聚低耦合，易扩展易维护；

## 接入指南

接入步骤：

1. 添加 jitpack 的 maven 仓库；
1. 添加 flap sdk 依赖；
1. 【可选】添加 flap-compiler ；

### **【必须】**添加 Jitpack maven
在根目录添加 jitpack 的 maven ：
```java
allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}
```

### **【必须】**按需添加 flap 相关 SDK

- **【必须】**flap ：核心库必须依赖；
- **【可选】**flap-annotations 和 flap-compiler ：如果不想用 APT 代码生成则可以不依赖
```groovy
apply plugin: 'kotlin-kapt'

dependencies{
    // 【可选】依赖 recyclerview
    implementation 'androidx.recyclerview:recyclerview:1.1.0'

    def flapVersion = '3.0.0.0-test'
    implementation "com.github.AlanCheen.Flap:flap:$flapVersion@aar"

    //可选，如果使用 APT 则需要添加
    implementation "com.github.AlanCheen.Flap:flap-annotations:$flapVersion"
    kapt "com.github.AlanCheen.Flap:flap-compiler:$flapVersion"
}
```

**【可选】如果要使用代码生成功能**，则需要配置所需要的 `packageName` 参数：
```groovy
android {
    //...
    defaultConfig {
        //...
        javaCompileOptions {
            annotationProcessorOptions {
                arguments = [packageName: '你模块的包名']
            }
        }
    }
}
```
## 基础使用教程

教程中包含基础的功能，涉及到：

- FlapAdapter
- Component
- Delegate 注解

基础使用的步骤大致如下：

- 定义你的 Model，可能已经有了；
- 根据 Model 定义 Component，Model 与 Component 一一对应；
- 为 Component 定义 AdapterDelegate，方式有二：
   - 使用 `@Delegate`  注解修饰 Component；
   - 自定义 AdapterDelegate 实现；
- 向 FlapAdapter 注册 AdapterDelegate
- FlapAdapter.setData 设置数据
<a name="Cyd5G"></a>
### Step 1 : 为组件创建一个 Model 类 :

Adapter 中可能会有多个 Model 类型，实际开发按需创建。<br />举例，创建一个包含简单字符串的模型 SimpleTextModel：
```java
public class SimpleTextModel {

    @NonNull
    public String content;

    public SimpleTextModel(@NonNull final String content) {
        this.content = content;
    }
}
```

<a name="fTFF8"></a>
### Step 2 : 创建一个组件布局 layout 文件："flap_item_simple_text"

该布局文件是 Component 组件使用的，复制该文件的名字，下一步需要用。<br />如果已经有布局了，则可以跳过新建步骤。

<a name="GDzdA"></a>
### Step 3 : 定义一个类继承 `Component`
定义一个 `SimpleTextComponent` 继承 Component ，并**按需**重写 `onBind()` 方法处理绑定逻辑。<br />代码示例：
```kotlin
class SimpleTextComponent(itemView: View) : Component<SimpleTextModel>(itemView) {

    private val tvContent: TextView = findViewById(R.id.tv_content)

    //参数更多 全面
    override fun onBind(model: SimpleTextModel, position: Int, payloads: List<Any>, adapter: FlapAdapter, delegate: AdapterDelegate<*, *>) {
        FLogger.d(TAG, "onBind() called with: model = $model, position = $position, payloads = $payloads, adapter = $adapter")
        tvContent.text = model.content
    }

    //参数更少的 onBind
    override fun onBind(model: SimpleTextModel) {
        FLogger.d(TAG, "onBind() called with: model = $model")
    }

    companion object {
        private const val TAG = "SimpleTextItem"
    }
}
```

Component 还有更多用法，可以见后文。
<a name="yTIB3"></a>
### Step 4：定义 AdapterDelegate
AdapterDelegate 的定义有两种方法。
<a name="nNn02"></a>
#### 1）使用 Delegate 注解 Component
:::info
使用注解必须依赖 flap-annotation 和 flap-compiler ，并配置正确。<br />具体参考前面章节。
:::

使用`@Delegate`  注解修饰刚创建的 Component，flap-compiler 会自动生成 一个名为 你的 **Component 类名+ AdapterDelegate 后缀**的实现类。

如果你是在**子模块**里使用，则**必须**使用 `layoutName` 配置：
```groovy
@Delegate(layoutName = "flap_item_simple_text")
class SimpleTextComponent(itemView: View) : Component<SimpleTextModel>(itemView)
```

如果是在 app 模块里使用，则还可以使用 `layoutId` 来配置：
```groovy
@Delegate(layoutId = R.layout.flap_item_simple_text)
```

> 因为子模块 R 文件的问题，更推荐使用 `layoutName`来配置。


SimpleTextComponent 使用注解后会自动生成 SimpleTextComponentAdapterDelegate 。
<a name="wEpX6"></a>
#### 2）自定义 AdapterDelegate
实现 AdapterDelegate 接口，并重写方法：

- `delegate()` ：是否负责代理 model，如果是就 return true，默认一个 AdapterDelegate 代理一个类型；
- `onCreateViewHolder()` : 创建 Component；
```kotlin
class SimpleTextComponentDelegate : AdapterDelegate<SimpleTextModel, SimpleTextComponent> {

    override fun delegate(model: Any): Boolean {
        return SimpleTextModel::class.java == model::class.java
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): SimpleTextComponent {
        return SimpleTextComponent(inflater.inflate(R.layout.flap_item_simple_text, parent, false))
    }
}
```

:::success
自定义 AdapterDelegate 会写样板代码，但是自由度更高；<br />使用注解能够提高效率，但是灵活度不高；<br />可以按个人喜好选择。
:::

<a name="c5ac3755"></a>
#### Step 5 : 创建你的 `FlapAdapter` 并设置数据

FlapAdapter 使用步骤：

1. 通过 `registerAdapterDelegate()` 注册 AdapterDelegate
1. 通过`setData()` 设置数据

示例：
```java
//创建你的 FlapAdapter
var adapter: FlapAdapter = FlapAdapter()

//注册 AdapterDelegate
adapter.registerAdapterDelegate(SimpleTextComponentAdapterDelegate())

val dataList = ArrayList<Any>()
dataList.add(SimpleTextModel("Android"))
dataList.add(SimpleTextModel("Java"))
dataList.add(SimpleTextModel("Kotlin"))

//设置你的 data
adapter.setData(dataList);

recyclerView.adapter = adapter
```

这样就完全 OK 啦！ 咱们跑起来看看：

<div align=center><img width="360" height="640" src="assets/flap-simple-showcase.png"/></div>


怎么样？超简单吧？！


## 进阶使用教程



### Component 高级用法



#### 更便捷好用的属性与回调方法

Component 继承自 ViewHolder 且根据实际研发经验添加了一些非常方便的属性和方法。

例如：


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


在一些业务场景下我们需要在 `ViewHolder` 中感知生命周期，在 `Component` 中你可以重写`onResume` 、`onPause`、`onStop`、`onDestroy`  方法，得到回调，**让你轻松面对类似 暂停/重播视频 这种依赖于生命周期的需求**。

如果觉得不够，你也加更多的方法。

`FlapAdapter` 会帮你自动绑定 `LifecycleOwner` ，生命周期问题从此不再出现。

相关的方法：

1. `FlapAdapter.setLifecycleEnable(boolean lifecycleEnable) `   默认开启
2. `FlapAdapter.setLifecycleOwner(@NonNull final LifecycleOwner lifecycleOwner)`

#### Component 使用 DataBinding 

如果你想在组件配合使用 `DataBinding` ，那么需要额外把 `@Proxy` 的 `useDataBinding` 设置为`true` 。

并且需要把构造函数修改为入参是你的 binding class ，举个例子：

```java
//1. 增加 useDataBinding = true
@Proxy(layoutName ="flap_item_simple_databinding", useDataBinding = true)
public class SimpleDataBindingComponent extends Component<SimpleDataBindingModel> {

    private FlapItemSimpleDatabindingBinding binding;
	  
   //2.构造方法入参修改了
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

#### Component 使用 ViewBinding 

如果你想在组件配合使用 `ViewBinding` ，那么需要额外把 `@Proxy` 的 `useViewBinding` 设置为`true` 。

并且需要把构造函数修改为入参是你的 binding class ，举个例子：

```java
//1. 增加 useViewBinding = true
@Proxy(layoutName = "flap_item_vb", useViewBinding = true)
public class ViewBindingComponent extends Component<VBModel> {

    private FlapItemVbBinding binding;
    //2.构造方法入参修改了
    public ViewBindingComponent(@NonNull FlapItemVbBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    @Override
    protected void onBind(@NonNull VBModel model) {
        binding.tvContent.setText("ViewBinding Sample");
    }
}
```


### 想用 AsyncListDiffer ？完全支持！

`AsyncListDiffer` 能够非常高效的刷新数据的能力， `Flap`  内部提供一个 `DifferFlapAdapter` ，支持了 `AsyncListDiffer` ，你只需要继承 `DifferFlapAdapter` 就可以同时享受 `Flap` 跟 ` AsyncListDiffer` 带来的强大的能力。

## 变更日志

版本变更详情请看： [CHANGELOG](./CHANGELOG.md) 。

## FAQ

#### 1. 如何设置统一 Component 的点击事件？

答：不能，Flap 没有提供一个全局的点击事件处理方法，而是推荐在 Component 的 `onBind` 方法里给 itemView 设置 onClick 事件，这样更清晰。

#### 2. 我想在 Component 里用 context 怎么办？

答：`Component` 有个属性 `context` 你可以直接访问使用。

#### 3. Flap 有上拉加载、Header/Footer的功能吗？

答：没有，Flap 的目标不在此，可以自行扩展。


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