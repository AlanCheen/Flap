package me.yifeiyuan.flapdev.testcases

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flap.decoration.GridSpaceItemDecoration
import me.yifeiyuan.flap.decoration.LinearItemDecoration
import me.yifeiyuan.flap.widget.FlapGridLayoutManager
import me.yifeiyuan.flap.widget.FlapLinearLayoutManager
import me.yifeiyuan.flap.widget.FlapRecyclerView
import me.yifeiyuan.flap.widget.FlapStaggeredGridLayoutManager
import me.yifeiyuan.flapdev.TestService
import me.yifeiyuan.flapdev.R
import me.yifeiyuan.flapdev.Scrollable
import me.yifeiyuan.flapdev.components.SimpleTextModel
import me.yifeiyuan.flapdev.toPixel
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

    lateinit var emptyView: View

    lateinit var linearItemDecoration: RecyclerView.ItemDecoration
    lateinit var gridItemDecoration: RecyclerView.ItemDecoration
    lateinit var currentItemDecoration: RecyclerView.ItemDecoration

    lateinit var linearLayoutManager: FlapLinearLayoutManager
    lateinit var gridLayoutManager: FlapGridLayoutManager
    lateinit var staggeredGridLayoutManager: FlapStaggeredGridLayoutManager
    lateinit var currentLayoutManager: RecyclerView.LayoutManager

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
        adapter.setLifecycleOwner(viewLifecycleOwner)

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

        adapter.setParamProvider {
            when (it) {
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

        if (isClickEnable()) {
            adapter.doOnItemClick { recyclerView, childView, position ->
                Log.d(TAG, "doOnItemClick called with: childView = $childView, position = $position")
                val component = recyclerView.getChildViewHolder(childView)
                toast("点击了 position = $position，model=${adapter.getItemData(position)}")
            }
        }

        if (isLongClickEnable()) {
            adapter.doOnItemLongClick { recyclerView, childView, position ->
                Log.d(TAG, "doOnItemLongClick called with: childView = $childView, position = $position")
                toast("长按了 position = $position")
                true
            }
        }

        adapter.registerAdapterService(TestService::class.java)
        adapter.registerAdapterService("LogService", TestService::class.java)

        initLayoutManagers()
        initItemDecorations()

        //配置完结束最后再赋值
        recyclerView.adapter = adapter

        swipeRefreshLayout.isRefreshing = true

        Handler().postDelayed({
            adapter.setData(createRefreshData())
            swipeRefreshLayout.isRefreshing = false
        }, getRefreshDelayedTime())
    }

    open fun initLayoutManagers() {
        linearLayoutManager = FlapLinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)

        val spanCount = 3
        gridLayoutManager = FlapGridLayoutManager(requireActivity(), spanCount, RecyclerView.VERTICAL, false).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    var spanSize = 1
                    //                            spanSize = if (position % 2 == 0) 2 else 1
                    return spanSize
                }
            }
        }

        staggeredGridLayoutManager = FlapStaggeredGridLayoutManager(3).apply {
            gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        }
    }

    open fun initItemDecorations() {
        val drawable = ContextCompat.getDrawable(requireActivity(), R.drawable.linear_item_decoration)

        //--1
        //        linearItemDecoration = LinearItemDecoration(drawable!!, DividerItemDecoration.VERTICAL)
        //--1

        //--2
        //        linearItemDecoration = LinearItemDecoration(requireActivity().toPixel(6), Color.BLUE)
        //                .withFirstItemTopEdge(true)
        //                .withLastItemBottomEdge(true)
        //--2

        //--3
        //        linearItemDecoration = LinearItemDecoration(resources.getDimensionPixelSize(R.dimen.item_decoration_size), Color.parseColor("#ff0000"))
        linearItemDecoration = LinearItemDecoration(resources.getDimensionPixelSize(R.dimen.item_decoration_size))
        //--3

        //        linearItemDecoration = LinearSpaceItemDecoration(requireActivity().toPixel(6))
        //                .withFirstItemTopEdge(false)
        //                .withLastItemBottomEdge(false)

        //        gridItemDecoration = GridItemDecoration(drawable!!)

        gridItemDecoration = GridSpaceItemDecoration(requireActivity().toPixel(12))
                .withFirstRowEdge(true)
    }

    open fun isClickEnable() = true

    open fun isLongClickEnable() = true

    open fun createAdapter() = FlapAdapter()

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

    open fun createRefreshData(size: Int = 30): MutableList<Any> {
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