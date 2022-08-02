package me.yifeiyuan.flap.differ

/**
 *
 * 实现 IDiffer 接口，可以高效刷新
 *
 * @see FlapDifferAdapter
 * @see androidx.recyclerview.widget.AsyncListDiffer
 *
 * Created by 程序亦非猿 on 2021/9/22.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 2020/9/22
 * @since 3.0.0
 */
interface IDiffer {

    /**
     * 是否是同一个 item，如果有 id 之类的属性，就比较它们是否相等
     * this.id == newItem.id
     *
     * 当返回 true，则会继续调用 areContentsTheSame
     *
     * @see androidx.recyclerview.widget.DiffUtil.ItemCallback.areItemsTheSame
     */
    fun areItemsTheSame(newItem: Any): Boolean

    /**
     * 用于判断两个数据的内容是否相同
     *
     * 当 areItemsTheSame 返回 true 的时候会调用这个方法
     *
     * @see androidx.recyclerview.widget.DiffUtil.ItemCallback.areContentsTheSame
     */
    fun areContentsTheSame(newItem: Any): Boolean

    /**
     * 当 areItemsTheSame 返回 true , 并且 areContentsTheSame 返回 false, 就会调用该方法
     *
     * @see androidx.recyclerview.widget.DiffUtil.ItemCallback.getChangePayload
     *
     * @return 一般返回 areContentsTheSame 中不相同的数据，例如 newItem.xxx
     */
    fun getChangePayload(newItem: Any): Any? = null
}