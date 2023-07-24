package com.yxr.base.widget.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.yxr.base.R;
import com.yxr.base.util.DisplayUtil;


/**
 * loading弹框
 *
 * @author ciba
 * @date 2020/09/17
 */

public class DefaultLoadingDialog extends BaseDialog {

    private String loadingText;
    private int widthHeight;
    private TextView tvLoading;

    public DefaultLoadingDialog(Context context) {
        super(context);
    }

    @Override
    protected int contentView() {
        return R.layout.layout_default_loading;
    }

    @Override
    protected void initView() {
        widthHeight = DisplayUtil.dp2px(200);
        tvLoading = findViewById(R.id.tvLoading);

        refreshUi();
    }

    @Override
    protected int getWindowAnimations() {
        return R.style.dialogAlphaAnimal;
    }

    @Override
    protected int getGravity() {
        return Gravity.CENTER;
    }

    @Override
    protected int getWindowWidth() {
        return widthHeight;
    }

    @Override
    protected int getWindowHeight() {
        return widthHeight;
    }

    @Override
    protected void onChildDialogWindow(@NonNull Window window) {
        window.setDimAmount(0);
    }

    public void setLoadingText(String loadingText) {
        this.loadingText = loadingText;
        refreshUi();
    }

    private void refreshUi() {
        if (tvLoading != null) {
            tvLoading.setText(loadingText);
            if (TextUtils.isEmpty(loadingText)) {
                tvLoading.setVisibility(View.GONE);
            } else {
                tvLoading.setVisibility(View.VISIBLE);
            }
        }
    }
}
