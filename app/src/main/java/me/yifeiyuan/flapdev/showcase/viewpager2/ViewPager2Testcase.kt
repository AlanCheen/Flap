package me.yifeiyuan.flapdev.showcase.viewpager2

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flap.delegate.AdapterDelegate
import me.yifeiyuan.flap.hook.AdapterHook
import me.yifeiyuan.flapdev.R
import me.yifeiyuan.flapdev.Scrollable
import me.yifeiyuan.flapdev.components.simpletext.SimpleTextModel
import java.util.*

/**
 *
 * 测试 ViewPager2 适用性
 * FlapAdapter 测试通过
 * FragmentStateAdapter 不需要测试
 */
class ViewPager2Testcase : Fragment() , Scrollable {

    lateinit var viewPager: ViewPager2

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_view_pager2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager = view.findViewById(R.id.viewPager)

        setHasOptionsMenu(true)

        val size = 5
        val list = ArrayList<Any>()
        repeat(size) {
            list.add(SimpleTextModel("初始数据 $it of $size"))
        }

        viewPager.adapter = FlapAdapter().apply {
            setData(list)

            /**
             * ViewPager2 要求 ViewHolder.itemView 必须是 match_parent 的，这里强制改一下
             * java.lang.IllegalStateException: Pages must fill the whole ViewPager2 (use match_parent)
             * at androidx.viewpager2.widget.ViewPager2$4.onChildViewAttachedToWindow(ViewPager2.java:270)
             */
            registerAdapterHook(object : AdapterHook {
                override fun onCreateViewHolderEnd(adapter: FlapAdapter, delegate: AdapterDelegate<*, *>?, viewType: Int, component: Component<*>) {
                    component.itemView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.viewpager2, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.horizontal->{
                viewPager.orientation=ViewPager2.ORIENTATION_HORIZONTAL
            }
            R.id.vertical->{
                viewPager.orientation=ViewPager2.ORIENTATION_VERTICAL
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun scrollToTop() {
        viewPager.setCurrentItem(0,true)
    }

    override fun scrollToBottom() {
        viewPager.setCurrentItem(viewPager.adapter!!.itemCount-1,true)
    }
}