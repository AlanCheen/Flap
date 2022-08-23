package me.yifeiyuan.flapdev.components

import android.util.Log
import android.view.View
import android.widget.TextView
import me.yifeiyuan.flap.Component
import me.yifeiyuan.flap.FlapAdapter
import me.yifeiyuan.flap.annotations.Delegate
import me.yifeiyuan.flap.delegate.AdapterDelegate
import me.yifeiyuan.flap.event.Event
import me.yifeiyuan.flap.ext.bindButton
import me.yifeiyuan.flapdev.TestService
import me.yifeiyuan.flapdev.R
import kotlin.text.StringBuilder

/**
 * Created by 程序亦非猿 on 2022/7/29.
 */

private const val TAG = "TestAllComponent"

class TestAllModel

@Delegate(layoutId = R.layout.component_test_all_feature)
class TestAllComponent(view: View) : Component<TestAllModel>(view) {

    override fun onBind(model: TestAllModel, position: Int, payloads: List<Any>, adapter: FlapAdapter, delegate: AdapterDelegate<*, *>) {

        //展示信息
        val messageTextView = findViewById<TextView>(R.id.message)

        bindButton(R.id.testFireEvent) {
            // fireEvent 发送事件
            setOnClickListener{
                val showToastEvent = Event("showToast", "Fire event:showToast,position=$position") {
                    Log.d(TAG, "onBind: showToastEvent success")

                    messageTextView.text = "showToast event 收到回调：success"
                }
                adapter.fireEvent(showToastEvent)

                val intE = Event("intEvent", 233333)
                adapter.fireEvent(intE)
            }
        }

        bindButton(R.id.testGetParam){

            setOnClickListener {
                val stringValue = adapter.getParam<String>("stringValue")
                val intValue = adapter.getParam<Int>("intValue")
                val booleanValue = adapter.getParam<Boolean>("booleanValue")

                val results :String = StringBuilder().
                append("stringValue=$stringValue ;;")
                        .append("intValue=$intValue ;;")
                        .append("booleanValue=$booleanValue ;;")
                        .toString()

                messageTextView.text = "Adapter.getParam results=$results"
            }
        }

        bindButton(R.id.testGetAdapterService) {
            setOnClickListener {

                val logService = adapter.getAdapterService(TestService::class.java)
                logService?.log("LogService Message")

                val logService2 = adapter.getAdapterService<TestService>("LogService")

                val result = logService2?.testResult()
                messageTextView.text = result
            }
        }
    }

    override fun onBind(model: TestAllModel) {
    }
}
