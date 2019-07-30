package com.casstime.ec.logger;

import android.text.TextUtils;

import com.tencent.mars.xlog.Log;
import com.tencent.mars.xlog.Xlog;

/**
 * Created by WenChang Mai on 2019/2/15 10:59.
 * Description: Log 工具类
 */
public class LogUtil {

    public static final int LEVEL_VERBOSE = 0;
    public static final int LEVEL_DEBUG = 1;
    public static final int LEVEL_INFO = 2;
    public static final int LEVEL_WARNING = 3;
    public static final int LEVEL_ERROR = 4;
    public static final int LEVEL_FATAL = 5;
    public static final int LEVEL_NONE = 6;

    private static String className;//类名
    private static String methodName;//方法名
    private static int lineNumber;//行数

    static {
        //默认配置
        Log.setLevel(BuildConfig.DEBUG ? LEVEL_VERBOSE : LEVEL_INFO, false);
    }

    private LogUtil() {
        /* Protect from instantiations */
    }

    private static String createLog(String log) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(methodName)
                .append("(").append(className).append(":").append(lineNumber).append(")")
                .append(" >> ")
                .append(log);
        return buffer.toString();
    }

    private static void getMethodNames(StackTraceElement[] sElements) {
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }

    public static void v(String message) {
        getMethodNames(new Throwable().getStackTrace());
        Log.v(className, createLog(message));
    }

    public static void d(String message) {
        getMethodNames(new Throwable().getStackTrace());
        Log.d(className, createLog(message));
    }

    public static void i(String message) {
        getMethodNames(new Throwable().getStackTrace());
        Log.i(className, createLog(message));
    }

    public static void w(String message) {
        getMethodNames(new Throwable().getStackTrace());
        Log.w(className, createLog(message));
    }

    public static void e(String message) {
        // Throwable instance must be created before any methods
        getMethodNames(new Throwable().getStackTrace());
        Log.e(className, createLog(message));
    }

    public static void f(String message) {
        // Throwable instance must be created before any methods
        getMethodNames(new Throwable().getStackTrace());
        Log.f(className, createLog(message));
    }


    public static void initLogger(LoggerConfig builder) {
        if (builder == null) {
            return;
        }

        if (TextUtils.isEmpty(builder.cachePath)) {
            return;
        }
        if (TextUtils.isEmpty(builder.logPath)) {
            return;
        }

        //加载so库
        try {
            System.loadLibrary("c++logger_shared");
            System.loadLibrary("marsxlog");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Xlog.appenderOpen(builder.level,
                builder.writeMode,
                builder.cachePath,
                builder.logPath,
                builder.namePrefix,
                builder.cacheDays,
                builder.pubKey);

        Xlog.setConsoleLogOpen(builder.isConsoleOpen);

        Log.setLogImp(new Xlog());

        if (builder.maxFileSize >= 0) {
            setMaxFileSize(builder.maxFileSize);
        } else {
            setMaxFileSize(2 * 1024 * 1024);
        }

    }

    public static void setLogLevel(int level) {
        Log.setLevel(level, Log.getImpl() instanceof Xlog);
    }

    public static void appenderFlush(boolean isSync){
        Log.appenderFlush(isSync);
    }

    public static void appenderClose(){
        Log.appenderClose();
    }

    public static void setMaxFileSize(long size){
        Xlog.setMaxFileSize(size);
    }

    public static void setAppenderMode(int mode){
        Xlog.setAppenderMode(mode);
    }



}
