package com.yxr.base.widget.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.Window;

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

    private int widthHeight;

    public DefaultLoadingDialog(Context context) {
        super(context);
    }

    @Override
    protected int contentView() {
        return R.layout.layout_default_loading;
    }

    @Override
    protected void initView() {
        widthHeight = DisplayUtil.dp2px(72);
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
}
