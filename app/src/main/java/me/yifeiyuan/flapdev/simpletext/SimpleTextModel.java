package me.yifeiyuan.flapdev.simpletext;

import android.support.annotation.NonNull;

/**
 * Created by 程序亦非猿
 */
public class SimpleTextModel {

    @NonNull
    public String content;

    public SimpleTextModel(@NonNull final String content) {
        this.content = content;
    }
}
