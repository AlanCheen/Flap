package me.yifeiyuan.flap.exceptions

/**
 * An DelegateNotFoundException will be thrown when trying to create a FlapItem when there is no matching AdapterDelegate.
 *
 * Created by 程序亦非猿 on 2021/9/22.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 2020/9/22
 * @since 3.0
 */
class DelegateNotFoundException(itemData: Any) : Exception(itemData.toString()) {

}