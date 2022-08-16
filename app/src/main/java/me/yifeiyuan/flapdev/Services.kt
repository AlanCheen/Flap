package me.yifeiyuan.flapdev

import android.util.Log
import me.yifeiyuan.flap.service.FlapService

/**
 * Created by 程序亦非猿 on 2022/8/16.
 */

private const val TAG = "Services"

class LogService : FlapService {

    fun log(message: String){
        Log.d(TAG, "log() called with: message = $message")
    }
}