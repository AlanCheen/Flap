package me.yifeiyuan.flap.differ

/**
 * todo
 * Created by 程序亦非猿 on 2021/9/22.
 *
 * @see FlapDifferAdapter
 * @see androidx.recyclerview.widget.AsyncListDiffer
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
     * @see androidx.recyclerview.widget.DiffUtil.ItemCallback.areItemsTheSame
     */
    fun areItemsTheSame(newItem: Any): Boolean {
        return this.javaClass == newItem.javaClass
    }

    /**
     *
     * equals
     * 当 areItemsTheSame 返回 true 的时候会调用这个方法
     *
     * @see androidx.recyclerview.widget.DiffUtil.ItemCallback.areContentsTheSame
     */
    fun areContentsTheSame(newItem: Any): Boolean {
        return equals(newItem)
    }

    /**
     * @see androidx.recyclerview.widget.DiffUtil.ItemCallback.getChangePayload
     */
    fun getChangePayload(newItem: Any): Any? = null
}