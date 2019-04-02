package me.yifeiyuan.flapdev;

import android.app.Application;
import android.util.Log;

import me.yifeiyuan.flap.Flap;
import me.yifeiyuan.flapdev.items.customviewtype.CustomViewTypeItem;

/**
 * Flap
 * Created by 程序亦非猿 on 2018/12/13.
 */
public class FlapApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initFlap();
    }

    private void initFlap() {
        //only setDebug(true) when you are testing
        Flap.setDebug(true);

        long t1 = System.currentTimeMillis();
        //Factories created by apt
        //自定义 layoutId ; custom item view type
        Flap.getDefault().register(new CustomViewTypeItem.Factory());

        long t2 = System.currentTimeMillis();

        Log.e("Flap", "Init Flap time cost :" + (t2 - t1));

//        Flap.getDefault().getFlapItemPool().setMaxRecycledViews(new SimpleImageItem.Factory().getItemViewType(null), 8);
    }
}
