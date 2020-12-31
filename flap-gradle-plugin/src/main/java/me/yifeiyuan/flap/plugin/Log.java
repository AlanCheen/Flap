package me.yifeiyuan.flap.plugin;

import org.gradle.api.Project;
import org.gradle.api.logging.Logger;

/**
 * Created by 程序亦非猿 on 2020/9/8.
 */
class Log {

    private static Logger logger;

    private static boolean debug = false;

    static void setup(Project project) {
        logger = project.getLogger();
    }

    static void i(String info) {
        if (debug) {
            logger.info("FlapPlugin:" + info);
        }
    }

    static void println(Object obj) {
        System.out.println("FlapPlugin: " + obj);
    }

    static void println(String msg) {
        if (debug) {
            System.out.println("FlapPlugin: " + msg);
        }
    }
}
