package com.micker.helper.router;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.kronos.router.Router;
import com.kronos.router.model.HostParams;
import com.kronos.router.model.RouterOptions;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.TintContextWrapper;
import com.micker.helper.ReflectionUtils;
import com.micker.helper.UtilsContextManager;

/**
 * Created by Leif Zhang on 16/8/1.
 * Email leifzhanggithub@gmail.com
 */
public class RouterHelper {
    public static final String PDF_URL = "wscn://wallstreetcn.com/pdf";
    public static final String EXCEL_URL = "wscn://wallstreetcn.com/excel";

    public static void open(String url) {
        try {
            Router.sharedRouter().open(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void open(String url, Context context) {
        open(url, context, null);
    }

    public static void open(String sourceUrl, Context context, Bundle bundle) {
        if (TextUtils.isEmpty(sourceUrl)) {
            return;
        }
        String url = sourceUrl.trim();
        url = url.replaceFirst("ivanka.wallstreetcn.com", "wallstreetcn.com");
        try {
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putString("url", sourceUrl);
            String suffix = getSuffix(url).toLowerCase();
            if (".pdf".equalsIgnoreCase(suffix)) {
                url = PDF_URL;
            }
            if (TextUtils.equals(".xlsx", suffix) || TextUtils.equals(".xls", suffix)
                    || TextUtils.equals(".doc", suffix) || TextUtils.equals(".docx", suffix)) {
                url = EXCEL_URL;
            }
            if (DoubleClickHelper.doubleClickCheck()) {
                Activity activity = getActivity(context);
                if (activity != null) {
                    Router.sharedRouter().open(url, bundle, activity);
                } else {
                    Router.sharedRouter().open(url, bundle, context);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Class<? extends Activity> activity = UtilsContextManager.getInstance().getWebViewActivity();
            DoubleClickHelper.cleanDownTime();
            if (activity != null) {
                if (bundle == null)
                    bundle = new Bundle();
                bundle.putString("url", sourceUrl);
                Activity mActivity = getActivity(context);
                if (mActivity != null) {
                    ActivityHelper.startActivity(mActivity, activity, bundle);
                } else {
                    ActivityHelper.startActivity(UtilsContextManager.getInstance().getApplication(),
                            activity, bundle);
                }
            }
        }
    }

    public static boolean openWithReturn(String url, Context context) {
        return openWithReturn(url, context, null);
    }

    public static boolean openWithReturn(String sourceUrl, Context context, Bundle bundle) {
        if (TextUtils.isEmpty(sourceUrl)) {
            return false;
        }
        String url = sourceUrl;
        url = url.replaceFirst("ivanka.wallstreetcn.com", "wallstreetcn.com");
        // url = url.replaceFirst("(\\w+\\.)*wallstreetcn\\.com", "wallstreetcn.com");
        try {
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putString("url", url);
            //  if (DoubleClickHelper.doubleClickCheck()) {
            Router.sharedRouter().open(url, bundle, getActivity(context));
            return true;
            //  }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Intent getIntentFromUrl(String sourceUrl, Context context) {
        try {
            String url = sourceUrl;
            url = url.replaceFirst("ivanka.wallstreetcn.com", "wallstreetcn.com");
            // url = url.replaceFirst("(\\w+\\.)*wallstreetcn\\.com", "wallstreetcn.com");
            return Router.sharedRouter().intentFor(context, url);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Activity getActivity(Context context) {
        if (context instanceof Application) {
            return null;
        }
        if (context instanceof Activity) {
            return (Activity) context;
        }
        if (context instanceof TintContextWrapper) {
            return getActivity(((TintContextWrapper) (context)).getBaseContext());
        }
        if (context instanceof ContextThemeWrapper) {
            return getActivity(((ContextThemeWrapper) (context)).getBaseContext());
        }

        if (context instanceof android.view.ContextThemeWrapper) {
            return getActivity(((android.view.ContextThemeWrapper) context).getBaseContext());
        }

        return null;
    }

    public static String getSuffix(String url) {
        try {
            url = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException var6) {
            var6.printStackTrace();
        }
        String suffixes = "avi|mpeg|3gp|mp3|mp4|wav|jpeg|gif|jpg|png|apk|exe|txt|html|zip|java|doc|docx|PDF|pdf|xls|xlsx";
        Pattern pat = Pattern.compile("[\\w]+[\\.](" + suffixes + ")");
        Matcher mc = pat.matcher(url);
        String suffix = null;
        while (mc.find()) {
            suffix = mc.group();//截取文件名后缀名
        }
        if (!TextUtils.isEmpty(suffix)) {
            suffix = suffix.substring(suffix.indexOf("."));
        } else {
            suffix = ".temp";
        }

        return suffix;
    }


    public static void printRouter() {
        Router router = Router.sharedRouter();
        Map<String, HostParams> hosts = (Map<String, HostParams>) ReflectionUtils.getFieldValue(router, "hosts");
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, HostParams> entry : hosts.entrySet()) {
            sb.append(entry.getKey()).append("\n");
            HostParams params = entry.getValue();
            Map<String, RouterOptions> optionMap = params.getRoutes();
            for (Map.Entry<String, RouterOptions> optionEntry : optionMap.entrySet()) {
                sb.append(optionEntry.getKey()).append(" : ");
                Class mClass = optionEntry.getValue().getOpenClass();
                if (mClass != null) {
                    sb.append(optionEntry.getValue().getOpenClass().getName());
                } else {
                    sb.append(optionEntry.getValue().getCallback().toString());
                }
                sb.append("\n");
            }
            sb.append("\n").append("\n");
        }
        Log.i("Router", String.valueOf(sb));

    }
}
