package com.yxr.base.widget.guideview;

import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import java.util.ArrayList;
import java.util.List;

public class GuideChain implements LifecycleObserver {
    private Guide currGuide = null;
    private final List<GuideEntity> guideList = new ArrayList<>();
    private int index = -1;
    private OnGuideListener onGuideListener = null;
    private final FragmentActivity activity;
    private final Handler handler = new Handler(Looper.getMainLooper());

    public GuideChain(@NonNull FragmentActivity activity) {
        this.activity = activity;
        activity.getLifecycle().addObserver(this);
    }

    public GuideChain(@NonNull Fragment fragment) {
        this.activity = fragment.getActivity();
        fragment.getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        handler.removeCallbacks(null);
        activity.getLifecycle().removeObserver(this);
    }

    /**
     * 添加引导
     */
    public GuideChain addGuide(@NonNull GuideEntity guide) {
        guideList.add(guide);
        return this;
    }

    /**
     * 设置引导监听
     */
    public GuideChain setGuideListener(OnGuideListener onGuideListener) {
        this.onGuideListener = onGuideListener;
        return this;
    }

    /**
     * 开始展示引导
     */
    public GuideChain show() {
        index = -1;
        currGuide = null;
        nextGuide();
        return this;
    }

    /**
     * 手动结束当前引导并进入下一个引导
     */
    public void manualDismissCurrThenNextGuide() {
        if (currGuide != null) {
            currGuide.dismiss();
        }
    }

    /**
     * 下一个引导
     */
    private void nextGuide() {
        index++;
        if (index >= 0 && index < guideList.size()) {
            showGuide(guideList.get(index));
        } else if (index >= guideList.size()) {
            handler.removeCallbacksAndMessages(null);
            if (onGuideListener != null) {
                onGuideListener.onGuideFinished();
            }
        }
    }

