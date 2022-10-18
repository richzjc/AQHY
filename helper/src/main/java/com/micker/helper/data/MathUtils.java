package com.micker.helper.data;

import com.micker.helper.R;
import com.micker.helper.ResourceUtils;
import com.micker.helper.text.TextUtil;

/**
 * Created by  Leif Zhang on 2017/10/17.
 * Email leifzhanggithub@gmail.com
 */

public class MathUtils {
    public static String longUnitChange(long count) {
        if (count >= 100 * 1000 * 1000) {
            float hMillCount = count * 1F / (100 * 1000 * 1000);
            return TextUtil.format(ResourceUtils.getResStringFromId(R.string.helper_n_dot_billion), hMillCount);
        } else if (count >= 10 * 1000) {
            float millCount = count * 1F / (10 * 1000);
            return TextUtil.format(ResourceUtils.getResStringFromId(R.string.helper_n_dot_million), millCount);
        } else {
            return String.valueOf(count);
        }
    }

}
