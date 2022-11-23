package me.yifeiyuan.flap

import me.yifeiyuan.flap.delegate.IAdapterDelegateManager
import me.yifeiyuan.flap.hook.IAdapterHookManager
import me.yifeiyuan.flap.service.IAdapterServiceManager

/**
 * Created by 程序亦非猿 on 2022/11/23.
 */
interface Registry : IAdapterHookManager, IAdapterDelegateManager, IAdapterServiceManager {

}