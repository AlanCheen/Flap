package me.yifeiyuan.flap.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ApplicationPlugin

import org.gradle.api.logging.Logger


class FlapPlugin implements Plugin<Project>{

    @Override
    void apply(Project project) {

        project.task("flapTask"){
            doLast {
                println("flap plugin added")
            }
        }

        Logger logger  = project.getLogger()
        def isApp = project.plugins.hasPlugin(ApplicationPlugin)

        logger.info("isApp:"+isApp)
        if (isApp) {

        }
    }
}