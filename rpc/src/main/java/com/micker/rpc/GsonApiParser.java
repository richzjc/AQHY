package com.micker.rpc;

import com.alibaba.fastjson.JSON;
import com.kronos.volley.ParseError;
import com.kronos.volley.toolbox.BaseApiParser;

/**
 * Created by zhangyang on 16/1/28.
 */
public class GsonApiParser implements BaseApiParser {
    private Class mClass;

    public GsonApiParser(Class mClass) {
        this.mClass = mClass;
    }

    @Override
    public Object parse(String content) throws ParseError {
        return JSON.parseObject(content, mClass);
    }
}
