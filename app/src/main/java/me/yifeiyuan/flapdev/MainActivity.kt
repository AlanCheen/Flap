package me.yifeiyuan.flapdev

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import me.yifeiyuan.flapdev.showcase.*
import me.yifeiyuan.flapdev.showcase.selection.SlideshowFragment
import me.yifeiyuan.flapdev.showcase.viewpager2.ViewPager2Testcase

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_app_bar_open_drawer_description, R.string.navigation_drawer_close)
        drawerLayout.setDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener { it ->

            var title = "Flap"
            var subtitle = ""
            when (it.itemId) {
                R.id.nav_multi_type -> {
                    title = "多类型"
                    subtitle = "多 itemViewType"
                    replace(MultiTypeTestcase::class.java)
                }
                R.id.nav_prefetch -> {
                    title = "预加载"
                    subtitle = "在滑动到底部之前预先加载"
                    replace(PrefetchTestcase::class.java)
                }
                R.id.nav_component_pool -> {
                    title = "测试 FlapComponentPool"
                    subtitle = "缓存功能测试"
                    replace(FlapComponentPoolTestcase::class.java)
                }
                R.id.nav_flap_rv -> {
                    title = "FlapRecyclerView"
                    subtitle = "自定义 RecyclerView"
                    replace(FlapRecyclerViewTestcase::class.java)
                }
                R.id.nav_viewpager2 -> {
                    title = "ViewPager2"
                    subtitle = "ViewPager2+FlapAdapter"
                    replace(ViewPager2Testcase::class.java)
                }
                R.id.nav_slideshow -> {
                    replace(SlideshowFragment::class.java)
                }
                R.id.nav_clicks -> {
                    replace(ItemClicksTestcase::class.java)
                }
            }
            drawerLayout.close()

            toolbar.title = title
            toolbar.subtitle = subtitle

            true
        }

        toolbar.title = "多类型"
        toolbar.subtitle = "多 itemViewType"
        replace(MultiTypeTestcase::class.java)

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val fragment = supportFragmentManager.fragments.get(0)
            if (fragment is Scrollable) {
                fragment.scrollToTop()
            }
        }

    }

    private fun <T : Fragment> replace(fragmentClass: Class<T>, args: Bundle? = null) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragmentClass, args).commitAllowingStateLoss()
    }

//    private fun createAdvanceTestCase(recyclerView: RecyclerView) {
//        val models = mockModels()
//        val adapter: FlapAdapter = ShowcaseAdapter()
//        adapter.setUseComponentPool(true)
//                .setLifecycleEnable(true)
//                .setLifecycleOwner(this)
//                .data = models
//        recyclerView.adapter = adapter
//    }

}