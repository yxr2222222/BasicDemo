package com.yxr.base.widget.guideview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.FragmentActivity;

/**
 * 遮罩系统的封装 <br>
 * * 外部需要调用{@link GuideBuilder}来创建该实例，实例创建后调用
 * <p>
 * Created by binIoter
 */

public class Guide implements View.OnTouchListener {
    /**
     * 滑动临界值
     */
    private static final int SLIDE_THRESHOLD = 30;
    private Configuration mConfiguration;
    private MaskView mMaskView;
    private Component[] mComponents;
    private GuideBuilder.OnVisibilityChangedListener mOnVisibilityChangedListener;
    private GuideBuilder.OnSlideListener mOnSlideListener;
    private final OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            if (mConfiguration != null && mConfiguration.mAutoDismiss) {
                dismiss();
            }
        }
    };

    Guide() {
    }

    void setConfiguration(Configuration configuration) {
        mConfiguration = configuration;
    }

    void setComponents(Component[] components) {
        mComponents = components;
    }

    void setCallback(GuideBuilder.OnVisibilityChangedListener listener) {
        this.mOnVisibilityChangedListener = listener;
    }

    public void setOnSlideListener(GuideBuilder.OnSlideListener onSlideListener) {
        this.mOnSlideListener = onSlideListener;
    }

    /**
     * 显示遮罩
     *
     * @param activity 目标Activity
     */
    public void show(FragmentActivity activity) {
        show(activity, null);
    }

    /**
     * 显示遮罩
     *
     * @param activity 目标Activity
     * @param overlay  遮罩层view
     */
    public void show(FragmentActivity activity, ViewGroup overlay) {
        mMaskView = onCreateView(activity, overlay);
        if (overlay == null) {
            overlay = (ViewGroup) activity.getWindow().getDecorView();
        }
        if (mMaskView.getParent() == null && mConfiguration.mTargetView != null) {
            overlay.addView(mMaskView);
            if (mConfiguration.mEnterAnimationId != -1) {
                Animation anim = AnimationUtils.loadAnimation(activity, mConfiguration.mEnterAnimationId);
                assert anim != null;
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (mOnVisibilityChangedListener != null) {
                            mOnVisibilityChangedListener.onShown();
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                mMaskView.startAnimation(anim);
            } else {
                if (mOnVisibilityChangedListener != null) {
                    mOnVisibilityChangedListener.onShown();
                }
            }
        }
    }

    public void clear() {
        if (mMaskView == null) {
            return;
        }
        final ViewGroup vp = (ViewGroup) mMaskView.getParent();
        if (vp == null) {
            return;
        }
        vp.removeView(mMaskView);
        onDestroy();
    }

    /**
     * 隐藏该遮罩并回收资源相关
     */
    public void dismiss() {
        if (mMaskView == null) {
            return;
        }
        final ViewGroup vp = (ViewGroup) mMaskView.getParent();
        if (vp == null) {
            return;
        }
        if (mConfiguration.mExitAnimationId != -1) {
            // mMaskView may leak if context is null
            Context context = mMaskView.getContext();
            assert context != null;

            Animation anim = AnimationUtils.loadAnimation(context, mConfiguration.mExitAnimationId);
            assert anim != null;
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    vp.removeView(mMaskView);
                    if (mOnVisibilityChangedListener != null) {
                        mOnVisibilityChangedListener.onDismiss();
                    }
                    onDestroy();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mMaskView.startAnimation(anim);
        } else {
            vp.removeView(mMaskView);
            if (mOnVisibilityChangedListener != null) {
                mOnVisibilityChangedListener.onDismiss();
            }
            onDestroy();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private MaskView onCreateView(FragmentActivity activity, ViewGroup overlay) {
        if (overlay == null) {
            overlay = (ViewGroup) activity.getWindow().getDecorView();
        }
        MaskView maskView = new MaskView(activity);
        maskView.setFullingColor(activity.getResources().getColor(mConfiguration.mFullingColorId));
        maskView.setFullingAlpha(mConfiguration.mAlpha);
        maskView.setHighTargetCorner(mConfiguration.mCorner);
        maskView.setPadding(mConfiguration.mPadding);
        maskView.setPaddingLeft(mConfiguration.mPaddingLeft);
        maskView.setPaddingTop(mConfiguration.mPaddingTop);
        maskView.setPaddingRight(mConfiguration.mPaddingRight);
        maskView.setPaddingBottom(mConfiguration.mPaddingBottom);
        maskView.setHighTargetGraphStyle(mConfiguration.mGraphStyle);
        maskView.setOverlayTarget(mConfiguration.mOverlayTarget);

        activity.getOnBackPressedDispatcher().addCallback(activity, onBackPressedCallback);

        // For removing the height of status bar we need the root content view's
        // location on screen
        int parentX = 0;
        int parentY = 0;
        if (overlay != null) {
            int[] loc = new int[2];
            overlay.getLocationInWindow(loc);
            parentX = loc[0];
            parentY = loc[1];
        }

        if (mConfiguration.mTargetView != null) {
            maskView.setTargetView(mConfiguration.mTargetView);
            maskView.setTargetRect(Common.getViewAbsRect(mConfiguration.mTargetView, parentX, parentY));
        } else {
            // Gets the target view's abs rect
            View target = activity.findViewById(mConfiguration.mTargetViewId);
            if (target != null) {
                maskView.setTargetView(target);
                maskView.setTargetRect(Common.getViewAbsRect(target, parentX, parentY));
            }
        }

        if (mConfiguration.mOutsideTouchable) {
            maskView.setClickable(false);
        } else {
            maskView.setOnTouchListener(this);
        }

        // Adds the components to the mask view.
        for (Component c : mComponents) {
            maskView.addView(Common.componentToView(activity.getLayoutInflater(), c));
        }

        return maskView;
    }

    private void onDestroy() {
        mConfiguration = null;
        mComponents = null;
        mOnVisibilityChangedListener = null;
        mOnSlideListener = null;
        mMaskView.removeAllViews();
        mMaskView = null;
        onBackPressedCallback.remove();
    }

    float startY = -1f;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        try {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                startY = motionEvent.getY();
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (startY - motionEvent.getY() > DimenUtil.dp2px(view.getContext(), SLIDE_THRESHOLD)) {
                    if (mOnSlideListener != null) {
                        mOnSlideListener.onSlideListener(GuideBuilder.SlideState.UP);
                    }
                } else if (motionEvent.getY() - startY > DimenUtil.dp2px(view.getContext(), SLIDE_THRESHOLD)) {
                    if (mOnSlideListener != null) {
                        mOnSlideListener.onSlideListener(GuideBuilder.SlideState.DOWN);
                    }
                }
                if (mMaskView != null
                        && mConfiguration != null
                        && !mConfiguration.mOverlayTarget
                        && mConfiguration.mHighLightRectClickable
                        && mConfiguration.mTargetView != null) {
                    RectF targetRect = mMaskView.getTargetRect();
                    if (targetRect != null) {
                        float rawX = motionEvent.getRawX();
                        float rawY = motionEvent.getRawY();
                        if (rawX > targetRect.left
                                && rawX < targetRect.right
                                && rawY > targetRect.top
                                && rawY < targetRect.bottom) {
                            if (mConfiguration.mHighLightRectClickListener != null) {
                                mConfiguration.mHighLightRectClickListener.onClick(mConfiguration.mTargetView);
                            } else {
                                int[] outLocation = new int[2];
                                mConfiguration.mTargetView.getLocationOnScreen(outLocation);
                                int x = (int) (motionEvent.getRawX() - outLocation[0]);
                                int y = (int) (motionEvent.getRawY() - outLocation[1]);
                                long currentTimeMillis = System.currentTimeMillis();
                                MotionEvent downMotionEvent = MotionEvent.obtain(currentTimeMillis, currentTimeMillis, MotionEvent.ACTION_DOWN, x, y, 0);
                                MotionEvent upMotionEvent = MotionEvent.obtain(currentTimeMillis, currentTimeMillis, MotionEvent.ACTION_UP, x, y, 0);

                                mConfiguration.mTargetView.dispatchTouchEvent(downMotionEvent);
                                mConfiguration.mTargetView.dispatchTouchEvent(upMotionEvent);

                                downMotionEvent.recycle();
                                upMotionEvent.recycle();
                            }
                            if (mConfiguration.mHighLightRectClickAutoDismiss) {
                                dismiss();
                            }
                        }
                    }
                }
                if (mConfiguration != null && mConfiguration.mAutoDismiss) {
                    dismiss();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
