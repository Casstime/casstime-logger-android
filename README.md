介绍¶
高性能日志打印组件，基于Tencent/mars/Xlog封装，具有异步打印和加密功能。

安装教程¶
引入


implementation 'com.casstime.ec:logger:0.0.1'
初始化
Kotlin

val config = LoggerConfig()
config.apply {
    //日志级别
    setLevel(if (isProduction) LogUtil.LEVEL_INFO else LogUtil.LEVEL_VERBOSE)
    //日志缓存路径,建议设置data/data/{package name}/file 中的路径，否则可能出错
    setCachePath(application.filesDir.toString() + "/xlog")
    //日志路径 
    setLogPath(logPath) 
    //日志文件前缀
    setNamePrefix(BuildConfig.FLAVOR.replace("_", Uri.EMPTY.toString()))
    //是否在控制台输出日志：生产环境关闭
    setConsoleOpen(!isProduction)
    //加密公钥
    setPubKey(PUB_KEY) 
    //日志转储方式:异步
    setWriteMode(LoggerConfig.WRITE_MODE_ASYNC)
}
LogUtil.initLogger(config)
Java
使用说明¶
1. LogUtil¶
日志打印示例


//在MainActivity的onCreate()方法中：
LogUtil.v("Log test verbose");
LogUtil.d("Log test debug");
LogUtil.i("Log test info")
LogUtil.w("Log test warning");
LogUtil.e("Log test error");
LogUtil.f("Log test fatal");
会在控制台输出日志:


2019-07-09 11:33:27.220 1655-1655/com.casstime.ec E/MainActivity.java:onCreate(MainActivity.java:39) >> Log test verbose
2019-07-09 11:33:27.220 1655-1655/com.casstime.ec E/MainActivity.java:onCreate(MainActivity.java:39) >> Log test debug
2019-07-09 11:33:27.220 1655-1655/com.casstime.ec E/MainActivity.java:onCreate(MainActivity.java:39) >> Log test info
2019-07-09 11:33:27.220 1655-1655/com.casstime.ec E/MainActivity.java:onCreate(MainActivity.java:39) >> Log test warning
2019-07-09 11:33:27.220 1655-1655/com.casstime.ec E/MainActivity.java:onCreate(MainActivity.java:39) >> Log test error
2019-07-09 11:33:27.220 1655-1655/com.casstime.ec E/MainActivity.java:onCreate(MainActivity.java:39) >> Log test fatal
并存储在指定文件夹中：


📂/sdcard/Android/data/com.casstime.ec/cache/log/test_20190709.xlog

public static void setLogLevel(int level)
设置日志级别，除了初始化的时候可以设置日志级别，通过此方法也可以改变日志级别。


public static void appenderFlush(boolean isSync)
当日志写入模式为异步时，调用该接口会把内存中的日志写入到文件。

isSync : true 为同步 flush，flush 结束后才会返回。 false 为异步 flush，不等待 flush 结束就返回。


public static void appenderClose()
关闭日志，在程序退出时调用。


public static void setMaxFileSize(long size)
设置文件大小限制，单位：字节。


public static void setAppenderMode(int mode)
设置文件写入模式，分异步和同步，Release版本一定要用LoggerConfig.WRITE_MODE_ASYNC，使用LoggerConfig.WRITE_MODE_SYNC可能会有卡顿。

2. LoggerConfig 日志配置¶
Tip

日志初始化配置，下面介绍LoggerConfig的可配置属性，皆有对应setter方法

level 日志级别

level	值	对应常量	说明
Verbose	0	LogUtil.LEVEL_VERBOSE	🎯次要日志信息
Debug	1	LogUtil.LEVEL_DEBUG	🐞调试日志信息
Info	2	LogUtil.LEVEL_INFO	🔑关键日志信息
Warning	3	LogUtil.LEVEL_WARNING	⚠️警告日志信息
Error	4	LogUtil.LEVEL_ERROR	❌错误日志信息
Fatal	5	LogUtil.LEVEL_FATAL	💀致命日志信息
None(不打印)	6	LogUtil.LEVEL_NONE	
日志级别为None的话表示不打印日志；开发模式默认为Verbose，日志全部打印；在生产环境默认为Info，只打印关键信息。

writeMode 文件写入模式

分异步和同步，Release版本一定要用LoggerConfig.WRITE_MODE_ASYNC，使用LoggerConfig.WRITE_MODE_SYNC可能会有卡顿。

cachePath 缓存目录

cachePath这个参数必传，而且要data/data下的私有文件目录，例如 /data/data/packagename/files/xlog，mmap文件会放在这个目录，如果传空串，可能会发生 SIGBUS 的crash。

logPath 日志写入目录

保存 log 的目录请使用单独的目录，不要存放任何其他文件防止被 xlog 自动清理功能误删。

namePrefix 日志文件名的前缀

例如，namePrefix="Test"，生成的日志文件会命名为：Test_20190709.xlog。

cacheDays 缓存保留天数

一般情况下填0即可。非0表示会在 cachePath 目录下存放几天的日志。

pubKey 加密公钥

加密所用的公钥，秘钥对生成方法参考Xlog 加密使用指引，传空表示只压缩不加密，默认传空。

isConsoleOpen 是否开启控制台打印

是否会把日志打印到 logcat 中， 默认不打印。

maxFileSize 文件大小设置

单个日志文件Size限制，单位：字节。默认值：2MB。

3. 日志加密&解压缩指引¶
参考 ：加密指引

安装环境

安装了python2.7x的版本。

下载整理好的工具包crypt.zip，解压后先安装pyelliptic1.5.7。


cd pyelliptic-1.5.7
python setup.py install 或 sudo python setup.py install
生成秘钥

在crypt目录下，执行


python gen_key.py
会生成private key 和public key。

Tip

1.public key传给LoggerConfig.pubKey。

2.private key务必保存在安全的位置，防止泄露。

3.把这两个key设置到decode_mars_crypt_log_file.py脚本中。

加密&解压缩

如果没有使用加密，日志文件仍然会被压缩，这时候需要解压缩：


sudo python ./decode_mars_nocrypt_log_file.py test_20190709.xlog
将在当前目录下得到解压缩后的可阅读日志文件：📃 test_20190709.xlog.log

如果使用了加密，需要解密并解压缩：


sudo python ./decode_mars_crypt_log_file.py test_20190709_1.xlog
同样当前目录下得到解密并解压缩后的可阅读日志文件：📃 test_20190709_1.xlog.log