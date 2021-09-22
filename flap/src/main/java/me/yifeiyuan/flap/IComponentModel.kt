package me.yifeiyuan.flap

/**
 * todo
 * Created by 程序亦非猿 on 2021/9/22.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 2020/9/22
 * @since 3.0
 */
interface IComponentModel {
    fun areItemsTheSame(other: IComponentModel): Boolean
    fun areContentsTheSame(other: IComponentModel): Boolean
    fun getChangePayload(newItem: IComponentModel): Any?
}