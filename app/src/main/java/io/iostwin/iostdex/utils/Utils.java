package io.iostwin.iostdex.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.StringRes;

import java.util.regex.Pattern;

public class Utils {
    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1500;
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }

    public static Activity getActivity(View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return (Activity) view.getRootView().getContext();
    }

    /**
     * 正则验证数字
     */
    private static final String REGEX_NUM = "^[0-9\\-]+$";

    public static boolean isNumeric(String src) {
        return Pattern.matches(REGEX_NUM, src);
    }

    public static void toast(@StringRes int stringRes) {
        Context context = ContextHolder.getContext();
        toast(context.getString(stringRes));
    }

    public static void toast(String string) {
        Context context = ContextHolder.getContext();
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

}
