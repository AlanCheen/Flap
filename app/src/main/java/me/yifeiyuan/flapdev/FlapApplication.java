package me.yifeiyuan.flapdev;

import android.app.Application;
import android.util.Log;

import me.yifeiyuan.flap.Flap;
import me.yifeiyuan.flapdev.components.customviewtype.CustomViewTypeComponent;

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
        //自定义 item view type ; custom item view type
        Flap.getDefault().register(new CustomViewTypeComponent.Factory());
        long t2 = System.currentTimeMillis();

        Log.e("Flap", "Init Flap time cost :" + (t2 - t1));

        Flap.getDefault().registerFlowListener(new ComponentMonitor());

//        Flap.getDefault().getFlapItemPool().setMaxRecycledViews(new SimpleImageItem.Factory().getItemViewType(null), 8);
    }
}
