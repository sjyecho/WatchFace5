package com.android.mi.wearable.watchfacealbum.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.content.res.use
import com.android.mi.wearable.watchface5.R
import com.android.mi.wearable.watchfacealbum.editor.IComplicationClick
import com.android.mi.wearable.watchfacealbum.editor.WatchFace5ConfigActivity
import kotlin.math.abs

class SlideHandleView @JvmOverloads constructor(

    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
): FrameLayout(context, attrs, defStyleAttr, defStyleRes), View.OnTouchListener{
    companion object {
        private const val SCROLL_DISTANCE_MIN = 100
        private const val FAST_SCROLL_INTERVAL= 800
    }
    private var isOpenSlide: Boolean = false
//    private var leftDestinationName: String = ""
    private var pageType: String = ""
    private var mPosX: Float = 0f
    private var mPosY: Float = 0f
    private var mCurX: Float = 0f
    private var mCurY: Float = 0f
    private var mPressTime: Long = 0
    private var mLastLaunchTime: Long = 0
    private lateinit var listener: IComplicationClick
    init {
        context.obtainStyledAttributes(attrs, R.styleable.SlideHandleView, defStyleAttr, defStyleRes)
            .use { typedArray ->
                isOpenSlide = typedArray.getBoolean(R.styleable.SlideHandleView_openSlide,false)
                pageType = typedArray.getString(R.styleable.SlideHandleView_pageType)?:""
//                rightDestinationName = typedArray.getString(R.styleable.SlideHandleView_rightDestinationName)?:""
                 if(isOpenSlide){
                     setOnTouchListener(this)
                 }else{
                     setOnTouchListener(null)
                 }
            }
        if (context is WatchFace5ConfigActivity){
            listener = context
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        var intercepted = false
        when (ev.action){
            MotionEvent.ACTION_DOWN -> {
                mPosX = ev.x
                mPosY = ev.y
                mCurX = ev.x
                mCurY = ev.y
                mPressTime = System.currentTimeMillis()
            }
            MotionEvent.ACTION_MOVE -> {
//                intercepted = abs(mPosX.minus(ev.x)) < abs(mPosY.minus(ev.y))
            }
        }
        return false
    }

    override fun onTouch(p0: View?, ev: MotionEvent?): Boolean {
        when (ev?.action) {
            MotionEvent.ACTION_MOVE -> {
                mCurX = ev.x
                mCurY = ev.y
            }
            MotionEvent.ACTION_UP -> {
                val isUp = mCurY.minus(mPosY) < 0
                val isFar = abs(mCurY.minus(mPosY)) > SCROLL_DISTANCE_MIN
                val isVertical = abs(mCurY.minus(mPosY)) >  abs(mCurX.minus(mPosX))
                val isTooFast = (System.currentTimeMillis() - mLastLaunchTime) < FAST_SCROLL_INTERVAL
                val upScroll: Boolean = isUp && isFar
                if(isVertical && isFar){
                    if ("color" == pageType){
                        listener.onColorPagerChange(isUp)
                    }else if("style" == pageType){
                        listener.onStylePagerChange(isUp)
                    }
                }
            }
        }
        return true
    }
}