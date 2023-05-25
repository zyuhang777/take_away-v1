package com.zyh.spring.take_away.util;

/**
 * @ Author:张宇航是个大帅哥
 * @ 求求了别逗我笑
 */
public class ThreadLocalContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static Long get() {
        return threadLocal.get();
    }

    public static void set(Long id) {
        threadLocal.set(id);
    }
}
