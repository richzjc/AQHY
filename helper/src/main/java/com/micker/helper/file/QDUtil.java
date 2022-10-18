package com.micker.helper.file;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.MeasureSpec;
import com.micker.helper.R;
import com.micker.helper.ResourceUtils;
import com.micker.helper.snack.MToastHelper;
import com.micker.helper.system.ScreenUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class QDUtil {

    public static Bitmap convertViewToBitmap(View v) {
        try {
            v.measure(MeasureSpec.makeMeasureSpec(ScreenUtils.getScreenWidth(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
            Bitmap screenshot = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Config.RGB_565);
            v.draw(new Canvas(screenshot));
            return screenshot;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String saveShareViewToDisk(Context context, View shareView) throws Exception {
        IOException e;

        Bitmap shareBitmap = convertViewToBitmap(shareView);
        if (shareBitmap == null) {
            return "";
        }

        if (!checkSDCardMounted()) {
            return "";
        }
        String name = "shareTextImage-" + System.currentTimeMillis() + ".jpg";
        File file = new File(getShareImageCache(context).getAbsolutePath(), name);
        if (!file.exists()) {
            FileOutputStream out = null;
            try {
                FileOutputStream out2 = new FileOutputStream(file);
                try {
                    shareBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out2);
                    out2.flush();
                    out2.close();
                    if (out2 != null) {
                        try {
                            out2.close();
                            out = out2;
                        } catch (Throwable e2) {
                            out = out2;
                        }
                    }
                } catch (IOException e3) {
                    e = e3;
                    out = out2;
                    try {
                        e.printStackTrace();
                        if (out != null) {
                            try {
                                out.close();
                            } catch (Throwable e22) {
                            }
                        }
                    } catch (Throwable th2) {
                        if (out != null) {
                            try {
                                out.close();
                            } catch (Throwable e222) {
                            }
                        }
                    }
                } catch (Throwable th3) {
                    out = out2;
                    if (out != null) {
                        out.close();
                    }
                }
            } catch (IOException e4) {
                e = e4;
                e.printStackTrace();
                return "";
            }
        }
        try {
            if (file.length() > 0)
                return file.getAbsolutePath();
            else
                return "";
        } catch (Exception e1) {
            e1.printStackTrace();
            return "";
        }
    }

    public static String realSave(Context context, Bitmap shareBitmap) {
        IOException e;
        if (!checkSDCardMounted()) {
            return "";
        }
        String name = "shareTextImage-" + System.currentTimeMillis() + ".jpg";
        File file = new File(getShareImageCache(context).getAbsolutePath(), name);
        if (!file.exists()) {
            FileOutputStream out = null;
            try {
                FileOutputStream out2 = new FileOutputStream(file);
                try {
                    shareBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out2);
                    out2.flush();
                    out2.close();
                    if (out2 != null) {
                        try {
                            out2.close();
                            out = out2;
                        } catch (Throwable e2) {
                            out = out2;
                        }
                    }
                } catch (IOException e3) {
                    e = e3;
                    out = out2;
                    try {
                        e.printStackTrace();
                        if (out != null) {
                            try {
                                out.close();
                            } catch (Throwable e22) {
                            }
                        }
                    } catch (Throwable th2) {
                        if (out != null) {
                            try {
                                out.close();
                            } catch (Throwable e222) {
                            }
                        }
                    }
                } catch (Throwable th3) {
                    out = out2;
                    if (out != null) {
                        out.close();
                    }
                }
            } catch (IOException e4) {
                e = e4;
                e.printStackTrace();
                return "";
            }
        }
        try {
            if(file.length() > 0)
                return file.getAbsolutePath();
            else
                return "";
        } catch (Exception e1) {
            e1.printStackTrace();
            return "";
        }
    }


    public static String saveShareViewToDisk(Context context, Bitmap shareBitmap) throws Exception {
        IOException e;

        if (!checkSDCardMounted()) {
            return "";
        }
        String name = "shareTextImage-" + System.currentTimeMillis() + ".jpg";
        File file = new File(getShareImageCache(context).getAbsolutePath(), name);
        if (!file.exists()) {
            FileOutputStream out = null;
            try {
                FileOutputStream out2 = new FileOutputStream(file);
                try {
                    shareBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out2);
                    out2.flush();
                    out2.close();
                    if (out2 != null) {
                        try {
                            out2.close();
                            out = out2;
                        } catch (Throwable e2) {
                            out = out2;
                        }
                    }
                } catch (IOException e3) {
                    e = e3;
                    out = out2;
                    try {
                        e.printStackTrace();
                        if (out != null) {
                            try {
                                out.close();
                            } catch (Throwable e22) {
                            }
                        }
                    } catch (Throwable th2) {
                        if (out != null) {
                            try {
                                out.close();
                            } catch (Throwable e222) {
                            }
                        }
                    }
                } catch (Throwable th3) {
                    out = out2;
                    if (out != null) {
                        out.close();
                    }
                }
            } catch (IOException e4) {
                e = e4;
                e.printStackTrace();
                return "";
            }
        }
        try {
            if (file.length() > 0)
                return file.getAbsolutePath();
            else
                return "";
        } catch (Exception e1) {
            e1.printStackTrace();
            return "";
        }
    }


    public static boolean hasSDCardMounted() {
        String state = Environment.getExternalStorageState();
        return state != null && state.equals("mounted");
    }

    public static boolean checkSDCardMounted() {
        boolean result = hasSDCardMounted();
        if (!result) {
            MToastHelper.showToast(ResourceUtils.getResStringFromId(R.string.helper_no_sd_card_tips));
        }
        return result;
    }

    public static File getShareImageCache(Context context) {
        File cacheDir = null;
        if (hasSDCardMounted()) {
            cacheDir = getExternalCacheDir(context);
        }
        if (cacheDir == null) {
            cacheDir = context.getCacheDir();
        }
        File shareImage = new File(cacheDir.getAbsolutePath(), "/shareImage");
        if (!shareImage.exists()) {
            shareImage.mkdirs();
        }
        return new File(cacheDir, "/shareImage");
    }

    @TargetApi(8)
    public static File getExternalCacheDir(Context context) {
        return new File(Environment.getExternalStorageDirectory().getPath() + ("/Android/data/" + context.getPackageName() + "/cache/"));
    }

    public static Uri getImageContentUri(Context context, String filePath) {
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        Uri uri = null;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
                Uri baseUri = Uri.parse("content://media/external/images/media");
                uri = Uri.withAppendedPath(baseUri, "" + id);
            }

            cursor.close();
        }

        if (uri == null) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, filePath);
            uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        }

        return uri;
    }
}