    /**
     * 展示每一步引导视图
     */
    private void showGuide(final GuideEntity guide) {
        guide.highLightView.post(new Runnable() {
            @Override
            public void run() {
                GuideBuilder builder = new GuideBuilder();
                builder.setTargetView(guide.highLightView)
                        .setAlpha(guide.alpha)
                        .setAutoDismiss(guide.clickMaskAutoDismiss)
                        .setCanBackPress(guide.canBackPress)
                        .setHighLightRectClickable(guide.highLightRectClickable)
                        .setHighLightRectClickListener(guide.highLightRectClickListener)
                        .setHighLightRectClickAutoDismiss(guide.highLightRectClickAutoDismiss)
                        .setOverlayTarget(guide.overlayTarget)
                        .setOutsideTouchable(guide.outsideTouchable)
                        .setHighTargetCorner(guide.highLightCorner)
                        .setHighTargetPadding(guide.highLightPadding)
                        .setHighTargetGraphStyle(guide.graphStyle)
                        .setOnSlideListener(guide.onSlideListener)
                        .setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
                            @Override
                            public void onShown() {
                                if (guide.onVisibilityChangedListener != null) {
                                    guide.onVisibilityChangedListener.onShown();
                                }
                            }

                            @Override
                            public void onDismiss() {
                                currGuide = null;
                                if (guide.onVisibilityChangedListener != null) {
                                    guide.onVisibilityChangedListener.onDismiss();
                                }
                                nextGuide();
                            }
                        });

                if (guide.components != null) {
                    for (SimpleComponent component : guide.components) {
                        component.setViewAutoNextClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (currGuide != null) {
                                    currGuide.dismiss();
                                }
                            }
                        });
                        builder.addComponent(component);
                    }
                }

                currGuide = builder.createGuide();
                currGuide.show(activity);

                handler.removeCallbacksAndMessages(null);
                if (guide.autoDismissDelay > 0) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (currGuide != null && !activity.isFinishing()) {
                                currGuide.dismiss();
                            }
                        }
                    }, guide.autoDismissDelay);
                }
            }
        });
    }

    public Guide getCurrGuide() {
        return currGuide;
    }

    public static class GuideEntity {
        // 需要高亮的控件
        @NonNull
        private final View highLightView;
        // 背景透明度
        private int alpha = 175;
        // 高亮圆角，不需要转换为px
        private int highLightCorner = 0;
        // 高亮padding，不需要转换px
        private int highLightPadding = 0;
        private int graphStyle = 0;
        // 遮罩是否盖住整个屏幕
        private boolean overlayTarget = false;
        // 外部是否可以触摸
        private boolean outsideTouchable = false;
        // 点击遮罩是否自动消失并进行下一步（如果有）
        private boolean clickMaskAutoDismiss = true;
        // 是否可以响应返回手势
        private boolean canBackPress = false;
        // 高亮区域是否支持点击
        private boolean highLightRectClickable = true;
        // 高亮区域点击事件监听，为空的话默认是响应需要高亮控件的点击事件
        private View.OnClickListener highLightRectClickListener = null;
        // 高亮区域点击之后是否自动消失并进行下一步（如果有）
        private boolean highLightRectClickAutoDismiss = true;
        // 自动dismiss延迟
        private long autoDismissDelay = -1;
        // 引导视图，可以是多个视图拼接的
        private List<SimpleComponent> components = null;
        // 手势滑动监听
        private GuideBuilder.OnSlideListener onSlideListener = null;
        // 当前引导可见性监听
        private GuideBuilder.OnVisibilityChangedListener onVisibilityChangedListener = null;

        public GuideEntity(@NonNull View highLightView) {
            this.highLightView = highLightView;
        }

        public View getHighLightView() {
            return highLightView;
        }

        public int getAlpha() {
            return alpha;
        }

        public int getHighLightCorner() {
            return highLightCorner;
        }

        public int getHighLightPadding() {
            return highLightPadding;
        }

        public boolean isOverlayTarget() {
            return overlayTarget;
        }

        public boolean isOutsideTouchable() {
            return outsideTouchable;
        }

        public boolean isClickMaskAutoDismiss() {
            return clickMaskAutoDismiss;
        }

        public boolean isCanBackPress() {
            return canBackPress;
        }

        public boolean isHighLightRectClickable() {
            return highLightRectClickable;
        }

        public long getAutoDismissDelay() {
            return autoDismissDelay;
        }

        public GuideEntity setAutoDismissDelay(long autoDismissDelay) {
            this.autoDismissDelay = autoDismissDelay;
            return this;
        }

        public View.OnClickListener getHighLightRectClickListener() {
            return highLightRectClickListener;
        }

        public boolean isHighLightRectClickAutoDismiss() {
            return highLightRectClickAutoDismiss;
        }

        public List<SimpleComponent> getComponents() {
            return components;
        }

        public GuideBuilder.OnSlideListener getOnSlideListener() {
            return onSlideListener;
        }

        public GuideBuilder.OnVisibilityChangedListener getOnVisibilityChangedListener() {
            return onVisibilityChangedListener;
        }

        public GuideEntity setAlpha(int alpha) {
            this.alpha = alpha;
            return this;
        }

        public GuideEntity setHighLightCorner(int highLightCorner) {
            this.highLightCorner = DimenUtil.dp2px(highLightView.getContext(), highLightCorner);
            return this;
        }

        public GuideEntity setHighTargetGraphStyle(int mGraphStyle) {
            this.graphStyle = mGraphStyle;
            return this;
        }

        public GuideEntity setHighLightPadding(int highLightPadding) {
            this.highLightPadding = DimenUtil.dp2px(highLightView.getContext(), highLightPadding);
            return this;
        }

        public GuideEntity setOverlayTarget(boolean overlayTarget) {
            this.overlayTarget = overlayTarget;
            return this;
        }

        public GuideEntity setOutsideTouchable(boolean outsideTouchable) {
            this.outsideTouchable = outsideTouchable;
            return this;
        }

        public GuideEntity setClickMaskAutoDismiss(boolean clickMaskAutoDismiss) {
            this.clickMaskAutoDismiss = clickMaskAutoDismiss;
            return this;
        }

        public GuideEntity setCanBackPress(boolean canBackPress) {
            this.canBackPress = canBackPress;
            return this;
        }

        public GuideEntity setHighLightRectClickable(boolean highLightRectClickable) {
            this.highLightRectClickable = highLightRectClickable;
            return this;
        }

        public GuideEntity setHighLightRectClickListener(View.OnClickListener highLightRectClickListener) {
            this.highLightRectClickListener = highLightRectClickListener;
            return this;
        }

        public GuideEntity setHighLightRectClickAutoDismiss(boolean highLightRectClickAutoDismiss) {
            this.highLightRectClickAutoDismiss = highLightRectClickAutoDismiss;
            return this;
        }

        public GuideEntity addComponent(SimpleComponent component) {
            if (this.components == null) {
                this.components = new ArrayList<>();
            }
            this.components.add(component);
            return this;
        }

        public GuideEntity setComponents(List<SimpleComponent> components) {
            this.components = components;
            return this;
        }

        public GuideEntity setOnSlideListener(GuideBuilder.OnSlideListener onSlideListener) {
            this.onSlideListener = onSlideListener;
            return this;
        }

        public GuideEntity setOnVisibilityChangedListener(GuideBuilder.OnVisibilityChangedListener onVisibilityChangedListener) {
            this.onVisibilityChangedListener = onVisibilityChangedListener;
            return this;
        }
    }
}
