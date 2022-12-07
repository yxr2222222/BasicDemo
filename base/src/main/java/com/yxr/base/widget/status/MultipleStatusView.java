package com.yxr.base.widget.status;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.yxr.base.R;
import com.yxr.base.widget.TitleBar;

import java.util.ArrayList;

/**
 * 一个方便在多种状态切换的view
 *
 * @author ciba
 */
public class MultipleStatusView extends RelativeLayout {
    private static final int NULL_RESOURCE_ID = -1;
    public final LayoutParams DEFAULT_LAYOUT_PARAMS = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    private final TitleBar titleBar;

    private View mEmptyView;
    private View mErrorView;
    private View mLoadingView;
    private View mNoNetworkView;
    private View mContentView;
    private int mEmptyViewResId;
    private int mErrorViewResId;
    private int mLoadingViewResId;
    private int mNoNetworkViewResId;
    private int mContentViewResId;

    private UIStatus mUiStatus;
    private LayoutInflater mInflater;
    private OnClickListener mOnRetryClickListener;

    private final ArrayList<UIStatus> statusTagList = new ArrayList<>();

    public MultipleStatusView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultipleStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        titleBar = new TitleBar(context);
        titleBar.setBackgroundColor(0xffffffff);
        titleBar.setId(R.id.default_title_bar);
        addView(titleBar);

