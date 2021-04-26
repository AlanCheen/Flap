package me.yifeiyuan.flapdev;

import me.yifeiyuan.flap.Flap;
import me.yifeiyuan.flap.apt.proxies.SimpleImageComponentProxy;
import me.yifeiyuan.flap.apt.proxies.SimpleTextComponentProxy;

/**
 * Created by 程序亦非猿 on 2020/9/8.
 */
class AsmTest {

    private void foo(Flap flap){
        flap.register(new SimpleTextComponentProxy());
        flap.register(new SimpleImageComponentProxy());
    }
}
