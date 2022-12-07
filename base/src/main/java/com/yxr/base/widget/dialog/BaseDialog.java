package com.yxr.base.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;

import com.yxr.base.R;


/**
 * @author ciba
 * @description 描述
 * @date 2020/09/17
 */
public abstract class BaseDialog extends Dialog {
    public BaseDialog(Context context) {
        super(context, R.style.common_dialog);
        setContentView(contentView());
        initView();
        initListener();
        initData();
        setDialogWindow();
    }

    protected void initView() {

    }

    protected void initListener() {

    }

    protected void initData() {

    }

    protected void setDialogWindow() {
        Window window = getWindow();
        if (window != null) {
            window.setWindowAnimations(getWindowAnimations());
            window.setGravity(getGravity());
            ViewGroup.LayoutParams lay = window.getAttributes();
            lay.width = getWindowWidth();
            lay.height = getWindowHeight();
            onChildDialogWindow(window);
        }
    }

    protected int getWindowWidth() {
        return ViewGroup.LayoutParams.MATCH_PARENT;
    }

    protected int getWindowHeight() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    protected int getWindowAnimations() {
        return R.style.fromBottomToTopAnimStyle;
    }

    protected int getGravity() {
        return Gravity.BOTTOM;
    }

    protected void onChildDialogWindow(@NonNull Window window) {

    }

    protected abstract int contentView();
}
