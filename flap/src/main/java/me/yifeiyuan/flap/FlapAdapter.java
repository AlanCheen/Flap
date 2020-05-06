package me.yifeiyuan.flap;

import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.yifeiyuan.flap.extensions.ComponentPool;

import static me.yifeiyuan.flap.Preconditions.checkNotNull;

/**
 * FlapAdapter is a flexible and powerful Adapter that makes you enjoy developing with RecyclerView.
 *
 * Check these also if need :
 *
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @version 1.0
 * @since 1.1
 * @see Component
 * @see ComponentPool
 * @see me.yifeiyuan.flap.extensions.DifferFlapAdapter
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 */
public class FlapAdapter extends RecyclerView.Adapter<Component> {

    @NonNull
    private final Flap flap = Flap.getDefault();

    @NonNull
    private List<?> data = new ArrayList<>();

    private LifecycleOwner lifecycleOwner;

    private boolean lifecycleEnable = true;

    private boolean useFlapItemPool = true;

    @NonNull
    @Override
    public Component onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        return flap.onCreateViewHolder(LayoutInflater.from(parent.getContext()), parent, viewType);
    }

    @Override
    public final void onBindViewHolder(@NonNull final Component holder, final int position) {
        //ignore
    }

    @Override
    public void onBindViewHolder(@NonNull final Component holder, final int position, @NonNull final List<Object> payloads) {
        attachLifecycleOwnerIfNeed(holder);
        flap.onBindViewHolder(holder, position, getItem(position), payloads, this);
    }

    /**
     * Attaches the holder to lifecycle if need.
     *
     * @param holder The holder we are going to bind.
     */
    private void attachLifecycleOwnerIfNeed(@NonNull final Component holder) {
        if (lifecycleEnable && lifecycleOwner != null) {
            lifecycleOwner.getLifecycle().addObserver((LifecycleObserver) holder);
        }
    }

    @Override
    public int getItemCount() {
        return getData().size();
    }

    @Override
    public int getItemViewType(final int position) {
        return flap.getItemViewType(getItem(position));
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (recyclerView.getContext() instanceof LifecycleOwner && lifecycleOwner == null) {
            setLifecycleOwner((LifecycleOwner) recyclerView.getContext());
        }
        if (useFlapItemPool) {
            recyclerView.setRecycledViewPool(flap.getComponentPool());
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull Component holder) {
        super.onViewAttachedToWindow(holder);
        flap.onViewAttachedToWindow(holder, this);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull Component holder) {
        super.onViewDetachedFromWindow(holder);
        flap.onViewDetachedFromWindow(holder, this);
    }

    @Override
    public void onViewRecycled(@NonNull final Component holder) {
        super.onViewRecycled(holder);
        flap.onViewRecycled(holder, this);
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull final Component holder) {
        return flap.onFailedToRecycleView(holder, this);
    }

    public FlapAdapter setLifecycleOwner(@NonNull final LifecycleOwner lifecycleOwner) {
        checkNotNull(lifecycleOwner, "lifecycleOwner can't be null here.");
        this.lifecycleOwner = lifecycleOwner;
        return this;
    }

    public FlapAdapter setLifecycleEnable(boolean lifecycleEnable) {
        this.lifecycleEnable = lifecycleEnable;
        return this;
    }

    protected Object getItem(final int position) {
        return getData().get(position);
    }

    public FlapAdapter setData(@NonNull List<?> data) {
        checkNotNull(data, "data can't be null here.");
        this.data = data;
        return this;
    }

    public void setDataAndNotify(@NonNull List<?> data) {
        checkNotNull(data, "data can't be null here.");
        this.data = data;
        notifyDataSetChanged();
    }

    @SuppressWarnings("WeakerAccess")
    @NonNull
    public List<?> getData() {
        return data;
    }

    /**
     * Set whether use the global RecycledViewPool or not.
     *
     * NOTE : Call this before you call RecyclerView.setAdapter.
     *
     * @param enable true by default
     *
     * @return this
     */
    public FlapAdapter setUseComponentPool(final boolean enable) {
        this.useFlapItemPool = enable;
        return this;
    }

}
