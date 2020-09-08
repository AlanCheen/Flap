package me.yifeiyuan.flap.plugin;

import com.android.build.gradle.AppExtension;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.jetbrains.annotations.NotNull;

/**
 * Created by 程序亦非猿 on 2020/9/8.
 */
class FlapPlugin implements Plugin<Project> {
    @Override
    public void apply(@NotNull Project project) {
        System.out.println("FlapPlugin apply");

        Log.setup(project);
        AppExtension appExtension = project.getExtensions().getByType(AppExtension.class);
        appExtension.registerTransform(new FlapTransform(project));
    }
}
