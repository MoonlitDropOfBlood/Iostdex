package io.iostwin.iostdex.common;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.github.fujianlian.klinechart.utils.DateUtil;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.iostwin.iostdex.BuildConfig;
import io.iostwin.iostdex.R;
import io.iostwin.iostdex.utils.ConverterUtil;
import io.iostwin.iostdex.utils.Utils;

public class BindingAdapters {
    /**
     * 为ImageView设置图片
     *
     * @param imageView    imageView
     * @param src          路径
     * @param defaultImage 默认图片
     * @param errorImage   加载失败图片
     */
    @BindingAdapter(value = {"android:src", "defaultImage", "errorImage"}, requireAll = false)
    public static void setImage(ImageView imageView, String src, Drawable defaultImage, Drawable errorImage) {
        Context context = imageView.getContext();
        if (null == errorImage) {
            errorImage = ContextCompat.getDrawable(context, R.drawable.ic_default);
        }
        if (TextUtils.isEmpty(src)) {
            if (defaultImage != null) {
                imageView.setImageDrawable(defaultImage);
            } else {
                imageView.setImageDrawable(errorImage);
            }
        } else {
            RequestOptions myOptions = RequestOptions.circleCropTransform().error(errorImage)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
            if (defaultImage != null) {
                myOptions = myOptions.placeholder(defaultImage);
            }
            if (Utils.isNumeric(src)) {
                Glide.with(context).load(ConverterUtil.getInteger(src)).thumbnail(0.1f)
                        .apply(myOptions).into(imageView);
            } else {
                if (!src.startsWith("http")) {
                    src = BuildConfig.RES_URL + src;
                }
                Glide.with(context).load(src).thumbnail(0.1f).apply(myOptions).into(imageView);
            }
        }
    }

    @BindingAdapter("android:visibility")
    public static void viewVisibility(View view, boolean visible) {
        if (visible) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    /**
     * 限制EditText只能输入两位小数
     */
    @BindingAdapter("filterLength")
    public static void editTextFilter(EditText view, int type) {
        EditTextFormat.addFilter(view, EditTextFormat.lengthFilter(type));
    }

    @BindingAdapter("order_status")
    public static void orderStatus(TextView view, int status) {
        String[] array = view.getContext().getResources().getStringArray(R.array.order_status);
        view.setText(array[status + 2]);
    }

    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @BindingAdapter("text_time")
    public static void textTime(TextView view, long time) {
        view.setText(simpleDateFormat.format(new Date(time * 1000L)));
    }

    @BindingAdapter("progress")
    public static void setProgress(ProgressBar view, int progress) {
        view.setProgress(progress, false);
        view.setSecondaryProgress(progress);
    }

    @BindingAdapter("android:textTime")
    public static void textTime(TextView view, int time) {
        view.setText(DateUtil.longTimeFormat.format(new Date((long) time * 1000L)));
    }

    @BindingAdapter("progress")
    public static void progressSeekBar(IndicatorSeekBar view, int progress) {
        if (progress != view.getProgress())
            view.setProgress(progress);
    }

    @InverseBindingAdapter(attribute = "progress", event = "progressAttrChanged")
    public static int getProgress(IndicatorSeekBar view) {
        return view.getProgress();
    }

    @BindingAdapter(value = "progressAttrChanged")
    public static void setChangeListener(IndicatorSeekBar view, InverseBindingListener listener) {
        view.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                listener.onChange();
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

            }
        });
    }
}
