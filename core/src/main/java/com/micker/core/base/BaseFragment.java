package com.micker.core.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.micker.core.callback.ICreateViewInterface;
import com.micker.core.internal.ViewQuery;

/**
 * Created by micker on 16/6/16.
 */
public class BaseFragment<V, T extends BasePresenter<V>> extends Fragment implements ICreateViewInterface, View.OnClickListener{

    protected ViewQuery mViewQuery = new ViewQuery();
    protected T mPresenter;
    private boolean isLoadData = false;
    private String TAG = "BaseFragment";

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lazyLoadAction();
        doAfter();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View containerView = LayoutInflater.from(getActivity()).inflate(doGetContentViewId(), container, false);
        doInitPresenter();
        attachToPresenter();
        doBefore(savedInstanceState);
        __internal(containerView);
        doInitSubViews(containerView);
        return containerView;
    }

    private void doInitPresenter() {
        if (mPresenter == null) {
            mPresenter = doGetPresenter();
        }
    }

    protected T doGetPresenter() {
        return null;
    }

    private void __internal(View view) {
        mViewQuery.setView(view).setClickListener(this);

    }

    @Override
    public void doBefore(Bundle savedInstanceState) {

    }

    @Override
    public ViewGroup doViewGroupRoot() {
        return null;
    }

    @Override
    public int doGetContentViewId() {
        return 0;
    }

    @Override
    public void doInitSubViews(View view) {

    }

    @Override
    public void doInitData() {

    }

    @Override
    public void doAfter() {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onResume() {
        super.onResume();
        notifyStateChange();
    }

    @Override
    public void onPause() {
        super.onPause();
        notifyStateChange();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        detachToPresenter();
        isLoadData = false;
    }

    protected boolean isVisibleToUser = true;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        this.isVisibleToUser = isVisibleToUser;
        if (isVisibleToUser) {
            lazyLoadAction();
        } else if (mPresenter != null) {
            dismiss();
        }
        notifyStateChange();
    }

    private void lazyLoadAction() {
        if (!isLoadData) {
            if (isAdded() && isVisibleToUser) {
                doInitPresenter();
                doInitData();
                isLoadData = true;
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        notifyStateChange();
    }

    private boolean islec_VisibleToUser = false;

    public void notifyStateChange() {
        boolean visible = le_visibleToUser();
        if (visible == islec_VisibleToUser) return;
        islec_VisibleToUser = visible;
        visibleChange(islec_VisibleToUser);
    }

    protected void visibleChange(boolean visible) {

    }

    public boolean le_visibleToUser() {
        boolean parentVisible = true;
        if (getParentFragment() instanceof BaseFragment) {
            parentVisible = ((BaseFragment) getParentFragment()).le_visibleToUser();
        }
        return parentVisible && isResumed() && getUserVisibleHint() && !isHidden();
    }

    public void dismiss() {

    }


    protected void attachToPresenter() {
        if (null != mPresenter) {
            if (!mPresenter.isViewRefAttached()) {
                mPresenter.attachViewRef((V) this);
            }
        }
    }

    protected void detachToPresenter() {
        if (null != mPresenter) {
            if (mPresenter.isViewRefAttached()) {
                mPresenter.detachViewRef();
            }
        }
    }

    public void onEmptyClick() {

    }

    public boolean onBackPressed() {
        return false;
    }
}
