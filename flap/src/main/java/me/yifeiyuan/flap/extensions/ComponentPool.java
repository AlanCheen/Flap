package me.yifeiyuan.flap.extensions;

import android.content.ComponentCallbacks2;
import android.content.res.Configuration;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import me.yifeiyuan.flap.FlapAdapter;

/**
 * A global RecycledViewPool that can be shared among RecyclerViews , which is enabled by default.
 *
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @see FlapAdapter#setUseComponentPool(boolean)
 * <p>
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 * @since 2019/1/2
 * @since 0.9.1
 */
public class ComponentPool extends RecyclerView.RecycledViewPool implements ComponentCallbacks2 {

    @Override
    public void onTrimMemory(int level) {

        switch (level) {
            case TRIM_MEMORY_RUNNING_CRITICAL:
            case TRIM_MEMORY_RUNNING_LOW:
            case TRIM_MEMORY_RUNNING_MODERATE:
                clear();
                break;
            case TRIM_MEMORY_BACKGROUND:
            case TRIM_MEMORY_COMPLETE:
            case TRIM_MEMORY_MODERATE:
            case TRIM_MEMORY_UI_HIDDEN:
            default:
                break;
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {

    }

    @Override
    public void onLowMemory() {
        clear();
    }
}
