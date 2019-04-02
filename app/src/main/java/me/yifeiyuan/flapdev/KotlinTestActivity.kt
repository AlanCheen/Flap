package me.yifeiyuan.flapdev

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_kotlin_test.*
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flapdev.R
import me.yifeiyuan.flapdev.items.simpletext.SimpleTextModel
import java.util.*

class KotlinTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_test)

        var adapter = FlapAdapter()

        adapter.data = mock()

        rv_kotlin.adapter = adapter

    }

    private fun mock(): MutableList<*> {
        var arrayList = ArrayList<Any>();

        arrayList.add(SimpleTextModel("Android"))
        arrayList.add(SimpleTextModel("Java"))
        arrayList.add(SimpleTextModel("Kotlin"))
        return arrayList
    }
}
