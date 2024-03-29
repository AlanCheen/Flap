package me.yifeiyuan.flapdev

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import me.yifeiyuan.flap.ext.ifIs
import me.yifeiyuan.flapdev.testcases.*
import java.lang.Exception

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
        navView.setItemIconTintList(null)
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
                R.id.nav_preload -> {
                    title = "预加载"
                    subtitle = "在滑动到底部或顶部之前预先加载"
                    replace(PreloadTestcase::class.java)
                }
                R.id.nav_component_pool -> {
                    title = "ComponentPool"
                    subtitle = "滑动列表观察缓存情况"
                    replace(ComponentPoolTestcase::class.java)
                }
                R.id.nav_flap_rv -> {
                    title = "FlapRecyclerView"
                    subtitle = "自定义 RecyclerView"
                    replace(FlapRecyclerViewTestcase::class.java)
                }
                R.id.nav_viewpager2 -> {
                    title = "ViewPager2 适配"
                    subtitle = "ViewPager2+FlapAdapter"
                    replace(ViewPager2Testcase::class.java)
                }
                R.id.nav_diff -> {
                    title = "FlapDifferAdapter"
                    subtitle = "修改数据后，下拉刷新观察"
                    replace(FlapDifferAdapterTestcase::class.java)
                }
                R.id.nav_header_footer -> {
                    subtitle = "Header、Footer"
                    replace(HeaderFooterTestcase::class.java)
                }
                R.id.nav_animation -> {
                    subtitle = "动画"
                    replace(AnimationTestcase::class.java)
                }
                R.id.nav_github_demo -> {
                    subtitle = "GitHub Demo"
                    replace(GitHubDemoFragment::class.java)
                }
                R.id.nav_item_decorations -> {
                    subtitle = "ItemDecorations"
                    replace(ItemDecorationTestcase::class.java)
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
            val fragment = supportFragmentManager.findFragmentByTag("testcase")
            fragment.ifIs<IMenuView> {
                showMenu()
            }
        }

        fab.setOnLongClickListener {
            val fragment = supportFragmentManager.findFragmentByTag("testcase")
            if (fragment is Scrollable) {
                fragment.scrollToTop()
            }
            true
        }
    }

    private fun <T : Fragment> replace(fragmentClass: Class<T>, args: Bundle? = null) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragmentClass, args, "testcase").commitAllowingStateLoss()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.author -> {
                val url = "https://www.yuque.com/cxyfy/blog/axzri0"
                val uri: Uri = Uri.parse(url)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                try {
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            R.id.github -> {
                val url = "https://github.com/AlanCheen/Flap"
                val uri: Uri = Uri.parse(url)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                try {
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home, menu)
        return true
    }
}