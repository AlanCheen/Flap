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
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.debug_menu.*
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flap.decoration.LinearItemDecoration
import me.yifeiyuan.flap.decoration.LinearSpaceItemDecoration
import me.yifeiyuan.flap.decoration.SpaceItemDecoration
import me.yifeiyuan.flap.widget.*
import me.yifeiyuan.flapdev.*
import me.yifeiyuan.flapdev.components.SimpleTextModel
import java.util.*

private const val TAG = "BaseCaseFragment"

/**
 * Created by 程序亦非猿 on 2021/10/19.
 */
open class BaseTestcaseFragment : Fragment(), Scrollable, IMenuView {

    lateinit var recyclerView: RecyclerView

    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    lateinit var adapter: FlapAdapter

    lateinit var toast: Toast

    lateinit var emptyView: View

    lateinit var spaceItemDecoration: SpaceItemDecoration

    lateinit var linearItemDecoration: RecyclerView.ItemDecoration
    lateinit var gridItemDecoration: RecyclerView.ItemDecoration
    lateinit var currentItemDecoration: RecyclerView.ItemDecoration

    lateinit var linearLayoutManager: FlapLinearLayoutManager
    lateinit var gridLayoutManager: FlapGridLayoutManager
    lateinit var staggeredGridLayoutManager: FlapStaggeredGridLayoutManager
    lateinit var indexedStaggeredGridLayoutManager: FlapIndexedStaggeredGridLayoutManager
    lateinit var currentLayoutManager: RecyclerView.LayoutManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        toast = Toast.makeText(context.applicationContext, "", Toast.LENGTH_SHORT)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    open fun getLayoutId(): Int = R.layout.fragment_base_case

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
                .withLifecycleOwner(viewLifecycleOwner)
                .withEmptyView(emptyView)
                .observeEvent<String>("showToast") {
                    toast(it.arg ?: "Default Message")
                    Log.d("observeEvent", "showToast event ")
                }
                .observeEvent<Int>("intEvent") {
                    Log.d("observeEvent", "intEvent called")
                }
                .observerEvents {
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

        initLayoutManagers()
        initItemDecorations()

        //配置完结束最后再赋值
        recyclerView.adapter = adapter

        swipeRefreshLayout.isRefreshing = true

        Handler().postDelayed({
            adapter.setDataAndNotify(createRefreshData())
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
                    //spanSize = if (position % 2 == 0) 2 else 1
                    return spanSize
                }
            }
        }

        indexedStaggeredGridLayoutManager = FlapIndexedStaggeredGridLayoutManager(2, RecyclerView.VERTICAL).apply {
            gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        }

        staggeredGridLayoutManager = FlapStaggeredGridLayoutManager(2)

