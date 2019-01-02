package me.yifeiyuan.flapdev;

import android.app.Application;
import android.util.Log;

import me.yifeiyuan.flap.Flap;
import me.yifeiyuan.flapdev.simpleimage.SimpleImageItem;
import me.yifeiyuan.flapdev.simpletext.SimpleTextItem;

/**
 * Flap
 * Created by 程序亦非猿 on 2018/12/13.
 */
public class FlapApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Flap.setDebug(true);

        long t1 = System.currentTimeMillis();

        Flap.getDefault().register(new SimpleTextItem.Factory());
        Flap.getDefault().register(new SimpleImageItem.Factory());

        long t2 = System.currentTimeMillis();

        Log.e("Flap", "Init Flap time cost :" + (t2 - t1));

    }
}
