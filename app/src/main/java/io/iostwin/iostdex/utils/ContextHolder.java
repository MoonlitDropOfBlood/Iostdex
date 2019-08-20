package io.iostwin.iostdex.utils;

import android.content.Context;

/**
 * application_context 工具类
 */
public class ContextHolder {
    private static Context APPLICATION_CONTEXT;

    /**
     * 初始化context
     */
    public static void init(Context context) {
        APPLICATION_CONTEXT = context.getApplicationContext();
    }

    /**
     * application context
     */
    public static Context getContext() {
        return APPLICATION_CONTEXT;
    }
}
