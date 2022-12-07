package com.yxr.base.widget;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;

public class DefaultAnimLoadingView extends LinearLayout {
    private LottieAnimationView lottieAnimationView;
    private TextView textView;

    public DefaultAnimLoadingView(Context context) {
        super(context);
        initView();
    }

    public DefaultAnimLoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DefaultAnimLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        lottieAnimationView = new LottieAnimationView(getContext());
        lottieAnimationView.setAnimation("refresh_loading.json");
        lottieAnimationView.setRepeatCount(ValueAnimator.INFINITE);

        int wh = (int) (getResources().getDisplayMetrics().density * 96);
        LayoutParams params = new LayoutParams(wh, wh);
        addView(lottieAnimationView, params);

        textView = new TextView(getContext());
        textView.setTextSize(14);
        textView.setTextColor(0xff999999);
        textView.setGravity(Gravity.CENTER);
        textView.setText("loading...");
        LayoutParams textParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textParams.leftMargin = wh / 2;
        textParams.rightMargin = wh / 2;
        textParams.topMargin = wh / 4;
        addView(textView, textParams);
    }

    public void startLoading() {
        if (!lottieAnimationView.isAnimating()) {
            lottieAnimationView.playAnimation();
        }
    }

    public void pauseLoading() {
        if (lottieAnimationView.isAnimating()) {
            lottieAnimationView.pauseAnimation();
        }
    }

    public void cancelLoading() {
        lottieAnimationView.cancelAnimation();
    }

    public void setLoadingText(String text) {
        textView.setText(TextUtils.isEmpty(text) ? "loading..." : text);
    }
}
