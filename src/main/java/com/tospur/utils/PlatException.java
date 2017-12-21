package com.tospur.utils;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-3-17
 * Time: 下午4:19
 * To change this template use File | Settings | File Templates.
 */
public class PlatException extends Exception {

    /**
     * 异常代码
     */
    private String key;

    /**
     * 参数
     */
    private Object[] values;

    public Object[] getValue() {
        return values;
    }

    public String getKey() {
        return key;
    }

    public PlatException() {
        super();
    }

    public PlatException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public PlatException(String message, Throwable throwable, String key) {
        super(message, throwable);
        this.key = key;
    }

    public PlatException(String message) {
        super(message);
    }

    public PlatException(String message, String key) {
        super(message);
        this.key = key;
    }

    public PlatException(String message, String key, Object value) {
        super(message);
        this.key = key;
        this.values = new Object[]{value};
    }

    public PlatException(String message, String key, Object[] values) {
        super(message);
        this.key = key;
        this.values = values;
    }

    public PlatException(Throwable throwable) {
        super(throwable);
    }

    public PlatException(Throwable throwable, String key) {
        super(throwable);
        this.key = key;
    }

    /**
     * 公共打印异常方法
     *
     * @param t 需要打印的异常
     */
    public static void myPrintStackTrace(Throwable t) {
        t.printStackTrace();
    }
}