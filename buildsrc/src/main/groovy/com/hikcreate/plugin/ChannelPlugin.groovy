package com.hikcreate.plugin

import com.hikcreate.plugin.config.PluginConfig
import com.hikcreate.plugin.extension.ChannelExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.internal.impldep.org.apache.ivy.util.FileUtil

/**
 * 渠道发布的plugin类
 *
 * @author wangtao55* @date 2019/9/4
 * @mail wangtao55@hikcreate.com
 */
class ChannelPlugin implements Plugin<Project> {

    final static String MAKE_CHANNEL_TASK = "makeChannel"
    final static String CLEAN_PROJECT_TASK = "cleanProject"
    final static String COPY_TINKER_TASK = "copyTinkerFile"
    final static String LIB_API_TASK = "androidlibdoc"
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
        project.extensions.create('channel', ChannelExtension)
        if (!hasInit) {
            hasInit = true
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

    static def copyFilePath(fromPath, toPath, createDestIfNotExist) {
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
                copyFile(file, toFile)
            } else {
                copyFilePath(file.getPath(), toPath + "/" + file.getName(), true)
            }
        }
    }

    static def copyFile(File src, File tar) {
        InputStream is = new FileInputStream(src)
        OutputStream os = new FileOutputStream(tar)
        byte[] bytes = new byte[1024]
        int len
        while ((len = is.read(bytes)) != -1) {
            os.write(bytes)
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
                def releasePath = project.project.rootDir.path + File.separator + project.channel.appName+ releaseApkPath
                commandLine "python", pythonShellPath,releasePath ,project.channel.path360,project.channel.sdkBuildToolPath
            }
        }
        makeChannelTask.doLast {
            println 'gradle task 执行完毕...'
            println '拷贝tinker备份包到对应的目录...'
            def bakApkPath = project.project.rootDir.path + File.separator + project.channel.appName+ bakApkPath
            copyFilePath(bakApkPath, tinkerPath, true)
        }

        //声明生成lib库api的task
        Task libApiTask = project.task(LIB_API_TASK)
        libApiTask.group = MAKE_CHANNEL_GROUP
    }

}