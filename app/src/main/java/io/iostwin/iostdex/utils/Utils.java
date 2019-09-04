package io.iostwin.iostdex.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.StringRes;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class Utils {

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

    public static String calculateAmount(String priceStr, String numStr) {
        if (TextUtils.isEmpty(priceStr))
            priceStr = "0";
        if (TextUtils.isEmpty(numStr))
            numStr = "0";
        BigDecimal price = new BigDecimal(priceStr);
        BigDecimal num = new BigDecimal(numStr);
        return price.multiply(num).setScale(8, BigDecimal.ROUND_HALF_DOWN).toPlainString();
    }
}
