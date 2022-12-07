package com.yxr.base.widget.refresh;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;

public class DefaultRefreshHeader extends RelativeLayout implements RefreshHeader {
    private LottieAnimationView lottieAnimationView;

    public DefaultRefreshHeader(Context context) {
        super(context);
        initView();
    }

    public DefaultRefreshHeader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DefaultRefreshHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        lottieAnimationView = new LottieAnimationView(getContext());
        lottieAnimationView.setAnimation("refresh_loading.json");
        lottieAnimationView.setRepeatCount(ValueAnimator.INFINITE);

        int height = (int) (getResources().getDisplayMetrics().density * 64);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, height);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        addView(lottieAnimationView, params);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (lottieAnimationView != null) {
            lottieAnimationView.cancelAnimation();
        }
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }

    @Override
    public void setPrimaryColors(int... colors) {

    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {

    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {

    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        return 0;
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        if (RefreshState.PullDownToRefresh == newState) {
            startAnim();
        } else if (RefreshState.RefreshFinish == newState) {
            pauseAnim();
        }
    }

    private void pauseAnim() {
        if (lottieAnimationView != null && lottieAnimationView.isAnimating()) {
            lottieAnimationView.pauseAnimation();
        }
    }

    private void startAnim() {
        if (lottieAnimationView != null && !lottieAnimationView.isAnimating()) {
            lottieAnimationView.playAnimation();
        }
    }
}
