package me.yifeiyuan.flap.ext

import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by 程序亦非猿 on 2021/9/29.
 */

//todo 是否需要缓存 提提速？findViewById 真的很耗时吗？
//val RecyclerView.ViewHolder.cachedViews: SparseArray<View>
//    get() = SparseArray(16)
//
//fun <V : View> RecyclerView.ViewHolder.bindView(viewId: Int, binder: V.() -> Unit): RecyclerView.ViewHolder {
//
//    var view = this.cachedViews[viewId]
//
//    if (view == null) {
//        view = itemView.findViewById<V>(viewId)
//        this.cachedViews.put(viewId, view)
//    }
//
//    (view as V).binder()
//    return this
//}

fun <V : View> RecyclerView.ViewHolder.bindView(viewId: Int, binder: V.() -> Unit): RecyclerView.ViewHolder {
    val view = itemView.findViewById<V>(viewId)
    view.binder()
    return this
}

fun <T : RecyclerView.ViewHolder> T.bindTextView(viewId: Int, binder: TextView.() -> Unit): T {
    val textView = itemView.findViewById<TextView>(viewId)
    textView.binder()
    return this
}

fun <T : RecyclerView.ViewHolder> T.bindButton(viewId: Int, binder: Button.() -> Unit): T {
    val textView = itemView.findViewById<Button>(viewId)
    textView.binder()
    return this
}

fun <T : RecyclerView.ViewHolder> T.bindImageView(viewId: Int, binder: ImageView.() -> Unit): T {
    val view = itemView.findViewById<ImageView>(viewId)
    view.binder()
    return this
}

fun <T : RecyclerView.ViewHolder> T.bindEditText(viewId: Int, binder: EditText.() -> Unit): T {
    val view = itemView.findViewById<EditText>(viewId)
    view.binder()
    return this
}

fun <T : RecyclerView.ViewHolder> T.bindCheckBox(viewId: Int, binder: CheckBox.() -> Unit): T {
    val view = itemView.findViewById<CheckBox>(viewId)
    view.binder()
    return this
}

fun <T : RecyclerView.ViewHolder> T.bindRecyclerView(viewId: Int, binder: RecyclerView.() -> Unit): T {
    val view = itemView.findViewById<RecyclerView>(viewId)
    view.binder()
    return this
}

