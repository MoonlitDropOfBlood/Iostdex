package io.iostwin.iostdex.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.LocaleList;
import android.text.TextUtils;

import java.util.Locale;

/**
 * Todo 多语言设置
 * 来自：https://blog.csdn.net/m0_38074457/article/details/84993366
 * 使用步骤：
 * 1、Application中onCreate添加registerActivityLifecycleCallbacks(MultiLanguageUtils.callbacks);
 *
 * @Override protected void attachBaseContext(Context base) {
 * //系统语言等设置发生改变时会调用此方法，需要要重置app语言
 * super.attachBaseContext(MultiLanguageUtils.attachBaseContext(base));
 * }
 * 2、改变应用语言调用MultiLanguageUtils.changeLanguage(activity,type,type);
 */
//public final static String SP_LANGUAGE="SP_LANGUAGE";
//public final static String SP_COUNTRY="SP_COUNTRY";
public class MultiLanguageUtils {
    /**
     * TODO 1、 修改应用内语言设置
     *
     * @param language 语言  zh/en
     * @param area     地区
     */
    public static void changeLanguage(Context context, String language, String area) {
        if (TextUtils.isEmpty(language) && TextUtils.isEmpty(area)) {
            //如果语言和地区都是空，那么跟随系统s
            SPUtils.put(context, Constants.SP_LANGUAGE, "");
            SPUtils.put(context, Constants.SP_COUNTRY, "");
        } else {
            //不为空，那么修改app语言，并true是把语言信息保存到sp中，false是不保存到sp中
            Locale newLocale = new Locale(language, area);
            setAppLanguage(context, newLocale);
            saveLanguageSetting(context, newLocale);
        }
    }

    /**
     * Todo 更新应用语言
     *
     * @param context
     * @param locale
     */
    private static Context setAppLanguage(Context context, Locale locale) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        configuration.setLocales(new LocaleList(locale));
        return context.createConfigurationContext(configuration);
    }

    /**
     * TODO 3、 跟随系统语言
     */
    public static Context attachBaseContext(Context context) {
        String spLanguage = (String) SPUtils.get(context, Constants.SP_LANGUAGE, "");
        String spCountry = (String) SPUtils.get(context, Constants.SP_COUNTRY, "");
        if (!TextUtils.isEmpty(spLanguage) && !TextUtils.isEmpty(spCountry)) {
            Locale locale = new Locale(spLanguage, spCountry);
            context = setAppLanguage(context, locale);
        }
        return context;
    }

    /**
     * 判断sp中和app中的多语言信息是否相同
     */
    public static boolean isSameWithSetting(Context context) {
        Locale locale = getAppLocale(context);
        String language = locale.getLanguage();
        String country = locale.getCountry();
        String sp_language = (String) SPUtils.get(context, Constants.SP_LANGUAGE, "");
        String sp_country = (String) SPUtils.get(context, Constants.SP_COUNTRY, "");
        return language.equals(sp_language) && country.equals(sp_country);
    }

    /**
     * 保存多语言信息到sp中
     */
    public static void saveLanguageSetting(Context context, Locale locale) {
        SPUtils.put(context, Constants.SP_LANGUAGE, locale.getLanguage());
        SPUtils.put(context, Constants.SP_COUNTRY, locale.getCountry());
    }

    /**
     * 获取应用语言
     */
    public static Locale getAppLocale(Context context) {
        return context.getResources().getConfiguration().getLocales().get(0);
    }
}