package me.yifeiyuan.flap

/**
 *
 * AdapterDelegateNotFoundException will be thrown when there's no AdapterDelegate for a model.
 *
 * Created by 程序亦非猿 on 2021/9/22.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 2020/9/22
 * @since 3.0.0
 */
internal class AdapterDelegateNotFoundException(itemData: Any) : Exception(itemData.toString()) {

}