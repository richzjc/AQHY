package com.micker.rpc;

import com.kronos.volley.*;
import com.kronos.volley.toolbox.HttpHeaderParser;
import com.kronos.volley.toolbox.NetResponse;
import com.kronos.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Map;

/**
 * Created by zhangyang on 16/6/21.
 */
public class RpcStringRequest extends StringRequest {
    private String cacheExtra = "";
    public Response.Listener<NetResponse> requestListener;
    private Map<String, String> map;

    public void setCacheExtra(String cacheExtra) {
        this.cacheExtra = cacheExtra;
    }

    public RpcStringRequest(String url) {
        super(url);
    }

    @Override
    public Request<NetResponse> setHeader(Map<String, String> map) {
        if (map != null) {
            this.map = map;
        }
        return this;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (map == null)
            return Collections.emptyMap();
        else return map;
    }


    @Override
    public Request setRequestListener(Response.Listener<NetResponse> mListener) {
        this.requestListener = mListener;
        return super.setRequestListener(mListener);
    }

    @Override
    public String getCacheKey() {
        if (requestBody != null) {
            return String.format("%s_%s_%s", getUrl(), requestBody.toString(), cacheExtra);
        } else {
            return String.format("%s_%s", getUrl(), cacheExtra);
        }
    }

    private Map<String, String> requestBody;

    @Override
    public StringRequest setRequestBody(Map<String, String> requestBody) {
        this.requestBody = requestBody;
        return super.setRequestBody(requestBody);
    }

    @Override
    protected Response<NetResponse> parseNetworkResponse(NetworkResponse response) throws ParseError {
        NetResponse netResponse;
        try {
            String parsed = new String(response.data, "UTF-8");
            JSONObject jsonObject = new JSONObject(parsed);
            if (jsonObject.has("code")) {
                int code = jsonObject.optInt("code");
                if (code != 20000) {
                    throw new ParseError(new NetworkResponse(code,
                            response.data, response.headers, response.notModified));
                } else {
                    parsed = jsonObject.optString("data");
                }
            }
            try {
                Object o = getApiParser().parse(parsed);
                netResponse = new NetResponse(response.isCache, o);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ParseError(response);
            }
        } catch (UnsupportedEncodingException | JSONException e) {
            e.printStackTrace();
            throw new ParseError(response);
        }
        return Response.success(netResponse, HttpHeaderParser.parseCacheHeaders(response));
    }
}
