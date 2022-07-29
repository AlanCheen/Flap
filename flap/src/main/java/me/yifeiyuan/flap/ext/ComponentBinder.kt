package me.yifeiyuan.flap.ext

import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flap.Component

/**
 * 如果不想写 Component.findViewById 那么也可以用 Component 的 bindXXXView 来做绑定。
 *
 * 只是挑选了一些常用的基础组件提供 binder 方法，目前包含：
 *
 * @see bindView
 * @see bindTextView
 * @see bindImageView
 * @see bindEditText
 * @see bindButton
 * @see bindCheckBox
 * @see bindRecyclerView
 * @see bindImageButton
 * @see bindProgressBar
 * @see bindRatingBar
 *
 * Created by 程序亦非猿 on 2021/9/29.
 * @since 3.0.0
 */

fun <V : View> Component<*>.bindView(viewId: Int, binder: V.() -> Unit) {
    val view = itemView.findViewById<V>(viewId)
    view.binder()
}

fun Component<*>.bindTextView(viewId: Int, binder: TextView.() -> Unit) {
    val view = itemView.findViewById<TextView>(viewId)
    view.binder()
}

fun Component<*>.bindButton(viewId: Int, binder: Button.() -> Unit) {
    val textView = itemView.findViewById<Button>(viewId)
    textView.binder()
}

fun Component<*>.bindImageView(viewId: Int, binder: ImageView.() -> Unit) {
    val view = itemView.findViewById<ImageView>(viewId)
    view.binder()
}

fun Component<*>.bindImageButton(viewId: Int, binder: ImageButton.() -> Unit) {
    val view = itemView.findViewById<ImageButton>(viewId)
    view.binder()
}

fun Component<*>.bindEditText(viewId: Int, binder: EditText.() -> Unit) {
    val view = itemView.findViewById<EditText>(viewId)
    view.binder()
}

fun Component<*>.bindCheckBox(viewId: Int, binder: CheckBox.() -> Unit) {
    val view = itemView.findViewById<CheckBox>(viewId)
    view.binder()
}

fun Component<*>.bindProgressBar(viewId: Int, binder: ProgressBar.() -> Unit) {
    val view = itemView.findViewById<ProgressBar>(viewId)
    view.binder()
}

fun Component<*>.bindRatingBar(viewId: Int, binder: RatingBar.() -> Unit) {
    val view = itemView.findViewById<RatingBar>(viewId)
    view.binder()
}

fun Component<*>.bindRecyclerView(viewId: Int, binder: RecyclerView.() -> Unit) {
    val view = itemView.findViewById<RecyclerView>(viewId)
    view.binder()
}