package io.iostwin.iostdex.common

import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import io.iostwin.iostdex.BuildConfig
import io.iostwin.iostdex.R
import io.iostwin.iostdex.utils.ConverterUtil
import io.iostwin.iostdex.utils.Utils


class BindingAdapters {

    companion object {


        /**
         * 为ImageView设置图片
         *
         * @param imageView    imageView
         * @param src         路径
         * @param defaultImage 默认图片
         * @param errorImage   加载失败图片
         */
        @BindingAdapter(value = ["android:src", "defaultImage", "errorImage"], requireAll = false)
        @JvmStatic
        fun setImage(
            imageView: ImageView,
            src: String?,
            defaultImage: Drawable?,
            errorImage: Drawable?
        ) {
            val context = imageView.rootView.context
            var errorImageFun = errorImage
            if (null == errorImageFun) {
                errorImageFun = ContextCompat.getDrawable(context, R.drawable.ic_default)
            }
            if (TextUtils.isEmpty(src)) {
                if (defaultImage != null) {
                    imageView.setImageDrawable(defaultImage)
                } else {
                    imageView.setImageDrawable(errorImageFun)
                }
            } else {
                var myOptions = RequestOptions().error(errorImageFun)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                if (defaultImage != null) {
                    myOptions = myOptions.placeholder(defaultImage)
                }
                if (Utils.isNumeric(src)) {
                    Glide.with(context).load(ConverterUtil.getInteger(src)).thumbnail(0.1f)
                        .apply(myOptions).into(imageView)
                } else {
                    Glide.with(context).load(src!!.run {
                        if (this.startsWith("http")) this else BuildConfig.RES_URL + src
                    }).thumbnail(0.1f).apply(myOptions).into(imageView)
                }
            }
        }

        @BindingAdapter("android:visibility")
        @JvmStatic
        fun viewVisibility(view: View, visible: Boolean) {
            if (visible) {
                view.visibility = View.VISIBLE
            } else {
                view.visibility = View.GONE
            }
        }

        /**
         * 限制EditText只能输入两位小数
         */
        @BindingAdapter("filterLength")
        @JvmStatic
        fun editTextFilter(view: EditText, type: Int) {
            EditTextFormat.addFilter(view, EditTextFormat.lengthFilter(type))
        }
    }
}