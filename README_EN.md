# Flap

[![Build Status](https://travis-ci.org/AlanCheen/Flap.svg?branch=master)](https://travis-ci.org/AlanCheen/Flap) ![AndroidX](https://img.shields.io/badge/AndroidX-Migrated-brightgreen) ![RecyclerView](https://img.shields.io/badge/RecyclerView-1.1.0-brightgreen.svg) ![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat) [![license](https://img.shields.io/github/license/AlanCheen/Flap.svg)](./LICENSE) [![Author](https://img.shields.io/badge/%E4%BD%9C%E8%80%85-%E7%A8%8B%E5%BA%8F%E4%BA%A6%E9%9D%9E%E7%8C%BF-blue.svg)](https://github.com/AlanCheen) [![PRs welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](https://github.com/AlanCheen/Flap/pulls)

------

## Usage

1. Step1 : create a Model
2. Step2 : create a layout file for component(A ViewHolder)
3. Step3 : create a AdapterDelegate and register it.


1）Create a Model, lets say SimpleTextModel ：
```kotlin
data class SimpleTextModel(val content: String)
```
2）A simple layout that containers a TextView ：
```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:background="#0ff0ff"
    android:orientation="horizontal">

    <TextView
        android:id="@+id/tv_content"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:gravity="center"
        android:minHeight="50dp"
        android:paddingLeft="16dp"
        android:text="content"
        android:textColor="@android:color/white"
        android:textSize="16sp" />

</FrameLayout>
```

3）Create a AdapterDelegate by `adapterDelegate` DSL ,and <br />override `onBind` method：

```kotlin
val simpleTextDelegate = adapterDelegate<SimpleTextModel>(R.layout.flap_item_simple_text) {
    onBind { model ->
        bindTextView(R.id.tv_content) {
            text = model.content
        }
    }
}
```

Use a FlapAdapter instead of Adapter and register AdapterDelegate to a FlapAdapter： :

```kotlin
//create FlapAdapter
var adapter: FlapAdapter = FlapAdapter()

//register AdapterDelegate
adapter.registerAdapterDelegate(simpleTextDelegate)

val dataList = ArrayList<Any>()
dataList.add(SimpleTextModel("Android"))
dataList.add(SimpleTextModel("Java"))
dataList.add(SimpleTextModel("Kotlin"))

//setData
adapter.setDataAndNotify(dataList);

recyclerView.adapter = adapter
```

Here we go!!

<div align=center><img width="360" height="640" src="assets/flap-simple-showcase.png"/></div>

## License
Apache 2.0