#发包的注意事项
### 整体环境配置
* 在app的build.gradle文件中buildConfigField("boolean", "IS_TEST", "true");,
true改为false，切换到生产环境

###发版之前需要验证功能
* 验证热更新功能
* 验证全量版本升级的功能；
* 验证渠道信息是否在bugly正常显示，
* 验证崩溃信息用户userid是否正常显示

### 发新版包：
* //baseApk = "${bakPath}/${baseApkDir}/app-release.apk"注释掉， 
* tinkerId = "base-1.0.1"，修改一下；
* isProtectedApp = true，现在打包都需要使用应用宝进行加固，保持默认即可
* 执行assembleRelease即可，在bakapk目录下面，得到对应时间的apk包，
* 使用应用宝进行打包加固，注意渠道信息设置上，保存基线版本以便后面发布补丁包

###发布热更新补丁
* 保证基线版本放置在bakApk目录下面，
* def baseApkDir = "app-0726-12-59-08"对应于基线版本的路径，需要修改一下时间对应即可
* //baseApk = "${bakPath}/${baseApkDir}/app-release.apk"放开注释
* tinkerId = "path-1.0.1"修改id
* tinker-support build目录下执行buildTinkerPatchRelease
* outputs的目录下patch目录，tinker中的补丁拿到，将补丁包上传到bugly的热更新后台发布即可，
* 可以选择开发设备或者是全量设备更新


