package com.yxr.base.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;

import com.yxr.base.R;

import java.util.LinkedList;

/**
 * @author ：Bob
 */
public class TitleBar extends ViewGroup implements View.OnClickListener {
    private static final int DEFAULT_MAIN_TEXT_SIZE = 18;
    private static final int DEFAULT_ACTION_TEXT_SIZE = 15;
    private static final int DEFAULT_TITLE_BAR_HEIGHT = 48;
    private static final String STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height";

    private ImageView mLeftImageView;
    private LinearLayout mRightLayout;
    private LinearLayout mCenterLayout;
    private TextView mCenterText;
    private View mCustomCenterView;
    private View mDividerView;

    private boolean mImmersive;

    private int mScreenWidth;
    private int mStatusBarHeight;
    private int mActionPadding;
    private int mOutPadding;
    private int mHeight;

    public TitleBar(Context context) {
        super(context);
        init(context);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setImmersive(false);
        float density = getResources().getDisplayMetrics().density;
        mActionPadding = (int) (12 * density);
        mOutPadding = (int) (8 * density);
        mHeight = (int) (density * DEFAULT_TITLE_BAR_HEIGHT);
        initView(context);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void initView(Context context) {
        float density = getResources().getDisplayMetrics().density;
        int leftPadding = (int) (density * 12);
        mLeftImageView = new ImageView(context);
        mLeftImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mLeftImageView.setPadding(leftPadding, leftPadding, leftPadding, leftPadding);
        mLeftImageView.setImageResource(R.drawable.titlebar_return_icon_black);
        mLeftImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getContext() instanceof Activity) {
                    ((Activity) getContext()).onBackPressed();
                }
            }
        });

        mCenterLayout = new LinearLayout(context);
        mRightLayout = new LinearLayout(context);
        mDividerView = new View(context);

        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);

        mCenterText = new TextView(context);
        TextPaint paint = mCenterText.getPaint();
        paint.setAntiAlias(true);
        paint.setFakeBoldText(true);
        mCenterText.setTextColor(0xff292929);
        mCenterLayout.addView(mCenterText);

        mCenterLayout.setGravity(Gravity.CENTER);
        mCenterText.setTextSize(DEFAULT_MAIN_TEXT_SIZE);
        mCenterText.setSingleLine();
        mCenterText.setGravity(Gravity.CENTER);
        mCenterText.setEllipsize(TextUtils.TruncateAt.END);

        addView(mLeftImageView, new LayoutParams((int) (density * 48), LayoutParams.MATCH_PARENT));
        addView(mCenterLayout);
        addView(mRightLayout, layoutParams);
        addView(mDividerView, new LayoutParams(LayoutParams.MATCH_PARENT, (int) getResources().getDisplayMetrics().density));
        setDividerColor(0xfff2f2f2);

        setBackgroundColor(0xffffffff);
    }

    public void setImmersive(boolean immersive) {
        mImmersive = immersive;
        if (mImmersive) {
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                mStatusBarHeight = getResources().getDimensionPixelSize(resourceId);
            }
        } else {
            mStatusBarHeight = 0;
        }
        setMeasuredDimension(getMeasuredWidth(), mHeight);
    }

    public void setHeight(int height) {
        mHeight = height;
        setMeasuredDimension(getMeasuredWidth(), mHeight);
    }

    public void setLeftImageResource(@DrawableRes int resId) {
        mLeftImageView.setImageResource(resId);
    }

    public void setLeftClickListener(OnClickListener l) {
        mLeftImageView.setOnClickListener(l);
    }

    public void setLeftVisible(boolean visible) {
        mLeftImageView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setTitle(CharSequence title) {
        mCenterText.setText(title);
    }

    private void setTitle(CharSequence title, CharSequence subTitle, int orientation) {
        mCenterLayout.setOrientation(orientation);
        mCenterText.setText(title);
    }

    public void setCenterClickListener(OnClickListener l) {
        mCenterLayout.setOnClickListener(l);
    }

    public void setTitle(int resid) {
        setTitle(getResources().getString(resid));
    }

    public void setTitleColor(int resid) {
        mCenterText.setTextColor(resid);
    }

    public void setTitleSize(float size) {
        mCenterText.setTextSize(size);
    }

    public void setTitleBackground(int resid) {
        mCenterText.setBackgroundResource(resid);
    }

    public void setCustomTitle(View titleView) {
        if (titleView == null) {
            mCenterText.setVisibility(View.VISIBLE);
            if (mCustomCenterView != null) {
                mCenterLayout.removeView(mCustomCenterView);
            }

        } else {
            if (mCustomCenterView != null) {
                mCenterLayout.removeView(mCustomCenterView);
            }
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            mCustomCenterView = titleView;
            mCenterLayout.addView(titleView, layoutParams);
            mCenterText.setVisibility(View.GONE);
        }
    }

    public void setDivider(Drawable drawable) {
        mDividerView.setBackgroundDrawable(drawable);
    }

    public void setDividerColor(int color) {
        mDividerView.setBackgroundColor(color);
    }

    public void setDividerHeight(int dividerHeight) {
        mDividerView.getLayoutParams().height = dividerHeight;
    }

    /**
     * Function to set a click listener for Title TextView
     *
     * @param listener the onClickListener
     */
    public void setOnTitleClickListener(OnClickListener listener) {
        mCenterText.setOnClickListener(listener);
    }

    @Override
    public void onClick(View view) {
        final Object tag = view.getTag();
        if (tag instanceof Action) {
            final Action action = (Action) tag;
            action.performAction(view);
        }
    }

    /**
     * Adds a list of {@link Action}s.
     *
     * @param actionList the actions to add
     */
    public void addActions(ActionList actionList) {
        int actions = actionList.size();
        for (int i = 0; i < actions; i++) {
            addAction(actionList.get(i));
        }
    }

    /**
     * Adds a new {@link Action}.
     *
     * @param action the action to add
     */
    public View addAction(Action action) {
        final int index = mRightLayout.getChildCount();
        return addAction(action, index);
    }

    /**
     * Adds a new {@link Action} at the specified index.
     *
     * @param action the action to add
     * @param index  the position at which to add the action
     */
    public View addAction(Action action, int index) {
        View view = inflateAction(action);

        LinearLayout.LayoutParams params;
        if (action instanceof ImageAction) {
            int imageHeight = mHeight - view.getPaddingTop() - view.getPaddingBottom();
            int imageWidht = (int) (imageHeight * ((ImageAction) action).getRatio());
            params = new LinearLayout.LayoutParams(imageWidht + view.getPaddingLeft() + view.getPaddingRight(), mHeight);
        } else {
            params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.MATCH_PARENT);
        }
        mRightLayout.addView(view, index, params);
        return view;
    }

    /**
     * Removes all action views from this action bar
     */
    public void removeAllActions() {
        mRightLayout.removeAllViews();
    }

    /**
     * Remove a action from the action bar.
     *
     * @param index position of action to remove
     */
    public void removeActionAt(int index) {
        mRightLayout.removeViewAt(index);
    }

    /**
     * Remove a action from the action bar.
     *
     * @param action The action to remove
     */
    public void removeAction(Action action) {
        int childCount = mRightLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = mRightLayout.getChildAt(i);
            if (view != null) {
                final Object tag = view.getTag();
                if (tag instanceof Action && tag.equals(action)) {
                    mRightLayout.removeView(view);
                }
            }
        }
    }

    /**
     * Returns the number of actions currently registered with the action bar.
     *
     * @return action count
     */
    public int getActionCount() {
        return mRightLayout.getChildCount();
    }

    /**
     * Inflates a {@link View} with the given {@link Action}.
     *
     * @param action the action to inflate
     * @return a view
     */
    private View inflateAction(Action action) {
        View view = null;
        if (action instanceof ImageAction) {
            ImageView img = new ImageView(getContext());
            img.setPadding(action.getPaddingLeft(), action.getPaddingTop(), action.getPaddingRight(), action.getPaddingBottom());
            img.setScaleType(((ImageAction) action).getScaleType());
            img.setImageResource(action.getDrawable());
            view = img;
        } else {
            TextView text = new TextView(getContext());
            text.setPadding(action.getPaddingLeft(), action.getPaddingTop(), action.getPaddingRight(), action.getPaddingBottom());
            text.setGravity(Gravity.CENTER);
            text.setText(action.getText());
            text.setTextSize(DEFAULT_ACTION_TEXT_SIZE);
            if (action.getTextColor() != 0) {
                text.setTextColor(action.getTextColor());
            }
            view = text;
        }

        view.setPadding(mActionPadding, mActionPadding, mOutPadding * 2, mActionPadding);
        view.setTag(action);
        view.setOnClickListener(this);
        return view;
    }

    public View getViewByAction(Action action) {
        View view = findViewWithTag(action);
        return view;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height;
        if (heightMode != MeasureSpec.EXACTLY) {
            height = mHeight + mStatusBarHeight;
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY);
        } else {
            height = MeasureSpec.getSize(heightMeasureSpec) + mStatusBarHeight;
        }
        mScreenWidth = MeasureSpec.getSize(widthMeasureSpec);
        measureChild(mLeftImageView, widthMeasureSpec, heightMeasureSpec);
        measureChild(mRightLayout, widthMeasureSpec, heightMeasureSpec);
        if (mLeftImageView.getMeasuredWidth() > mRightLayout.getMeasuredWidth()) {
            mCenterLayout.measure(
                    MeasureSpec.makeMeasureSpec(mScreenWidth - 2 * mLeftImageView.getMeasuredWidth(), MeasureSpec.EXACTLY)
                    , heightMeasureSpec);
        } else {
            mCenterLayout.measure(
                    MeasureSpec.makeMeasureSpec(mScreenWidth - 2 * mRightLayout.getMeasuredWidth(), MeasureSpec.EXACTLY)
                    , heightMeasureSpec);
        }
        measureChild(mDividerView, widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mLeftImageView.layout(0, mStatusBarHeight, mLeftImageView.getMeasuredWidth(), mLeftImageView.getMeasuredHeight() + mStatusBarHeight);
        mRightLayout.layout(mScreenWidth - mRightLayout.getMeasuredWidth(), mStatusBarHeight,
                mScreenWidth, mRightLayout.getMeasuredHeight() + mStatusBarHeight);
        if (mLeftImageView.getMeasuredWidth() > mRightLayout.getMeasuredWidth()) {
            mCenterLayout.layout(mLeftImageView.getMeasuredWidth(), mStatusBarHeight,
                    mScreenWidth - mLeftImageView.getMeasuredWidth(), getMeasuredHeight());
        } else {
            mCenterLayout.layout(mRightLayout.getMeasuredWidth(), mStatusBarHeight,
                    mScreenWidth - mRightLayout.getMeasuredWidth(), getMeasuredHeight());
        }
        mDividerView.layout(0, getMeasuredHeight() - mDividerView.getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight());
    }

    /**
     * A {@link LinkedList} that holds a list of {@link Action}s.
     */
    @SuppressWarnings("serial")
    public static class ActionList extends LinkedList<Action> {
    }

    /**
     * Definition of an action that could be performed, along with a icon to
     * show.
     */
    public interface Action {
        int getPaddingLeft();

        int getPaddingTop();

        int getPaddingRight();

        int getPaddingBottom();

        String getText();

        int getTextColor();

        int getDrawable();

        void performAction(View view);
    }

    public static abstract class ImageAction implements Action {
        private int mDrawable;
        private float ratio;

        public ImageAction(int drawable) {
            this(drawable, 1);
        }

        public ImageAction(int drawable, float ratio) {
            mDrawable = drawable;
            this.ratio = ratio;
        }

        public float getRatio() {
            return ratio;
        }

        @Override
        public int getDrawable() {
            return mDrawable;
        }

        @Override
        public final String getText() {
            return null;
        }

        @Override
        public final int getTextColor() {
            return 0;
        }

        @Override
        public int getPaddingLeft() {
            return 0;
        }

        @Override
        public int getPaddingTop() {
            return 0;
        }

        @Override
        public int getPaddingRight() {
            return 0;
        }

        @Override
        public int getPaddingBottom() {
            return 0;
        }

        public ImageView.ScaleType getScaleType() {
            return ImageView.ScaleType.CENTER_CROP;
        }
    }

    public static abstract class TextAction implements Action {
        final private String mText;
        private final int textColor;

        public TextAction(String text, int textColor) {
            mText = text;
            this.textColor = textColor;
        }

        @Override
        public int getDrawable() {
            return 0;
        }

        @Override
        public String getText() {
            return mText;
        }

        @Override
        public final int getTextColor() {
            return textColor;
        }

        @Override
        public int getPaddingLeft() {
            return 0;
        }

        @Override
        public int getPaddingTop() {
            return 0;
        }

        @Override
        public int getPaddingRight() {
            return 0;
        }

        @Override
        public int getPaddingBottom() {
            return 0;
        }
    }

}
