package me.yifeiyuan.flap.internal;

import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import me.yifeiyuan.flap.FlapAdapter;
import me.yifeiyuan.flap.FlapItem;

/**
 * Created by 程序亦非猿
 *
 * DefaultFlapItem is a build-in FlapItem that would be used when something went wrong .
 *
 * So that Flap won't crash your App.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public final class DefaultFlapItem extends FlapItem {

    private static final int DEFAULT_ITEM_TYPE = -66666;

    public DefaultFlapItem(final View itemView) {
        super(itemView);
    }

    @Override
    protected void onBind(@NonNull final Object model, @NonNull final FlapAdapter adapter, @NonNull final List payloads) {

    }

    public static class Factory implements FlapItemFactory {

        @NonNull
        @Override
        public FlapItem onCreateViewHolder(@NonNull final LayoutInflater inflater, @NonNull final ViewGroup parent, final int viewType) {

            View view = new View(parent.getContext());
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
            view.setLayoutParams(layoutParams);
            return new DefaultFlapItem(view);
        }

        @Override
        public int getItemViewType(final Object model) {
            return DEFAULT_ITEM_TYPE;
        }
    }
}
