package me.yifeiyuan.flapdev.testcases

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import jp.wasabeef.recyclerview.animators.ScaleInAnimator
import me.yifeiyuan.flap.animation.*
import me.yifeiyuan.flapdev.R
import me.yifeiyuan.flapdev.components.SimpleTextModel

/**
 * 测试动画功能
 *
 * 更推荐使用 https://github.com/wasabeef/recyclerview-animators
 * 除了 minSdk 指定 21 有点高外。
 *
 * Created by 程序亦非猿 on 2022/8/29.
 */
class AnimationTestcase : BaseTestcaseFragment() {

   private val alphaInAdapterAnimation = AlphaInAdapterAnimation()
   private val scaleInAdapterAnimation = ScaleInAdapterAnimation()
   private val slideInRightAdapterAnimation = SlideInRightAdapterAnimation()
   private val slideInLeftAdapterAnimation = SlideInLeftAdapterAnimation()
   private val slideInBottomAdapterAnimation = SlideInBottomAdapterAnimation()

    lateinit var currentAdapterAnimation: BaseAdapterAnimation

    override fun onInit(view: View) {
        super.onInit(view)

        setHasOptionsMenu(true)

        adapter.doOnPreload {
            loadMoreData(20)
        }

        currentAdapterAnimation = slideInBottomAdapterAnimation
        currentAdapterAnimation.attachToAdapter(adapter)

        recyclerView.addItemDecoration(spaceItemDecoration)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.animation, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.resetData -> {
                resetData()
            }
            R.id.slideInLeft -> {
                resetAdapterAnimation()
                currentAdapterAnimation = slideInLeftAdapterAnimation
                currentAdapterAnimation.attachToAdapter(adapter)
            }
            R.id.slideInRight -> {
                resetAdapterAnimation()
                currentAdapterAnimation = slideInRightAdapterAnimation
                currentAdapterAnimation.attachToAdapter(adapter)
            }
            R.id.scaleIn -> {
                resetAdapterAnimation()
                currentAdapterAnimation = scaleInAdapterAnimation
                currentAdapterAnimation.attachToAdapter(adapter)
            }
            R.id.alphaIn -> {
                resetAdapterAnimation()
                currentAdapterAnimation = alphaInAdapterAnimation
                currentAdapterAnimation.attachToAdapter(adapter)
            }
            R.id.slideInBottom -> {
                resetAdapterAnimation()
                currentAdapterAnimation = slideInBottomAdapterAnimation
                currentAdapterAnimation.attachToAdapter(adapter)
            }

            R.id.animators -> {
                resetAdapterAnimation()
                recyclerView.itemAnimator = ScaleInAnimator()
            }
            R.id.addOne -> {
                val newOne = SimpleTextModel("New one")
                adapter.insertDataAt(1, newOne)
            }
            R.id.removeOne -> {
                adapter.removeDataAt(1)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun resetData() {
        adapter.setDataAndNotify(createRefreshData())
        currentAdapterAnimation.reset()
    }

    private fun resetAdapterAnimation() {
        currentAdapterAnimation.let {
            it.reset()
            adapter.unregisterAdapterHook(it)
        }
        recyclerView.itemAnimator = null
    }
}