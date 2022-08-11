package me.yifeiyuan.flap.skeleton

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by 程序亦非猿 on 2022/8/11.
 *
 * @since 3.0.1
 */
class SkeletonAdapter : RecyclerView.Adapter<SkeletonViewHolder>() {

    var skeletonItemCount: Int = 0

    @LayoutRes
    var skeletonLayoutRes: Int = -1

    var multiSkeletonLayoutRes: ((positon: Int) -> Int)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkeletonViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(viewType, parent, false)
        return SkeletonViewHolder(view)
    }

    override fun onBindViewHolder(holder: SkeletonViewHolder, position: Int) {}

    override fun getItemCount(): Int {
        return skeletonItemCount
    }

    override fun getItemViewType(position: Int): Int {
        if (multiSkeletonLayoutRes != null) {
            return multiSkeletonLayoutRes!!.invoke(position)
        }
        return skeletonLayoutRes
    }
}

class SkeletonViewHolder(view: View) : RecyclerView.ViewHolder(view)