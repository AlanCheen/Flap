package me.yifeiyuan.flap.plugin;

import org.gradle.api.Project;
import org.gradle.api.logging.Logger;

/**
 * Created by 程序亦非猿 on 2020/9/8.
 */
class Log {

    private static Logger logger;

    static void setup(Project project) {
        logger = project.getLogger();
    }

    static void i(String info) {
        logger.info("FlapPlugin:" + info);
    }

    static void println(Object obj) {
        System.out.println("FlapPlugin: " + obj);
    }

    static void println(String msg) {
        System.out.println("FlapPlugin: " + msg);
    }
}
