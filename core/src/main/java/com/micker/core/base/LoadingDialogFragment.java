package com.micker.core.base;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import androidx.fragment.app.FragmentManager;
import com.micker.core.R;
import com.micker.core.widget.IOSLoadingView;
import com.micker.helper.system.ScreenUtils;

public class LoadingDialogFragment extends BaseDialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int style = R.style.dialog_loading;
        setStyle(STYLE_NO_TITLE, style);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Override
    public int doGetContentViewId() {
        return R.layout.base_dialog_loading;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (isAdded()) return;
        try {
            super.show(manager, tag);
            if(getView() != null) {
                IOSLoadingView splashView = getView().findViewById(R.id.splash);
                splashView.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dismiss() {
        super.onDestroy();
        super.dismiss();
        IOSLoadingView splashView = getView().findViewById(R.id.splash);
        splashView.setVisibility(View.GONE);
    }

    @Override
    public int getGravity() {
        return Gravity.CENTER;
    }

    @Override
    public int getDialogWidth() {
        return ScreenUtils.getScreenWidth();
    }

    @Override
    public int getDialogHeight() {
        return ScreenUtils.getScreenHeight();
    }
}

