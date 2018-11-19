package me.yifeiyuan.flapdev;

import java.util.List;

import me.yifeiyuan.flap.FlapAdapter;

/**
 * Created by Fitz|mingjue on 2018/11/19.
 */
public class SampleAdapter extends FlapAdapter {

    private List<Object> models;

    public void setModels(final List<Object> models) {
        this.models = models;
        notifyDataSetChanged();
    }

    @Override
    protected List<Object> getModels() {
        return models;
    }
}