        currentLayoutManager = linearLayoutManager
    }

    open fun initItemDecorations() {
        val drawable = ContextCompat.getDrawable(requireActivity(), R.drawable.linear_item_decoration)

        //        linearItemDecoration = LinearItemDecoration(drawable!!, DividerItemDecoration.VERTICAL)

        linearItemDecoration = SpaceItemDecoration(requireActivity().toPixel(6))

        //        linearItemDecoration = LinearSpaceItemDecoration(requireActivity().toPixel(6))
        //                .withFirstItemTopEdge(false)
        //                .withLastItemBottomEdge(false)

        gridItemDecoration = SpaceItemDecoration(requireActivity().toPixel(6))

        spaceItemDecoration = SpaceItemDecoration(requireActivity().toPixel(6))

        currentItemDecoration = linearItemDecoration
    }

    open fun isClickEnable() = true

    open fun isLongClickEnable() = true

    open fun createAdapter() = FlapAdapter()

    open fun onRefresh() {
        swipeRefreshLayout.postDelayed({
            refreshData(20)
            swipeRefreshLayout.isRefreshing = false
        }, getRefreshDelayedTime())
    }

    open fun getRefreshDelayedTime() = 1000L

    open fun refreshData(size: Int = 20) {
        adapter.setDataAndNotify(createRefreshData(size))
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
            adapter.appendDataAndNotify(list)
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
                is FlapIndexedStaggeredGridLayoutManager -> {
                    layoutManager.scrollToPositionWithOffset(0, 0)
                }
                else -> {
                    layoutManager?.scrollToPosition(0)
                }
            }
        }
    }

    /**
     * 0 Linear
     * 1 Grid
     * 2 Staggered
     * 3 IndexedStaggered
     */
    fun switchLayoutManager(type: Int) {
        when (type) {
            0 -> {
                recyclerView.removeItemDecoration(currentItemDecoration)
                currentItemDecoration = linearItemDecoration
                recyclerView.addItemDecoration(currentItemDecoration)
                recyclerView.invalidateItemDecorations()

                currentLayoutManager = linearLayoutManager
                recyclerView.layoutManager = currentLayoutManager
            }
            1 -> {

                recyclerView.removeItemDecoration(currentItemDecoration)
                currentItemDecoration = gridItemDecoration
                recyclerView.addItemDecoration(currentItemDecoration)
                recyclerView.invalidateItemDecorations()

                currentLayoutManager = gridLayoutManager
                recyclerView.layoutManager = currentLayoutManager
            }
            2 -> {
                recyclerView.removeItemDecoration(currentItemDecoration)
                currentItemDecoration = gridItemDecoration
                recyclerView.addItemDecoration(currentItemDecoration)
                recyclerView.invalidateItemDecorations()

                currentLayoutManager = staggeredGridLayoutManager
                recyclerView.layoutManager = currentLayoutManager
            }
            3 -> {
                recyclerView.removeItemDecoration(currentItemDecoration)
                currentItemDecoration = gridItemDecoration
                recyclerView.addItemDecoration(currentItemDecoration)
                recyclerView.invalidateItemDecorations()

                currentLayoutManager = indexedStaggeredGridLayoutManager
                recyclerView.layoutManager = currentLayoutManager
            }
        }
    }

    fun updateOrientation(orientation: Int) {

        when (currentItemDecoration) {
            is LinearItemDecoration -> {
                (currentItemDecoration as LinearItemDecoration).orientation = orientation
            }
            is LinearSpaceItemDecoration -> {
                (currentItemDecoration as LinearSpaceItemDecoration).orientation = orientation
            }
            is SpaceItemDecoration -> {
                // do nothing
            }
        }

        when (currentLayoutManager) {
            is LinearLayoutManager -> {
                (currentLayoutManager as LinearLayoutManager).orientation = orientation
            }
            is GridLayoutManager -> {
                (currentLayoutManager as GridLayoutManager).orientation = orientation
            }
            is StaggeredGridLayoutManager -> {
                (currentLayoutManager as StaggeredGridLayoutManager).orientation = orientation
            }
            is FlapIndexedStaggeredGridLayoutManager -> {
                (currentLayoutManager as FlapIndexedStaggeredGridLayoutManager).orientation = orientation
            }
        }

        recyclerView.invalidateItemDecorations()
    }

    var configMenuView: ConfigMenuView? = null
    var menuDialog: BottomSheetDialog? = null
    override fun showMenu() {
        if (menuDialog == null) {
            configMenuView = ConfigMenuView(requireActivity())
            configMenuView?.callback = object : ConfigMenuView.Callback {
                override fun onOrientationChanged(orientation: Int) {
                    updateOrientation(orientation)
                }

                override fun onLayoutManagerChanged(type: Int) {
                    switchLayoutManager(type)
                }

                override fun onPreloadChanged(direction: Int, enable: Boolean) {
                }
            }
            menuDialog = BottomSheetDialog(requireActivity()).apply {
                setContentView(configMenuView!!)
            }
        }
        menuDialog?.show()
    }
}