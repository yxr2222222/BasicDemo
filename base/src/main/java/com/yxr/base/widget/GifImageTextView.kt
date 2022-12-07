package com.yxr.base.widget

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.annotation.DrawableRes
import com.yxr.base.R
import pl.droidsonroids.gif.AnimationListener
import pl.droidsonroids.gif.GifDrawable

/**
 * 选中状态时执行gif图，gif图执行完毕显示普通图片
 * 未选中状态为普通图片
 */
class GifImageTextView(context: Context?, attrs: AttributeSet?) : ImageTextView(context, attrs),
    AnimationListener {
    private var gifDrawable: GifDrawable? = null
    private lateinit var gifImageView: ImageView

    // 是否gif结束之后展示的是普通图片
    var isStopShowImage = true
    var isGifAutoPlay = false
    var isNoImageIcon = false
    var isStopSeekToFirst = false

    // 是否需要展示gif动画
    var isNeedGif = true
        set(value) {
            field = value
            setCheck(isChecked)
        }

    override fun init(attrs: AttributeSet?) {
        super.init(attrs)
        val array = context.obtainStyledAttributes(attrs, R.styleable.GifImageTextView)
        isGifAutoPlay = array.getBoolean(R.styleable.GifImageTextView_isGifAutoPlay, false)
        isNoImageIcon = array.getBoolean(R.styleable.GifImageTextView_isNoImageIcon, false)
        isStopSeekToFirst = array.getBoolean(R.styleable.GifImageTextView_isStopSeekToFirst, false)
        array.recycle()

        gifImageView = ImageView(context)
        gifImageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageParent.addView(
            gifImageView,
            RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )

        changImageGifVisible(GONE, VISIBLE)
    }

    fun setGifDrawRes(@DrawableRes gifRes: Int, loopCount: Int) {
        val gifDrawable = GifDrawable(context.resources, gifRes)
        gifDrawable.loopCount = loopCount

        setGifDrawRes(gifDrawable)
        if (isGifAutoPlay && isNeedGif) {
            changImageGifVisible(VISIBLE, GONE)
            startGif()
        }
    }

    /**
     * 设置gif图
     */
    fun setGifDrawRes(gifDrawable: GifDrawable) {
        recycleGif()

        this.gifDrawable = gifDrawable
        this.gifDrawable?.addAnimationListener(this)
        gifImageView.setImageDrawable(this.gifDrawable)

        if (isGifAutoPlay) {
            changImageGifVisible(VISIBLE, GONE)
            startGif()
        }
    }

    /**
     * 选中状态改变，选中显示gif并重新播放
     * 未选中显示普通图片，结束gif播放
     */
    override fun setCheck(checked: Boolean) {
        super.setCheck(checked)
        if (checked && isNeedGif) {
            changImageGifVisible(VISIBLE, GONE)
            startGif()
        } else {
            stopGif()
            changImageGifVisible(GONE, VISIBLE)
        }
    }

    /**
     * git动画播放完成，展示普通图片
     */
    override fun onAnimationCompleted(loopNumber: Int) {
        if ((gifDrawable?.loopCount ?: 0) > 0) {
            try {
                post {
                    stopGif()
                    if (isStopShowImage) {
                        changImageGifVisible(GONE, VISIBLE)
                    }
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 停止播放gif图
     */
    fun stopGif() {
        try {
            gifDrawable?.stop()
            if (isStopSeekToFirst) {
                gifDrawable?.seekTo(0)
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    /**
     * 开始播放gif图
     */
    fun startGif() {
        try {
            gifDrawable?.reset()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    /**
     * 释放gif图资源
     */
    fun recycleGif() {
        try {
            gifDrawable?.removeAnimationListener(this)
            gifDrawable?.recycle()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    /**
     * 改变gif图和普通图的展示状态
     */
    private fun changImageGifVisible(gifVisible: Int, imageVisible: Int) {
        if (isNoImageIcon) {
            gifImageView.visibility = VISIBLE
            imageView.visibility = GONE
        } else {
            gifImageView.visibility = gifVisible
            imageView.visibility = imageVisible
        }
    }

}