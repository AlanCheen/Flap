package me.yifeiyuan.flapdev.components.simpletext;

import androidx.annotation.NonNull;

/**
 * Created by 程序亦非猿
 */
public class SimpleTextModel {

    @NonNull
    public String content;

    public SimpleTextModel(@NonNull final String content) {
        this.content = content;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final SimpleTextModel that = (SimpleTextModel) o;

        return content.equals(that.content);
    }

    @Override
    public int hashCode() {
        return content.hashCode();
    }

    @Override
    public String toString() {
        return "SimpleTextModel{" +
                "content='" + content + '\'' +
                '}';
    }
}
