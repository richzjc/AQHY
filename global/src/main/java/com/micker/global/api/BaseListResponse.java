package com.micker.global.api;


import com.micker.core.callback.BaseRecyclerViewCallBack;
import com.micker.rpc.ResponseListener;

/**
 * Created by zhangyang on 16/7/11.
 */
public class BaseListResponse<T extends BaseListModel> implements ResponseListener<T> {

    protected T listEntity;
    protected String TAG = "BaseListResponse";
    protected BaseRecyclerViewCallBack baseRecyclerViewCallBack;

    protected boolean isRefresh;

    public BaseListResponse(T listEntity) {
        this(listEntity, null);
    }

    public BaseListResponse(T listEntity, BaseRecyclerViewCallBack baseRecyclerViewCallBack) {
        this.listEntity = listEntity;
        this.baseRecyclerViewCallBack = baseRecyclerViewCallBack;
        isRefresh = listEntity.isRefresh();
    }


    @Override
    public void onSuccess(T model, boolean isCache) {
        checkIsEnd(model);
        if (isRefresh) {
            listEntity.clear();
            if (model != null) {
                listEntity.addList(model);
            }
            if (baseRecyclerViewCallBack != null) {
                baseRecyclerViewCallBack.setData(listEntity, isCache);
            }
        } else {
                listEntity.addList(model);
                if (baseRecyclerViewCallBack != null) {
                    baseRecyclerViewCallBack.notifyDataRangeChange();
            }
        }
        listEntity.updateCounter();
    }

    @Override
    public void onErrorResponse(int code, String error) {
        if (baseRecyclerViewCallBack != null)
            baseRecyclerViewCallBack.onResponseError(code);
    }

    protected void checkIsEnd(T model) {
        listEntity.checkIsEnd(model);
        if (baseRecyclerViewCallBack != null) {
            baseRecyclerViewCallBack.isListFinish(listEntity.isTouchEnd());
        }
    }
}
