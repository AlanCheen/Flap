package me.yifeiyuan.flap.ext

import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import me.yifeiyuan.flap.FlapAdapter
import java.util.*

/**
 * FlapDiffAdapter supports AsyncListDiffer feature.
 *
 * Created by 程序亦非猿 on 2021/9/22.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 2020/9/22
 * @since 3.0
 */
class FlapDiffAdapter<T : DiffModel> : FlapAdapter {

    private val differ: AsyncListDiffer<T>

    constructor() {
        differ = AsyncListDiffer(this, object : ItemCallback<T>() {
            override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
                return oldItem.areItemsTheSame(newItem)
            }

            override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
                return oldItem.areContentsTheSame(newItem)
            }

            override fun getChangePayload(oldItem: T, newItem: T): Any? {
                return oldItem.getChangePayload(newItem)
            }
        })
    }

    constructor(itemCallback: ItemCallback<T>) {
        differ = AsyncListDiffer(this, itemCallback)
    }

    constructor(config: AsyncDifferConfig<T>) {
        differ = AsyncListDiffer(AdapterListUpdateCallback(this), config)
    }

    @JvmOverloads
    fun submitList(newList: List<T>, callback: Runnable? = null) {
        if (differ.currentList === newList) {
            val data: ArrayList<T> = ArrayList<T>(newList)
            differ.submitList(data, callback)
        } else {
            differ.submitList(newList, callback)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun getItemData(position: Int): Any {
        return differ.currentList[position] as Any
    }

    override fun setData(newDataList: MutableList<Any>) {
        val data = ArrayList<T>()
        for (o in newDataList) {
            data.add(o as T)
        }
        submitList(data)
    }
}