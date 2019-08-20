package io.iostwin.iostdex.common;

import android.text.InputFilter;
import android.widget.EditText;

public class EditTextFormat {

    /**
     * 添加新的Filter
     */
    public static void addFilter(EditText view, InputFilter filter) {
        InputFilter[] old = view.getFilters();
        InputFilter[] filters = new InputFilter[old.length + 1];
        int position = 0;
        for (; position < old.length; position++) {
            filters[position] = old[position];
        }
        filters[position] = filter;
        view.setFilters(filters);
    }

    /**
     * 设置小数位数控制
     */
    public static InputFilter lengthFilter(final int decimalDigits) {
        return (source, start, end, dest, dStart, dEnd) -> {
            // ""等特殊字符，直接返回
            if ("".equals(source.toString())) {
                return null;
            }
            if (dest.toString().contains(".") && source.toString().indexOf(".") == source.length() - 1) {
                return source.subSequence(start, end - 1);
            }
            String dValue = dest.toString();
            String[] splitArray = dValue.split("\\.");
            if (splitArray.length > 1) {
                String dotValue = splitArray[1];
                int diff = dotValue.length() + 1 - decimalDigits;
                if (diff > 0) {
                    return source.subSequence(start, end - diff);
                }
            }
            return null;
        };
    }
}
