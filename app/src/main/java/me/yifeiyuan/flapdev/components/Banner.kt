package me.yifeiyuan.flapdev.components

import android.widget.ImageView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flap.dsl.adapterDelegate
import me.yifeiyuan.flapdev.FlapApplication.Companion.toast
import me.yifeiyuan.flapdev.R

/**
 * Created by 程序亦非猿 on 2022/9/11.
 */

class BannerModel {
    var images: MutableList<BannerImageModel>? = null
}

class BannerImageModel {
    var url: String? = null
    var resId: Int = 0
}

fun createBannerAdapterDelegate() = adapterDelegate<BannerModel>(R.layout.component_banner) {

    swipeEnable = false
    dragEnable = false

    val viewPager2 = findViewById<ViewPager2>(R.id.banner)

    val bannerAdapter = FlapAdapter().apply {
        registerAdapterDelegate(bannerImageDelegate())
    }

    viewPager2.adapter = bannerAdapter

    onBind { model, position, payloads, adapter ->
        model.images?.let {
            bannerAdapter.setDataAndNotify(it)
        }
    }

    bannerAdapter.doOnItemClick { recyclerView, childView, position ->
        toast("点击 banner position=$position")
    }
}

fun bannerImageDelegate() = adapterDelegate<BannerImageModel>(R.layout.component_banner_image) {

    val imageView = findViewById<ImageView>(R.id.logo)

    onBind { model ->
        if (model.url?.isNotEmpty() == true) {
            Glide.with(context).load(model.url).into(imageView)
        } else if (model.resId > 0) {
            Glide.with(context).load(model.resId).into(imageView)
        }
    }
}