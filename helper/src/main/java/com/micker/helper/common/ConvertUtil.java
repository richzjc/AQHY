package com.micker.helper.common;

import android.text.TextUtils;
import com.micker.helper.R;
import com.micker.helper.ResourceUtils;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by wscn on 17/2/13.
 */

public class ConvertUtil {

    public static String convertTwoPoint(double value) {
        DecimalFormat format = new DecimalFormat("#0.00");
        return format.format(value);
    }
    public static String convertOnePoint(double value) {
        DecimalFormat format = new DecimalFormat("#0.0");
        return format.format(value);
    }

    public static String unitChange(int count) {
        if (count > 10 * 1000 * 1000) {
            float newCount = count * 1f / (10 * 1000 * 1000);
            return ConvertUtil.convertOnePoint(newCount) + ResourceUtils.getResStringFromId(R.string.helper_hundreds_of_millions);
        } else if (count > 10 * 1000) {
            float newCount = count * 1f / (10 * 1000);
            return ConvertUtil.convertOnePoint(newCount) + ResourceUtils.getResStringFromId(R.string.helper_ten_thousand);
        }
        return String.valueOf(count);
    }

    public static String convertF2Y(double fen) {
        DecimalFormat format = new DecimalFormat("#0.00");
        BigDecimal fenDecimal = new BigDecimal(fen);
        BigDecimal rateDecimal = new BigDecimal(100);
        double value = fenDecimal.divide(rateDecimal).doubleValue();
        return format.format(value);
    }

    public static String convertF2Y(String fen) {
        try {
            long fenValue = Long.parseLong(fen);
            return convertF2Y(fenValue);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "0.00";
        }
    }

    public static String convertF2YWithoutPoint(int fen) {
        DecimalFormat format = new DecimalFormat("#0.00");
        BigDecimal fenDecimal = new BigDecimal(fen);
        BigDecimal rateDecimal = new BigDecimal(100);
        double value = fenDecimal.divide(rateDecimal).doubleValue();
        int intValue = (int) value;
        if (value > intValue)
            return format.format(value);
        else
            return String.valueOf(intValue);
    }

    public static String convertF2YWithoutPoint(String fen) {
        try {
            int fenValue = Integer.parseInt(fen);
            return convertF2YWithoutPoint(fenValue);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "0";
        }
    }


    /**
     * Pro版 特辑 特辑绑定实物 文章 是可以98折  其他都不打折吧
     *
     * @param fen
     * @return
     */
    public static String disCount(String fen) {
        try {
            Double fenValue = Double.parseDouble(fen);
            return convertF2Y(fenValue);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "0.00";
        }
    }

    public static String disCount(double fenValue) {
        return convertF2Y(fenValue);
    }

    public static String format(int count, double price) {
        String format = "0.";
        count = count == 0 ? 2 : count;
        for (int i = 0; i < count; i++) {
            format += "0";
        }
        DecimalFormat dfs = new DecimalFormat(format);
        return dfs.format(price);
    }

    public static String formatTwoPoint(float price){
        return formatTwoPoint(String.valueOf(price));
    }

    public static String formatTwoPoint(String price){
        if(TextUtils.isEmpty(price))
            return price;
        int index = price.indexOf(".");
        if(index < 0){
            return price + ".00";
        }else{
            int length = price.length();
            if((length - 1) == index){
                return price + "00";
            }else if((length -1) == (index + 1)){
                return price + "0";
            }else{
                return price;
            }
        }
    }
}
