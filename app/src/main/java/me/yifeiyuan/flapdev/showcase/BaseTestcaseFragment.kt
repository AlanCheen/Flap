package me.yifeiyuan.flapdev.showcase

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
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
import me.yifeiyuan.flap.ext.ExtraParamsProvider
import me.yifeiyuan.flap.widget.FlapRecyclerView
import me.yifeiyuan.flapdev.R
import me.yifeiyuan.flapdev.Scrollable
import me.yifeiyuan.flapdev.components.SimpleTextModel
import java.util.ArrayList

private const val TAG = "BaseCaseFragment"

/**
 * Created by 程序亦非猿 on 2021/10/19.
 */
open class BaseTestcaseFragment : Fragment(), Scrollable {

    lateinit var recyclerView: RecyclerView
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    lateinit var adapter: FlapAdapter

    lateinit var toast: Toast

    lateinit var emptyView:View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        toast = Toast.makeText(context.applicationContext, "", Toast.LENGTH_SHORT)
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
        emptyView = view.findViewById(R.id.emptyView)
        recyclerView = view.findViewById(R.id.recyclerView)
        adapter = createAdapter()

        adapter.setEmptyView(emptyView)

        adapter.observeEvent<String>("showToast") {
            toast(it.arg ?: "Default Message")
            Log.d("observeEvent", "showToast event ")
        }

        adapter.observeEvent<Int>("intEvent") {
            Log.d("observeEvent", "intEvent called")
        }

        adapter.observerEvents {
            when (it.eventName) {
                "showToast" -> {
                    Log.d("observerEvents", "showToast ~~ ${it.arg}")
                    it.setEventResult(isSuccess = true)
                }
                "intEvent" -> {
                    Log.d("observerEvents", "intEvent ~~ ${it.arg}")
                }
            }
        }

        adapter.paramProvider = object : ExtraParamsProvider {
            override fun getParam(key: String): Any? {
                return when (key) {
                    "intValue" -> {
                        233
                    }
                    "stringValue" -> {
                        "这是一个 stringValue"
                    }
                    "booleanValue" -> {
                        true
                    }
                    else -> "Unknown Key"
                }
            }
        }

        adapter.doOnItemClick { recyclerView, childView, position ->
            Log.d(TAG, "doOnItemClick called with: childView = $childView, position = $position")
            val component = recyclerView.getChildViewHolder(childView)
            toast("点击了 position = $position，model=${adapter.getItemData(position)}")
        }

        adapter.doOnItemLongClick { recyclerView, childView, position ->
            Log.d(TAG, "doOnItemLongClick called with: childView = $childView, position = $position")
            toast("长按了 position = $position")
            true
        }

        //配置完结束最后在赋值
        recyclerView.adapter = adapter
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
        toast.setText(title)
        toast.duration = Toast.LENGTH_SHORT
        toast.show()
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