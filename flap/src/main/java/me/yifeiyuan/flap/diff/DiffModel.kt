package me.yifeiyuan.flap.diff

/**
 * todo
 * Created by 程序亦非猿 on 2021/9/22.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 2020/9/22
 * @since 3.0
 */
interface DiffModel {

    //this== newItem
    fun areItemsTheSame(other: DiffModel): Boolean

    //hashCode() == newItem.hashCode()
    fun areContentsTheSame(other: DiffModel): Boolean

    fun getChangePayload(newItem: DiffModel): Any?
}