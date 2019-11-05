package com.hikcreate.plugin

import com.hikcreate.plugin.config.PluginConfig
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

/**
 * 渠道发布的plugin类
 *
 * @author wangtao55* @date 2019/9/4
 * @mail wangtao55@hikcreate.com
 */
class ChannelPlugin implements Plugin<Project> {

    final static String MAKE_CHANNEL_TASK = "makeChannel"
    final static String CLEAN_PROJECT_TASK = "cleanProject"
    //插件group名称
    final static String MAKE_CHANNEL_GROUP = "hikcreate"

    final static String MAKE_BUILD_RELEASE_TASK = "buildRelease"

    final static String CLEAN_FLAG = "clean"
    final static String PUBLIC_FLAG = "assembleRelease"

    static def hasInit = false
    //tinker 基准包路径
    static def bakApkPath = PluginConfig.bakApkPath
    //release包产生的路径
    static def releaseApkPath = PluginConfig.releaseApkPath
    //可执行的python脚本路径
    static def pythonShellPath = File.separator + "buildsrc" + File.separator + "walle" + File.separator + "pack.py"
    //最终tinker基准包存放的路径
    static def tinkerPath = File.separator + "buildsrc" + File.separator + "walle" + File.separator + "ctinker" + File.separator

    @Override
    void apply(Project project) {
//        project.extensions.create('channel', ChannelExtension)
        System.out.println("========================")
        System.out.println("start make channel package hik gradle plugin!")
        System.out.println("========================")
        if(!hasInit){
            hasInit = true
            bakApkPath = project.project.rootDir.path + bakApkPath
            releaseApkPath = project.project.rootDir.path + releaseApkPath
            pythonShellPath = project.project.rootDir.path + pythonShellPath
            tinkerPath = project.project.rootDir.path + tinkerPath
        }
        makeChannelTake(project)
    }

    static def createFile(path, createIfNotExist) {
        def file = new File(path)
        if (!file.exists()) {
            if (createIfNotExist) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs()
                }
                file.createNewFile()
            } else {
                throw NullPointerException("Missing File :" + path)
            }
        }
        return file
    }

    static def copyFile(fromPath, toPath, createDestIfNotExist) {

        def fromFile = new File(fromPath)
        def toPathFile = new File(toPath)

        if (toPathFile.exists()) {
            toPathFile.deleteDir()
        }
        toPathFile.mkdirs()

        if (!fromFile.exists()) {
            return false
        }
        def listFiles = fromFile.listFiles()
        listFiles.each { file ->
            if (file.isFile()) {
                def toFile = createFile(toPath + "/" + file.getName(), createDestIfNotExist)
                toFile.withWriter { ffile ->
                    file.eachLine { line ->
                        ffile.writeLine(line)
                    }
                }
            } else {
                copyFile(file.getPath(), toPath + "/" + file.getName(), true)
            }
        }
    }

    /**
     * 创建相关的task
     * @param project
     * @return
     */
    static def makeChannelTake(Project project) {

        //声明clean工程的task
        Task cleanTask = project.task(CLEAN_PROJECT_TASK)
        cleanTask.group = MAKE_CHANNEL_GROUP
        cleanTask.dependsOn(CLEAN_FLAG)

        //声明编译release版本的task
        Task makeReleaseTask = project.task(MAKE_BUILD_RELEASE_TASK)
        makeReleaseTask.group = MAKE_CHANNEL_GROUP
        makeReleaseTask.dependsOn(PUBLIC_FLAG)

        //声明makeChannelTask
        Task makeChannelTask = project.task(MAKE_CHANNEL_TASK)
        makeChannelTask.group = MAKE_CHANNEL_GROUP
        makeChannelTask.dependsOn(makeReleaseTask)
        makeChannelTask.dependsOn(cleanTask)
        //noinspection UnstableApiUsage
        makeReleaseTask.mustRunAfter(cleanTask)
        makeChannelTask.doLast {
            project.exec {
                workingDir './'
                commandLine "python", pythonShellPath, releaseApkPath, bakApkPath
            }
        }
        makeChannelTask.doLast {
            println 'gradle task 执行完毕...'
            println '拷贝tinker备份包到对应的目录...'
            try {
                copyFile(bakApkPath, tinkerPath, true)
            } catch (Exception e) {
                e.printStackTrace()
            }
        }
    }
}