package me.yifeiyuan.flapdev

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import me.yifeiyuan.flap.ktmodule.KtComponentModel
import me.yifeiyuan.flapdev.components.customviewtype.CustomModel
import me.yifeiyuan.flapdev.components.databindingsample.SimpleDataBindingModel
import me.yifeiyuan.flapdev.components.generictest.GenericModel
import me.yifeiyuan.flapdev.components.simpleimage.SimpleImageModel
import me.yifeiyuan.flapdev.components.simpletext.SimpleTextModel
import me.yifeiyuan.flapdev.showcase.MultiTypeFragment
import me.yifeiyuan.flapdev.showcase.PrefetchFragment
import me.yifeiyuan.flapdev.showcase.selection.SlideshowFragment
import me.yifeiyuan.flapdev.showcase.swipe.GalleryFragment
import me.yifeiyuan.ktx.foundation.othermodule.JavaModuleModel
import me.yifeiyuan.ktx.foundation.othermodule.vb.VBModel
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

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
                    replace(MultiTypeFragment::class.java)
                }
                R.id.nav_prefetch -> {
                    title = "预加载"
                    subtitle = "在滑动到底部之前预先加载"
                    replace(PrefetchFragment::class.java)
                }
                R.id.nav_gallery -> {
                    replace(GalleryFragment::class.java)
                }
                R.id.nav_slideshow -> {
                    replace(SlideshowFragment::class.java)
                }
            }
            drawerLayout.close()

            toolbar.title = title
            toolbar.subtitle = subtitle

            true
        }

        replace(MultiTypeFragment::class.java)

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