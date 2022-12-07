package com.yxr.base.widget.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.yxr.base.R;

public class CancelConfirmDialog extends BaseDialog implements View.OnClickListener {
    private TextView tvTitle, tvCancel, tvConfirm;
    private CancelConfirmListener cancelConfirmListener;

    public CancelConfirmDialog(Context context) {
        super(context);
    }

    @Override
    protected int contentView() {
        return R.layout.layout_dialog_cancel_confirm;
    }

    @Override
    protected void initView() {
        tvTitle = findViewById(R.id.tvTitle);
        tvCancel = findViewById(R.id.tvCancel);
        tvConfirm = findViewById(R.id.tvConfirm);
    }

    @Override
    protected void initListener() {
        tvCancel.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        dismiss();
        if (cancelConfirmListener != null) {
            if (tvCancel == v) {
                cancelConfirmListener.onCancel();
            } else if (tvConfirm == v) {
                cancelConfirmListener.onConfirm();
            }
        }
    }

    @Override
    protected void setDialogWindow() {
        Window window = getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.dialogAlphaAnimal);
            window.setGravity(Gravity.CENTER);
            ViewGroup.LayoutParams lay = window.getAttributes();
            lay.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
    }

    public CancelConfirmDialog setContent(CharSequence content) {
        tvTitle.setText(content);
        return this;
    }

    public CancelConfirmDialog setCancelText(CharSequence cancelText) {
        tvCancel.setText(cancelText);
        return this;
    }

    public CancelConfirmDialog setConfirmText(CharSequence confirmText) {
        tvConfirm.setText(confirmText);
        return this;
    }

    public CancelConfirmDialog setCancelConfirmListener(CancelConfirmListener cancelConfirmListener) {
        this.cancelConfirmListener = cancelConfirmListener;
        return this;
    }

    public static class SimpleCancelConfirmListener implements CancelConfirmListener {
        @Override
        public void onCancel() {

        }

        @Override
        public void onConfirm() {

        }
    }

    public interface CancelConfirmListener {
        void onCancel();

        void onConfirm();
    }
}
