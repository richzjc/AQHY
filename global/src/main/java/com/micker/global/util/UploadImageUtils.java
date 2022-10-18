package com.micker.global.util;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.micker.global.api.realApi.UploadTokenApi;
import com.micker.rpc.CustomFileApi;
import com.micker.rpc.ResponseListener;
import com.micker.rpc.manager.SevenBullConstants;
import org.json.JSONObject;

public class UploadImageUtils {

    public static void uploadImage(String filePath, ResponseListener qiniuListener, String mimeType) {
        UploadTokenApi uploadTokenApi = new UploadTokenApi(new ResponseListener<String>() {
            @Override
            public void onSuccess(String model, boolean isCache) {
                try {
                    JSONObject jsonObject = new JSONObject(model);
                    SevenBullConstants.QiniuKey = jsonObject.optString("key");
                    SevenBullConstants.QiniuToken = jsonObject.optString("token");
                    String uploadImageUrl = jsonObject.optString("imgUrl");
                    Bundle mBundle = new Bundle();
                    mBundle.putString("file", filePath);
                    mBundle.putString("mimeType", mimeType);
                    UploadImageApi api = new UploadImageApi(new ResponseListener<String>() {
                        @Override
                        public void onSuccess(String model, boolean isCache) {
                            if (qiniuListener != null) {
                                qiniuListener.onSuccess(uploadImageUrl, isCache);
                            }
                        }

                        @Override
                        public void onErrorResponse(int code, String error) {
                            if (qiniuListener != null) {
                                Log.i("question", "UploadImageApi onErrorResponse");
                                qiniuListener.onErrorResponse(code, error);
                            }
                        }
                    }, mBundle);
                    api.start();
                } catch (Exception e) {
                    Log.i("question", "Exception : " + e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(int code, String error) {
                if (qiniuListener != null) {
                    qiniuListener.onErrorResponse(code, error);
                    Log.i("question", "onErrorResponse");
                }
            }
        });
        uploadTokenApi.start();
    }

    static class UploadImageApi extends CustomFileApi<String> {
        private String fileName;

        public UploadImageApi(ResponseListener<String> responseListener, Bundle bundle) {
            super(responseListener, bundle);
            fileName = bundle.getString("file");
        }

        @Override
        public String getMediaType() {
            String mimeType = bundle.getString("mimeType", "");
            if(!TextUtils.isEmpty(mimeType)){
               return mimeType;
            }else{
                if (!TextUtils.isEmpty(fileName)) {
                    if (fileName.endsWith(".png"))
                        return "image/png";
                    if (fileName.endsWith(".gif"))
                        return "image/gif";
                }
                return "image/jpeg";
            }
        }

        @Override
        public String getFileName() {
            return fileName;
        }

        @Override
        public String getUrl() {
            return "http://upload.qiniu.com/";
        }

    }
}
