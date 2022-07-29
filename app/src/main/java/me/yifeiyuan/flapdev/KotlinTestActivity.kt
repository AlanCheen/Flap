package me.yifeiyuan.flapdev

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_kotlin_test.*
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flapdev.components.SimpleTextModel
import java.util.*

class KotlinTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_test)

        val adapter = FlapAdapter()

        adapter.setData(mock())

        rv_kotlin.adapter = adapter

    }

    private fun mock(): MutableList<Any> {
        val arrayList = ArrayList<Any>()

        arrayList.add(SimpleTextModel("Android"))
        arrayList.add(SimpleTextModel("Java"))
        arrayList.add(SimpleTextModel("Kotlin"))
        return arrayList
    }
}
