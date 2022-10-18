package com.micker.rpc.manager;

import com.kronos.volley.VolleyError;
import com.kronos.volley.toolbox.FileUploadRequest;
import com.kronos.volley.toolbox.OkHttpStack;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import java.io.File;

/**
 * Created by Leif Zhang on 2016/12/13.
 * Email leifzhanggithub@gmail.com
 */

public class WscnOkHttpStack extends OkHttpStack {

    public WscnOkHttpStack(OkHttpClient client) {
        super(client);
    }

    @Override
    protected RequestBody createFileRequestBody(FileUploadRequest r) throws VolleyError {
        String fileName = new String(r.getBody());
        File file = new File(fileName);
        return new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("key", SevenBullConstants.QiniuKey)
                .addFormDataPart("token", SevenBullConstants.QiniuToken)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(MediaType.parse(r.getMediaType()), new File(fileName)))
                .build();
    }
}
