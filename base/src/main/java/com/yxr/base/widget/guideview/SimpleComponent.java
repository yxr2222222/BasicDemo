package com.yxr.base.widget.guideview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;

import java.util.List;

public abstract class SimpleComponent implements Component {
    private View.OnClickListener viewAutoNextOrDismissClickListener = null;

    @Override
    public View getView(LayoutInflater inflater) {
        View guideView = inflater.inflate(getGuideLayoutId(), null, false);
        List<Integer> clickIds = getClickIds();
        if (clickIds != null) {
            for (int id : clickIds) {
                View view = guideView.findViewById(id);
                if (view != null) {
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onViewClick(v);
                            if (viewAutoNextOrDismissClickListener != null) {
                                viewAutoNextOrDismissClickListener.onClick(v);
                            }
                        }
                    });
                }
            }
        }
        return guideView;
    }

    /**
     * 引导视图在目标控件的什么位置，例如Component.ANCHOR_RIGHT则说明引导视图在目标控件右边
     */
    @Override
    public int getAnchor() {
        return Component.ANCHOR_BOTTOM;
    }

    /**
     * 配合getAnchor使用的补充另外一个方向的位置
     * 例如getAnchor是上下，此处补充的则是左右位置，如果是左右补充的则是上下位置
     */
    @Override
    public int getFitPosition() {
        return Component.FIT_START;
    }

    /**
     * X轴的偏移量，不用转换成px
     */
    @Override
    public int getXOffset() {
        return 0;
    }

    /**
     * Y轴的偏移量，不用转换成px
     */
    @Override
    public int getYOffset() {
        return 0;
    }

    @Override
    public int getComponentWidth() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    public void setViewAutoNextClickListener(View.OnClickListener viewAutoNextOrDismissClickListener) {
        this.viewAutoNextOrDismissClickListener = viewAutoNextOrDismissClickListener;
    }

    /**
     * 获取需要响应点击的控件
     */
    protected List<Integer> getClickIds() {
        return null;
    }

    /**
     * @param view
     */
    protected void onViewClick(View view) {

    }

    /**
     * 获取引导的视图ID
     */
    @LayoutRes
    protected abstract int getGuideLayoutId();
}
