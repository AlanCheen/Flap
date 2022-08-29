package me.yifeiyuan.flapdev.testcases

import android.view.View
import me.yifeiyuan.flap.animation.SlideInRightAdapterAnimation

/**
 * Created by 程序亦非猿 on 2022/8/29.
 */
class AnimationTestcase : BaseTestcaseFragment() {


    override fun onInit(view: View) {
        super.onInit(view)

        SlideInRightAdapterAnimation().attachToAdapter(adapter)
    }
}