package me.yifeiyuan.flap

import androidx.annotation.RestrictTo
import me.yifeiyuan.flap.delegate.IAdapterDelegateManager
import me.yifeiyuan.flap.hook.IAdapterHookManager
import me.yifeiyuan.flap.service.IAdapterServiceManager

/**
 * 统一注册中心接口抽象
 *
 * Created by 程序亦非猿 on 2022/11/23.
 * @since 3.3.0
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
interface FlapRegistry : IAdapterHookManager, IAdapterDelegateManager, IAdapterServiceManager {

}