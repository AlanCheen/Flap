package me.yifeiyuan.flapdev.gallery;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import me.yifeiyuan.flap.FlapViewHolder;
import me.yifeiyuan.flap.ItemFactory;

/**
 * Created by Fitz|mingjue on 2018/11/19.
 */
public class GalleryItemFactory implements ItemFactory<GalleryModel> {

    @NonNull
    @Override
    public FlapViewHolder createViewHolder(@NonNull final LayoutInflater inflater, @NonNull final ViewGroup parent, final int viewType) {
        return null;
    }

    @Override
    public int getItemViewType(final GalleryModel model) {
        return 0;
    }

}
