package com.micker.global.api.realApi;

import com.micker.data.constant.ServerApiKt;
import com.micker.global.user.AccountManager;
import com.micker.rpc.CustomJsonApi;
import com.micker.rpc.ResponseListener;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Leif Zhang on 2016/12/13.
 * Email leifzhanggithub@gmail.com
 */

public class UploadTokenApi extends CustomJsonApi<String> {

    public UploadTokenApi(ResponseListener<String> responseListener) {
        super(responseListener);
    }

    @Override
    public String getUrl() {
        return ServerApiKt.BASE_URL + "user/qiniu-upload-token";
    }

    @Override
    public JSONObject getRequestJSONBody() {
        JSONObject jobj = new JSONObject();
        try {
            jobj.put("userId", AccountManager.INSTANCE.getAccountUserId());
            jobj.put("token", AccountManager.INSTANCE.getAccountToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jobj;
    }
}
