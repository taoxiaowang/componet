
package com.hikcreate.plugin.config

/**
 * 不同的工程需要添加的配置，都是相对路径
 *
 * @author wangtao55* @date 2019/9/4
 * @mail wangtao55@hikcreate.com
 */
class PluginConfig{
    //build文件夹路径
    static def buildPath =  File.separator + "build"+ File.separator
    //tinker 基准包路径
    static def bakApkPath = buildPath + "bakApk"
    //release包产生的路径
    static def releaseApkPath = buildPath + "outputs" + File.separator + "apk" + File.separator + "release"
}