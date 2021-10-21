package me.yifeiyuan.flapdev.showcase

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flapdev.R
import me.yifeiyuan.flapdev.components.simpletext.SimpleTextModel
import me.yifeiyuan.flapdev.mockData
import java.util.ArrayList

/**
 * Created by 程序亦非猿 on 2021/10/19.
 */
open class BaseFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    lateinit var adapter: FlapAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerView)

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setColorSchemeColors(resources.getColor(R.color.colorPrimary))
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.postDelayed({
                refreshData(20)
                swipeRefreshLayout.isRefreshing = false
            }, getRefreshDelayedTime())
        }
        adapter = createAdapter()
        recyclerView.adapter = adapter
    }

    open fun createAdapter() = FlapAdapter().apply {
        setData(createRefreshData())
    }

    open fun getLayoutId(): Int = R.layout.fragment_base

    open fun onRefresh() {
        adapter.mockData()
    }

    open fun getRefreshDelayedTime() = 1000L

    open fun refreshData(size: Int = 20) {
        adapter.setData(createRefreshData(size))
    }

    open fun createRefreshData(size: Int = 20):MutableList<Any>{
        val list = ArrayList<Any>()
        repeat(size) {
            list.add(SimpleTextModel("初始数据 $it of $size"))
        }
        return list
    }

    open fun loadMoreData(size: Int = 10) {
        Handler().postDelayed({
            val list = ArrayList<Any>()
            repeat(size) {
                list.add(SimpleTextModel("加载更多数据 $it of $size"))
            }
            adapter.appendData(list)
        }, 500)
    }

    fun toast(title: String) {
        Toast.makeText(requireContext(), title, Toast.LENGTH_SHORT).show()
    }
}