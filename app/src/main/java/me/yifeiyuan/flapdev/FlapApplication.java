package me.yifeiyuan.flapdev;

import android.app.Application;
import android.util.Log;

import me.yifeiyuan.flap.Flap;
import me.yifeiyuan.flapdev.simpleimage.SimpleImageItem;
import me.yifeiyuan.flapdev.simpleimage.SimpleImageModel;
import me.yifeiyuan.flapdev.simpletext.SimpleTextItem;
import me.yifeiyuan.flapdev.simpletext.SimpleTextModel;

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

        Flap.getDefault().register(SimpleTextModel.class, new SimpleTextItem.SimpleTextItemFactory());
        Flap.getDefault().register(SimpleImageModel.class, new SimpleImageItem.SimpleImageItemFactory());

        long t2 = System.currentTimeMillis();

        Log.e("Flap", "Init Flap time cost :" + (t2 - t1));

    }
}
