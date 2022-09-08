package me.yifeiyuan.flap

import me.yifeiyuan.flap.delegate.AdapterDelegateManager
import me.yifeiyuan.flap.delegate.IAdapterDelegateManager
import me.yifeiyuan.flap.hook.AdapterHookManager
import me.yifeiyuan.flap.hook.IAdapterHookManager
import me.yifeiyuan.flap.service.AdapterServiceManager
import me.yifeiyuan.flap.service.IAdapterServiceManager

/**
 * Created by 程序亦非猿 on 2022/9/8.
 */
class FlapManager : IAdapterHookManager by AdapterHookManager(), IAdapterDelegateManager by AdapterDelegateManager(), IAdapterServiceManager by AdapterServiceManager(), IFlapManager {


}