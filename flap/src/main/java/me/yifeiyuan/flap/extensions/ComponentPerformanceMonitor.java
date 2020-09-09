package me.yifeiyuan.flap.extensions;

import android.os.SystemClock;
import android.util.Log;

import me.yifeiyuan.flap.Component;
import me.yifeiyuan.flap.internal.ComponentProxy;

/**
 * 组件性能监控
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 *
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 2020/3/24 11:14 PM
 * @since 1.5.2
 */
public class ComponentPerformanceMonitor implements ComponentFlowListener {

    private static final String TAG = "Flap-PerformanceMonitor";

    private long createTime;
    private long bindTime;

    @Override
    public void onStartCreateComponent(final ComponentProxy proxy) {
        if (proxy==null){
            return;
        }
        Log.d(TAG, "开始使用 " + proxy.getClass().getSimpleName() + " 创建组件");
        createTime = SystemClock.uptimeMillis();
    }

    @Override
    public void onComponentCreated(final ComponentProxy proxy, final Component component) {
        if (proxy==null){
            return;
        }
        long timeCost = SystemClock.uptimeMillis() - createTime;
        Log.d(TAG, "组件 " + component.getClass().getSimpleName() + " 创建完毕，耗时 " + timeCost + " 毫秒");
        if (timeCost > getCreateCostThreshold()) {
            Log.w(TAG, "请注意：" + component.getClass().getSimpleName() + " 组件创建超过阈值，请优化！！！ ");
        }
    }

    @Override
    public void onStartBindComponent(final Component component, final int position, final Object model) {
        Log.d(TAG, "开始绑定组件 [" + component.getClass().getSimpleName() + "], position = [" + position + "], model = [" + model + "]");
        bindTime = SystemClock.uptimeMillis();
    }

    public void onComponentBound(final Component component, final int position, final Object model) {
        long timeCost = SystemClock.uptimeMillis() - bindTime;
        Log.d(TAG, "组件绑定完毕，耗时 " + timeCost + " 毫秒");
        if (timeCost > getBindCostThreshold()) {
            Log.w(TAG, "请注意：" + component.getClass().getSimpleName() + " 组件绑定超时，请优化！ ");
        }
    }

    /**
     * 创建组件的耗时阈值，超过阈值会有警告。
     * 默认 20 毫秒
     *
     * @return 毫秒
     */
    protected long getCreateCostThreshold() {
        return 20;
    }

    /**
     * 绑定组件的耗时阈值，超过阈值会有警告。
     * 默认 5 毫秒
     *
     * @return 毫秒
     */
    protected long getBindCostThreshold() {
        return 5;
    }
}
