package me.yifeiyuan.flapdev.showcase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flapdev.R
import me.yifeiyuan.flapdev.mockData
import me.yifeiyuan.flapdev.mockModels

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
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.postDelayed({
                onRefresh()
                swipeRefreshLayout.isRefreshing = false
            }, 2000)
        }
        adapter = createAdapter()
        recyclerView.adapter = adapter
    }

    open fun createAdapter() = FlapAdapter().apply {
        mockData()
    }

    open fun getLayoutId(): Int = R.layout.fragment_base

    open fun onRefresh() {
        adapter.mockData()
    }

    fun toast(title: String) {
        Toast.makeText(requireContext(),title,Toast.LENGTH_SHORT).show()
    }
}