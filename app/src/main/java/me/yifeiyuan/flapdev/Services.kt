package me.yifeiyuan.flapdev

import android.util.Log
import me.yifeiyuan.flap.service.AdapterService

/**
 * Created by 程序亦非猿 on 2022/8/16.
 */

private const val TAG = "Services"

class LogService : AdapterService {

    fun log(message: String){
        Log.d(TAG, "log() called with: message = $message")
    }

    fun testResult():String{
        return "TestService.testResult is called!"
    }
}