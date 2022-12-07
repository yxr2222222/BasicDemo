package com.yxr.base;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.yxr.base.widget.guide.Component;
import com.yxr.base.widget.guide.GuideBuilder;

public class BaseGuide implements GuideBuilder.OnVisibilityChangedListener, GuideBuilder.OnTargetClickListener {
    @NonNull
    private final Activity activity;

    public BaseGuide(@NonNull Activity activity) {
        this.activity = activity;
    }

    public void showGuide(View targetView) {
        if (targetView != null) {
            if (targetView.getWidth() > 0 && targetView.getHeight() > 0) {
                showInnerGuide(targetView);
            } else {
                targetView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (targetView.getWidth() > 0 && targetView.getHeight() > 0) {
                            targetView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            showInnerGuide(targetView);
                        }
                    }
                });
            }
        }
    }

    private void showInnerGuide(View targetView) {
        GuideBuilder builder = new GuideBuilder();
        builder = builder.setTargetView(targetView)
                .setAlpha(getAlpha())
                .setOutsideTouchable(getOutsideTouchable())
                .setHighTargetCorner(getHeightTargetCorner())
                .setHighTargetPadding(getHeightTargetPadding())
                .setOnTargetClickListener(this)
                .setOnVisibilityChangedListener(this);
        Component component = getComponent();
        if (component != null) {
            builder = builder.addComponent(component);
        }
        builder.createGuide()
                .show(activity);
    }

    @Override
    public void onShown() {

    }

    @Override
    public void onDismiss() {

    }

    @Override
    public void onTargetClicked() {
        Log.e("TTTTTAG", "onTargetClicked: ");
    }

    protected boolean getOutsideTouchable() {
        return false;
    }

    protected Component getComponent() {
        return new SimpleComponent(R.drawable.icon_guide_arrow, activity.getString(R.string.click_here));
    }

    protected int getHeightTargetPadding() {
        return 0;
    }

    protected int getHeightTargetCorner() {
        return 20;
    }

    protected int getAlpha() {
        return 150;
    }

    public static class SimpleComponent implements Component {

        private int iconRes;
        private String desc;

        public SimpleComponent(String desc) {
            this(0, desc);
        }

        public SimpleComponent(int iconRes, String desc) {
            this.iconRes = iconRes;
            this.desc = desc;
        }

        @Override
        public View getView(LayoutInflater inflater) {
            View view = inflater.inflate(R.layout.layout_simple_component, null);
            ImageView ivIcon = view.findViewById(R.id.ivIcon);
            TextView tvDesc = view.findViewById(R.id.tvDesc);
            if (iconRes != 0) {
                ivIcon.setImageResource(iconRes);
            }
            tvDesc.setText(desc);
            return view;
        }

        @Override
        public int getAnchor() {
            return Component.ANCHOR_BOTTOM;
        }

        @Override
        public int getFitPosition() {
            return Component.FIT_END;
        }

        @Override
        public int getXOffset() {
            return 0;
        }

        @Override
        public int getYOffset() {
            return 0;
        }
    }
}
