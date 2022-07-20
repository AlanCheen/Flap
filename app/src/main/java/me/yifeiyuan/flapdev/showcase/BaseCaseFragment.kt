package me.yifeiyuan.flapdev.showcase

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flap.ext.Event
import me.yifeiyuan.flap.ext.EventObserver
import me.yifeiyuan.flap.widget.FlapRecyclerView
import me.yifeiyuan.flapdev.R
import me.yifeiyuan.flapdev.Scrollable
import me.yifeiyuan.flapdev.components.simpletext.SimpleTextModel
import java.util.ArrayList

/**
 * Created by 程序亦非猿 on 2021/10/19.
 */
open class BaseCaseFragment : Fragment(), Scrollable {

    lateinit var recyclerView: RecyclerView
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    lateinit var adapter: FlapAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setColorSchemeColors(resources.getColor(R.color.colorPrimary))
        swipeRefreshLayout.setOnRefreshListener {
            onRefresh()
        }

        onInit(view)
    }

    open fun onInit(view: View) {
        recyclerView = view.findViewById(R.id.recyclerView)
        adapter = createAdapter()
        recyclerView.adapter = adapter

        adapter.observeEvent("showToast",object :EventObserver{
            override fun onEvent(event: Event<*>) {
                toast(event.arg?.toString()?:"default toast message")
                event.onSuccess?.invoke()
            }
        })
    }

    open fun createAdapter() = FlapAdapter().apply {
        setData(createRefreshData())
    }

    open fun getLayoutId(): Int = R.layout.fragment_base_case

    open fun onRefresh() {
        swipeRefreshLayout.postDelayed({
            refreshData(20)
            swipeRefreshLayout.isRefreshing = false
        }, getRefreshDelayedTime())
    }

    open fun getRefreshDelayedTime() = 1000L

    open fun refreshData(size: Int = 20) {
        adapter.setData(createRefreshData(size))
    }

    open fun createRefreshData(size: Int = 20): MutableList<Any> {
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

    override fun scrollToBottom() {
        TODO("Not yet implemented")
    }

    override fun scrollToTop() {
        if (recyclerView is FlapRecyclerView) {
            (recyclerView as FlapRecyclerView).scrollToTop()
        } else {
            when (val layoutManager = recyclerView.layoutManager) {
                is LinearLayoutManager -> {
                    layoutManager.scrollToPositionWithOffset(0, 0)
                }
                is StaggeredGridLayoutManager -> {
                    layoutManager.scrollToPositionWithOffset(0, 0)
                }
                else -> {
                    layoutManager?.scrollToPosition(0)
                }
            }
        }
    }
}