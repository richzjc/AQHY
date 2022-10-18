package com.micker.core.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import com.micker.core.callback.ICreateViewInterface;

/**
 * Created by Spark on 2016/7/4 16:28.
 */
public class BaseDialogFragment extends DialogFragment implements ICreateViewInterface {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, getStyle());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setGravity(getGravity());
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return dialog;
    }

    public int getGravity() {
        return Gravity.CENTER;
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            getDialog().getWindow().setLayout(getDialogWidth(), getDialogHeight());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LayoutInflater mInflater = LayoutInflater.from(getContext());
        mInflater = mInflater.cloneInContext(getContext());
        View containerView = mInflater.inflate(doGetContentViewId(), container, false);
        doBefore(savedInstanceState);
        doInitSubViews(containerView);
        return containerView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        doInitData();
        doAfter();
    }


    public int getStyle() {
        return 0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialog);
        }
    }

    private DialogInterface.OnDismissListener onDismissListener;

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
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

    public WindowManager.LayoutParams getLayoutParams() {
        return getDialog().getWindow().getAttributes();
    }

    public int getDialogWidth() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    public int getDialogHeight() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }


    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            Context context = getContext();
            if (context instanceof Activity) {
                Activity activity = (Activity) context;
                if (!activity.isDestroyed() && !activity.isFinishing())
                    super.show(manager, tag);
            } else {
                super.show(manager, tag);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}