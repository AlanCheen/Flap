package me.yifeiyuan.flapdev.components

import android.util.Log
import android.widget.TextView
import me.yifeiyuan.flap.dsl.adapterDelegate
import me.yifeiyuan.flap.event.Event
import me.yifeiyuan.flap.ext.bindButton
import me.yifeiyuan.flapdev.R
import me.yifeiyuan.flapdev.TestService

/**
 * Created by 程序亦非猿 on 2022/7/29.
 */

private const val TAG = "TestAllComponent"

class TestAdapterApiModel

fun createTestAdapterApiComponentDelegate() = adapterDelegate<TestAdapterApiModel>(R.layout.component_test_all_feature){

    //展示信息
    val messageTextView = findViewById<TextView>(R.id.message)

    onBind { model, position, payloads ->

        bindButton(R.id.testFireEvent) {
            // fireEvent 发送事件
            setOnClickListener{
                val showToastEvent = Event("showToast", "Fire event:showToast,position=$position") {
                    Log.d(TAG, "onBind: showToastEvent success")

                    messageTextView.text = "showToast event 收到回调：success"
                }
                fireEvent(showToastEvent)

                val intE = Event("intEvent", 233333)
                fireEvent(intE)
            }
        }

        bindButton(R.id.testGetParam){

            setOnClickListener {
                val stringValue = getParam<String>("stringValue")
                val intValue = getParam<Int>("intValue")
                val booleanValue = getParam<Boolean>("booleanValue")

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

                callService<TestService> {
                    log("LogService Message")
                    messageTextView.text = testResult()
                }

//                val logService = adapter.getAdapterService(TestService::class.java)
//                logService?.log("LogService Message")
//                messageTextView.text = logService?.testResult()
            }
        }
    }
}