package com.yxr.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import com.yxr.base.R;


public class ImageTextView extends LinearLayout {
    private final static int LEFT = 0;
    private final static int TOP = 1;
    private final static int RIGHT = 2;
    private final static int BOTTOM = 3;
    private static final int DEFAULT_TEXT_COLOR = 0xff5c5c5c;
    private static final int DEFAULT_TEXT_CHECK_COLOR = 0xfffeb34d;
    private static final float DEFAULT_TEXT_SIZE = 14;

    private int imageOrientation = LEFT;
    private int imageRes;
    private int imageCheckRes;
    private int textColor = DEFAULT_TEXT_COLOR;
    private int textCheckColor = DEFAULT_TEXT_CHECK_COLOR;
    private float textSize;
    private RelativeLayout imageParent;
    private ImageView imageView;
    private TextView textView;
    private boolean checked;
    private String text;
    private String textCheck;
    private boolean textShow = true;
    private boolean textBold = false;
    private boolean checkBold = false;
    private boolean imageShow = true;
    private boolean centerGravity = true;

    public ImageTextView(Context context) {
        this(context, null);
    }

    public ImageTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    protected void init(AttributeSet attrs) {
        int defaultWidthHeight = (int) (getResources().getDisplayMetrics().density * 20);
        int defaultPadding = (int) (getResources().getDisplayMetrics().density * 8);
        int imageWidth = defaultWidthHeight;
        int imageHeight = defaultWidthHeight;
        int imageTextPadding = defaultPadding;
        boolean isTextFill = false;

        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.ImageTextView);
        if (array != null) {
            imageOrientation = array.getInt(R.styleable.ImageTextView_imageOrientation, LEFT);
            imageRes = array.getResourceId(R.styleable.ImageTextView_imageRes, 0);
            imageCheckRes = array.getResourceId(R.styleable.ImageTextView_imageCheckRes, 0);
            imageWidth = (int) array.getDimension(R.styleable.ImageTextView_imageWidth, defaultWidthHeight);
            imageHeight = (int) array.getDimension(R.styleable.ImageTextView_imageHeight, defaultWidthHeight);

            textColor = array.getColor(R.styleable.ImageTextView_textNormalColor, DEFAULT_TEXT_COLOR);
            textCheckColor = array.getColor(R.styleable.ImageTextView_textCheckColor, DEFAULT_TEXT_CHECK_COLOR);
            textSize = array.getFloat(R.styleable.ImageTextView_textNormalSize, DEFAULT_TEXT_SIZE);
            checked = array.getBoolean(R.styleable.ImageTextView_checked, false);
            text = array.getString(R.styleable.ImageTextView_textNormal);
            textCheck = array.getString(R.styleable.ImageTextView_textCheck);
            textShow = array.getBoolean(R.styleable.ImageTextView_textShow, true);
            checkBold = array.getBoolean(R.styleable.ImageTextView_checkBold, false);
            textBold = array.getBoolean(R.styleable.ImageTextView_textBold, false);
            imageShow = array.getBoolean(R.styleable.ImageTextView_imageShow, true);
            centerGravity = array.getBoolean(R.styleable.ImageTextView_centerGravity, true);
            isTextFill = array.getBoolean(R.styleable.ImageTextView_isTextFill, false);
            imageTextPadding = (int) array.getDimension(R.styleable.ImageTextView_imageTextPadding, defaultPadding);
            array.recycle();
        }

        if (centerGravity) {
            setGravity(Gravity.CENTER);
        }

        imageParent = new RelativeLayout((getContext()));
        imageView = new ImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        setImageRes(false);
        imageParent.addView(imageView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        textView = new TextView(getContext());
        textView.setIncludeFontPadding(false);
        LayoutParams textParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (isTextFill) {
            textParams.width = 0;
            textParams.weight = 1;
            textView.setGravity(Gravity.CENTER);
        }
        textView.setLayoutParams(textParams);
        setText(false);
        textView.setTextSize(textSize);
        if (textBold) {
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }

        LayoutParams imageParams = new LayoutParams(imageWidth, imageHeight);
        if (LEFT == imageOrientation) {
            setOrientation(HORIZONTAL);
            imageParams.setMargins(0, 0, imageTextPadding, 0);
            imageParent.setLayoutParams(imageParams);
            addView(imageParent);
            addView(textView);
        } else if (TOP == imageOrientation) {
            setOrientation(VERTICAL);
            imageParams.setMargins(0, 0, 0, imageTextPadding);
            imageParent.setLayoutParams(imageParams);
            addView(imageParent);
            addView(textView);
        } else if (RIGHT == imageOrientation) {
            setOrientation(HORIZONTAL);
            imageParams.setMargins(imageTextPadding, 0, 0, 0);
            imageParent.setLayoutParams(imageParams);
            addView(textView);
            addView(imageParent);
        } else {
            setOrientation(VERTICAL);
            imageParams.setMargins(0, imageTextPadding, 0, 0);
            imageParent.setLayoutParams(imageParams);
            addView(textView);
            addView(imageParent);
        }

        imageParent.setVisibility(imageShow ? VISIBLE : GONE);
        textView.setVisibility(textShow ? VISIBLE : GONE);

        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageTextView.this.performClick();
            }
        });

        imageParent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageTextView.this.performClick();
            }
        });
    }

    public void setImageCheckRes(@DrawableRes int imageCheckRes) {
        this.imageCheckRes = imageCheckRes;
        setImageRes(checked);
    }

    public void setImageUncheckRes(@DrawableRes int imageRes) {
        this.imageRes = imageRes;
        setImageRes(checked);
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        setText(checked);
    }

    public void setTextCheckColor(int textCheckColor) {
        this.textCheckColor = textCheckColor;
        setText(checked);
    }

    public void setCheck(boolean checked) {
        this.checked = checked;
        setText(checked);
        setImageRes(checked);
    }

    public void setCheck(boolean checked, boolean isImageViewGone) {
        this.checked = checked;
        setText(checked);
        imageParent.setVisibility(isImageViewGone ? View.GONE : View.VISIBLE);
    }

    public boolean isChecked() {
        return checked;
    }

    private void setText(boolean checked) {
        textView.setTextColor(checked ? textCheckColor : textColor);
        textView.setText(checked ? textCheck : text);
        if (!textBold) {
            if (checkBold && checked) {
                textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            } else {
                textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            }
        }
    }

    public void setText(String text) {
        this.textCheck = text;
        this.text = text;
        textView.setText(text);
    }

    public void setNormalText(String text) {
        this.text = text;
        textView.setText(text);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public RelativeLayout getImageParent() {
        return imageParent;
    }

    public TextView getTextView() {
        return textView;
    }

    private void setImageRes(boolean checked) {
        if (checked && imageCheckRes != 0) {
            imageView.setImageResource(imageCheckRes);
        } else if (imageRes != 0) {
            imageView.setImageResource(imageRes);
        }
    }

    public void setImageTextEnabled(boolean imageTextEnabled) {
        setEnabled(imageTextEnabled);
        imageParent.setEnabled(imageTextEnabled);
        textView.setEnabled(imageTextEnabled);
    }
}