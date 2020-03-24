package me.yifeiyuan.flapdev;

import android.os.SystemClock;
import android.util.Log;

import me.yifeiyuan.flap.FlapComponent;
import me.yifeiyuan.flap.extensions.ComponentFlowListener;
import me.yifeiyuan.flap.internal.ComponentProxy;
/**
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 *
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 2020/3/24 11:14 PM
 * @since 1.0
 */
public class ComponentMonitor implements ComponentFlowListener {

    private static final String TAG = "ComponentMonitor";

    private long createTime;
    private long bindTime;

    @Override
    public void onStartCreateComponent(final ComponentProxy factory) {
        Log.d(TAG, "开始使用 " + factory.getClass().getSimpleName() + " 创建组件");
        createTime = SystemClock.uptimeMillis();
    }

    @Override
    public void onComponentCreated(final ComponentProxy factory, final FlapComponent component) {
        long timeCost = SystemClock.uptimeMillis() - createTime;
        Log.d(TAG, "组件 " + component.getClass().getSimpleName() + " 创建完毕，耗时 " + timeCost + " 毫秒");
    }

    @Override
    public void onStartBindComponent(final FlapComponent component, final int position, final Object model) {
        Log.d(TAG, "开始绑定组件 [" + component.getClass().getSimpleName() + "], position = [" + position + "], model = [" + model + "]");
        bindTime = SystemClock.uptimeMillis();
    }

    @Override
    public void onComponentBound(final FlapComponent component, final int position, final Object model) {
        Log.d(TAG, "组件绑定完毕，耗时 " + (SystemClock.uptimeMillis() - bindTime) + " 毫秒");
    }
}
