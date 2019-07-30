package com.casstime.ec.logger;

/**
 * Created by maiwenchang at 2019-07-09 09:26
 * Description ：日志初始化配置
 */
public class LoggerConfig {

    public static final int WRITE_MODE_ASYNC = 0; //异步打印
    public static final int WRITE_MODE_SYNC = 1; //同步打印(不会加密)

    public static final String CACHE_PATH_DEFAULT = "";

    /**
     * 日志级别
     */
    int level = LogUtil.LEVEL_VERBOSE;

    /**
     * 是否输出到控制台
     */
    boolean isConsoleOpen;

    /**
     * cachePath这个参数必传，
     * 而且要data下的私有文件目录，例如 /data/data/packagename/files/xlog，
     * mmap文件会放在这个目录，如果传空串，可能会发生 SIGBUS 的crash。
     * <p>
     * final String cachePath = this.getFilesDir() + "/xlog";
     */
    String cachePath = CACHE_PATH_DEFAULT;

    /**
     * 日志保存路径,保存 log 的目录请使用单独的目录，不要存放任何其他文件防止被 xlog 自动清理功能误删。
     * <p>
     * final String SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath();
     * final String logPath = SDCARD + "/marssample/log";
     */
    String logPath;

    /**
     * 日志前缀
     */
    String namePrefix = "Log";

    /**
     * 一般情况下填0即可。非0表示会在 _cachedir 目录下存放几天的日志。
     */
    int cacheDays = 0;

    /**
     * 加密所用的 pub_key
     */
    String pubKey = "";

    /**
     * 最大文件大小，单位：字节
     */
    long maxFileSize= -1L;

    /**
     * 文件写入模式，分异步和同步，Release版本一定要用WRITE_MODE_ASYNC，使用WRITE_MODE_SYNC可能会有卡顿。
     */
    int writeMode = WRITE_MODE_ASYNC;

    public LoggerConfig setLevel(int level) {
        this.level = level;
        return this;
    }

    public LoggerConfig setConsoleOpen(boolean consoleOpen) {
        isConsoleOpen = consoleOpen;
        return this;
    }

    public LoggerConfig setCachePath(String cachePath) {
        this.cachePath = cachePath;
        return this;
    }

    public LoggerConfig setLogPath(String logPath) {
        this.logPath = logPath;
        return this;
    }


    public LoggerConfig setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
        return this;
    }

    public LoggerConfig setCacheDays(int cacheDays) {
        this.cacheDays = cacheDays;
        return this;
    }

    public LoggerConfig setPubKey(String pubKey) {
        this.pubKey = pubKey;
        return this;
    }

    public LoggerConfig setWriteMode(int writeMode) {
        this.writeMode = writeMode;
        return this;
    }

    public void setMaxFileSize(long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }
}
