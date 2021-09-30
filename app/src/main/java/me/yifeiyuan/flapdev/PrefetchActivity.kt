package me.yifeiyuan.flapdev

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import me.yifeiyuan.flap.hook.PrefetchDetector

class PrefetchActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "PrefetchActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prefetch)

        val adapter: ShowcaseAdapter = ShowcaseAdapter().apply {
            mockData()
        }

        val prefetchDetector = PrefetchDetector(3) {
            Log.d(TAG, "onCreate: prefetch")
            Handler().postDelayed({
                adapter.appendMockData()
            }, 500)
        }

        prefetchDetector.prefetchEnable = true // 默认 true

        prefetchDetector.setPrefetchComplete() // 当出错时，需要手动调用

        adapter.registerAdapterHook(prefetchDetector)

        val rv = findViewById<RecyclerView>(R.id.list)

        rv.adapter = adapter

    }
}