        DEFAULT_LAYOUT_PARAMS.addRule(RelativeLayout.BELOW, titleBar.getId());

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MultipleStatusView, defStyleAttr, 0);
        mEmptyViewResId = a.getResourceId(R.styleable.MultipleStatusView_emptyView, R.layout.layout_default_empty);
        mErrorViewResId = a.getResourceId(R.styleable.MultipleStatusView_errorView, R.layout.layout_default_no_network);
        mLoadingViewResId = a.getResourceId(R.styleable.MultipleStatusView_loadingView, R.layout.layout_default_loading);
        mNoNetworkViewResId = a.getResourceId(R.styleable.MultipleStatusView_noNetworkView, R.layout.layout_default_no_network);
        mContentViewResId = a.getResourceId(R.styleable.MultipleStatusView_contentView, NULL_RESOURCE_ID);

        a.recycle();
        mInflater = LayoutInflater.from(getContext());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        showContent();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        try {
            clear(mEmptyView, mLoadingView, mErrorView, mNoNetworkView);
            statusTagList.clear();
            mOnRetryClickListener = null;
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取当前状态
     */
    public UIStatus getViewStatus() {
        return mUiStatus;
    }

    /**
     * 设置重试点击事件
     *
     * @param onRetryClickListener 重试点击事件
     */
    public void setOnRetryClickListener(OnClickListener onRetryClickListener) {
        this.mOnRetryClickListener = onRetryClickListener;
    }

    /**
     * 设置加载Ui资源，不设置使用默认样式
     *
     * @param layoutResId 加载Ui资源
     */
    public void setLoadingLayoutResId(@LayoutRes int layoutResId) {
        this.mLoadingViewResId = layoutResId;
    }

    /**
     * 设置空布局Ui资源，不设置使用默认样式
     *
     * @param layoutResId 空布局Ui资源
     */
    public void setEmptyLayoutResId(@LayoutRes int layoutResId) {
        this.mEmptyViewResId = layoutResId;
    }

    /**
     * 设置错误Ui资源，不设置使用默认样式
     *
     * @param layoutResId 错误Ui资源
     */
    public void setErrorLayoutResId(@LayoutRes int layoutResId) {
        this.mErrorViewResId = layoutResId;
    }

    /**
     * 设置网络错误Ui资源，不设置使用默认样式
     *
     * @param layoutResId 网络错误Ui资源
     */
    public void setNetWorkErrorLayoutResId(@LayoutRes int layoutResId) {
        this.mEmptyViewResId = layoutResId;
    }

    /**
     * 改变当前UI状态
     *
     * @param uiStatus 改变后的状态
     */
    public void changUiStatus(@NonNull UIStatus uiStatus) {
        switch (uiStatus) {
            case LOADING:
                showLoading();
                break;
            case EMPTY:
                showEmpty();
                break;
            case NETWORK_ERROR:
                showNoNetwork();
                break;
            case ERROR:
                showError();
                break;
            default:
                showContent();
                break;
        }
    }

    /**
     * 显示空视图
     */
    public final void showEmpty() {
        showEmpty(mEmptyViewResId, DEFAULT_LAYOUT_PARAMS);
    }

    /**
     * 显示空视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    public final void showEmpty(int layoutId, ViewGroup.LayoutParams layoutParams) {
        showEmpty(inflateView(layoutId), layoutParams);
    }

    /**
     * 显示空视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    public final void showEmpty(View view, ViewGroup.LayoutParams layoutParams) {
        checkNull(view, "Empty view is null!");
        mUiStatus = UIStatus.EMPTY;
        if (null == mEmptyView) {
            mEmptyView = view;
            mEmptyView.setTag(mUiStatus);
            View emptyRetryView = mEmptyView.findViewById(R.id.empty_retry_view);
            if (null != mOnRetryClickListener && null != emptyRetryView) {
                emptyRetryView.setOnClickListener(mOnRetryClickListener);
            }
            statusTagList.add(mUiStatus);
            addView(mEmptyView, 0, layoutParams);
        }
        showViewByTag(mUiStatus);
    }

    /**
     * 显示错误视图
     */
    public final void showError() {
        showError(mErrorViewResId, DEFAULT_LAYOUT_PARAMS);
    }

    /**
     * 显示错误视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    public final void showError(int layoutId, ViewGroup.LayoutParams layoutParams) {
        showError(inflateView(layoutId), layoutParams);
    }

    /**
     * 显示错误视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    public final void showError(View view, ViewGroup.LayoutParams layoutParams) {
        checkNull(view, "Error view is null!");
        mUiStatus = UIStatus.ERROR;
        if (null == mErrorView) {
            mErrorView = view;
            mErrorView.setTag(mUiStatus);
            View errorRetryView = mErrorView.findViewById(R.id.error_retry_view);
            if (null != mOnRetryClickListener && null != errorRetryView) {
                errorRetryView.setOnClickListener(mOnRetryClickListener);
            }
            statusTagList.add(mUiStatus);
            addView(mErrorView, 0, layoutParams);
        }
        showViewByTag(mUiStatus);
    }

    /**
     * 显示加载中视图
     */
    public final void showLoading() {
        showLoading(mLoadingViewResId, DEFAULT_LAYOUT_PARAMS);
    }

    /**
     * 显示加载中视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    public final void showLoading(int layoutId, ViewGroup.LayoutParams layoutParams) {
        showLoading(inflateView(layoutId), layoutParams);
    }

    /**
     * 显示加载中视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    public final void showLoading(View view, ViewGroup.LayoutParams layoutParams) {
        checkNull(view, "Loading view is null!");
        mUiStatus = UIStatus.LOADING;
        if (null == mLoadingView) {
            mLoadingView = view;
            mLoadingView.setTag(mUiStatus);
            statusTagList.add(mUiStatus);
            addView(mLoadingView, 0, layoutParams);
        }
        showViewByTag(mUiStatus);
    }

    /**
     * 显示无网络视图
     */
    public final void showNoNetwork() {
        showNoNetwork(mNoNetworkViewResId, DEFAULT_LAYOUT_PARAMS);
    }

    /**
     * 显示无网络视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    public final void showNoNetwork(int layoutId, ViewGroup.LayoutParams layoutParams) {
        showNoNetwork(inflateView(layoutId), layoutParams);
    }

    /**
     * 显示无网络视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    public final void showNoNetwork(View view, ViewGroup.LayoutParams layoutParams) {
        checkNull(view, "No network view is null!");
        mUiStatus = UIStatus.NETWORK_ERROR;
        if (null == mNoNetworkView) {
            mNoNetworkView = view;
            mNoNetworkView.setTag(mUiStatus);
            View noNetworkRetryView = mNoNetworkView.findViewById(R.id.no_network_retry_view);
            if (null != mOnRetryClickListener && null != noNetworkRetryView) {
                noNetworkRetryView.setOnClickListener(mOnRetryClickListener);
            }
            statusTagList.add(mUiStatus);
            addView(mNoNetworkView, 0, layoutParams);
        }
        showViewByTag(mUiStatus);
    }

    /**
     * 显示内容视图
     */
    public final void showContent() {
        mUiStatus = UIStatus.CONTENT;
        if (null == mContentView && mContentViewResId != NULL_RESOURCE_ID) {
            mContentView = inflateView(mContentViewResId);
            addView(mContentView, 0, DEFAULT_LAYOUT_PARAMS);
        }
        showContentView();
    }

    private void showContentView() {
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            if (view == titleBar) {
                continue;
            }
            view.setVisibility(statusTagList.contains(view.getTag()) ? View.GONE : View.VISIBLE);
        }
    }

    private View inflateView(int layoutId) {
        return mInflater.inflate(layoutId, null);
    }

    private void showViewByTag(UIStatus tag) {
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            if (view == titleBar) {
                continue;
            }
            UIStatus viewTagInt = UIStatus.CONTENT;
            Object viewTag = view.getTag();
            if (viewTag instanceof UIStatus) {
                viewTagInt = (UIStatus) viewTag;
            }
            view.setVisibility(viewTagInt == tag ? View.VISIBLE : View.GONE);
        }
    }

    private void checkNull(Object object, String hint) {
        if (null == object) {
            throw new NullPointerException(hint);
        }
    }

    private void clear(View... views) {
        if (null == views) {
            return;
        }
        try {
            for (View view : views) {
                if (null != view) {
                    removeView(view);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TitleBar getTitleBar() {
        return titleBar;
    }
}
