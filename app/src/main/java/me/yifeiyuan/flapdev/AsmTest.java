package me.yifeiyuan.flapdev;

import me.yifeiyuan.flap.Flap;
import me.yifeiyuan.flapdev.components.simpletext.SimpleTextComponentDelegate;

/**
 * Created by 程序亦非猿 on 2020/9/8.
 */
class AsmTest {

    private void foo(Flap flap){
        flap.registerAdapterDelegate(new SimpleTextComponentDelegate());
    }
}
