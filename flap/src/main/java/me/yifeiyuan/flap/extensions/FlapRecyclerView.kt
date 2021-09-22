package me.yifeiyuan.flap.extensions

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flap.FlapAdapter

/**
 * 封装了 Flap 的 RecyclerView，未测试，暂时请不要使用。
 * todo
 *
 * Created by 程序亦非猿 on 2021/9/22.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 2020/9/22
 * @since 3.0
 */
class FlapRecyclerView : RecyclerView {

    private var adapter: FlapAdapter? = null

    constructor(context: Context) : super(context) {
        init(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        init(context, attrs, defStyle)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyle: Int) {
        adapter = FlapAdapter()
        setAdapter(adapter)
    }

    fun setData(data: MutableList<Any>) {
        adapter!!.setData(data)
    }
}