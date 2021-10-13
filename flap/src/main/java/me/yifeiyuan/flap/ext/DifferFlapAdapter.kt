package me.yifeiyuan.flap.ext

import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import me.yifeiyuan.flap.FlapAdapter

/**
 * DifferFlapAdapter supports AsyncListDiffer feature.
 *
 * Created by 程序亦非猿 on 2021/9/22.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 2020/9/22
 * @since 3.0
 */
open class DifferFlapAdapter<T> : FlapAdapter {

    private val differ: AsyncListDiffer<T>

    constructor(itemCallback: DiffUtil.ItemCallback<T>) {
        differ = AsyncListDiffer(this, itemCallback)
    }

    constructor(config: AsyncDifferConfig<T>) {
        differ = AsyncListDiffer(AdapterListUpdateCallback(this), config)
    }

    @JvmOverloads
    fun submitList(data: List<T>, callback: Runnable? = null) {
        differ.submitList(data, callback)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun getItemData(position: Int): Any {
        return differ.currentList[position] as Any
    }

    override fun setData(list: MutableList<Any>, notifyDataSetChanged: Boolean) {
        throw UnsupportedOperationException("不支持，请使用 submitList")
    }
}