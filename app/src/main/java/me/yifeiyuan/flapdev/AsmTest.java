package me.yifeiyuan.flapdev;

import me.yifeiyuan.flap.Flap;
import me.yifeiyuan.flap.apt.proxies.SimpleImageComponentProxy;
import me.yifeiyuan.flap.apt.proxies.SimpleTextComponentProxy;

/**
 * Created by 程序亦非猿 on 2020/9/8.
 *
 * todo proxy 类的名字是  me/xxx/xxxProxy
 *
 *    L1
 *     LINENUMBER 16 L1
 *     ALOAD 2
 *     NEW me/yifeiyuan/flap/apt/proxies/SimpleTextComponentProxy
 *     DUP
 *     INVOKESPECIAL me/yifeiyuan/flap/apt/proxies/SimpleTextComponentProxy.<init> ()V
 *     INVOKEVIRTUAL me/yifeiyuan/flap/Flap.register (Lme/yifeiyuan/flap/internal/ComponentProxy;)Lme/yifeiyuan/flap/ComponentRegistry;
 *     POP
 *    L2
 *     LINENUMBER 18 L2
 *     ALOAD 2
 *     NEW me/yifeiyuan/flap/apt/proxies/SimpleImageComponentProxy
 *     DUP
 *     INVOKESPECIAL me/yifeiyuan/flap/apt/proxies/SimpleImageComponentProxy.<init> ()V
 *     INVOKEVIRTUAL me/yifeiyuan/flap/Flap.register (Lme/yifeiyuan/flap/internal/ComponentProxy;)Lme/yifeiyuan/flap/ComponentRegistry;
 *     POP
 *
 */
class AsmTest {

    private void foo(Flap flap){
        flap.register(new SimpleTextComponentProxy());
        flap.register(new SimpleImageComponentProxy());
    }
}
