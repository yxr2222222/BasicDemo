package com.yxr.base.widget;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class CountDownTextView extends TextView {
    public static final long COUNT_DOWN_INTERVAL = 1000;
    public static final long DEFAULT_COUNT_DOWN_TIME = 3000;
    private CountDownListener countDownListener;
    private CountDownTimer countDownTimer;
    protected long time;
    private String format;
    private int maxShowTime;

    public CountDownTextView(Context context) {
        super(context);
    }

    public CountDownTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CountDownTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置倒计时监听
     *
     * @param countDownListener 倒计时监听
     */
    public void setCountDownListener(CountDownListener countDownListener) {
        this.countDownListener = countDownListener;
    }

    /**
     * 设置倒计时展示格式
     *
     * @param format 格式
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * 开始倒计时
     */
    public void startCountDown() {
        startCountDown(DEFAULT_COUNT_DOWN_TIME);
    }

    public void setMaxShowTime(int maxShowTime) {
        this.maxShowTime = maxShowTime;
    }

    /**
     * 开始倒计时
     *
     * @param time 倒计时时长
     */
    public void startCountDown(long time) {
        this.time = time;
        releaseCountDown();
        countDownTimer = new CountDownTimer(time, COUNT_DOWN_INTERVAL) {

            @Override
            public void onTick(long millisUntilFinished) {
                onInnerTick(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                onInnerTick(0);
                if (countDownListener != null) {
                    countDownListener.onCountDownFinish();
                }
            }
        };
        countDownTimer.start();
    }

    protected void onInnerTick(long millisUntilFinished) {
        int showTime = (int) (millisUntilFinished / COUNT_DOWN_INTERVAL + 1);
        showTime = maxShowTime != 0 && showTime > maxShowTime ? maxShowTime : showTime;
        if (format != null) {
            setText(String.format(format, showTime));
        } else {
            setText(String.valueOf(showTime));
        }
    }

    /**
     * 释放倒计时
     */
    public void releaseCountDown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    public interface CountDownListener {
        void onCountDownFinish();
    }
}
