package com.android.mi.wearable.watchfacealbum

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.*
import android.os.BatteryManager
import android.util.Log
import android.view.SurfaceHolder
import androidx.core.graphics.withRotation
import androidx.core.graphics.withScale
import androidx.wear.watchface.*
import androidx.wear.watchface.complications.data.NoDataComplicationData
import androidx.wear.watchface.complications.data.RangedValueComplicationData
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.style.CurrentUserStyleRepository
import androidx.wear.watchface.style.UserStyle
import androidx.wear.watchface.style.UserStyleSetting
import com.android.mi.wearable.watchface5.R
import com.android.mi.wearable.watchfacealbum.data.watchface.*
import com.android.mi.wearable.watchfacealbum.utils.BitmapTranslateUtils
import com.android.mi.wearable.watchfacealbum.utils.LEFT_COMPLICATION_ID
import com.android.mi.wearable.watchfacealbum.utils.RIGHT_COMPLICATION_ID
import com.android.mi.wearable.watchfacealbum.utils.STYLE_SETTING
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.ZonedDateTime
import kotlin.math.abs


private const val FRAME_PERIOD_MS_DEFAULT: Long = 16L
class WatchFace3CanvasRenderer(
    private val context: Context,
    surfaceHolder: SurfaceHolder,
    watchState: WatchState,
    private val complicationSlotsManager: ComplicationSlotsManager,
    currentUserStyleRepository: CurrentUserStyleRepository,
    canvasType: Int
) : Renderer.CanvasRenderer2<WatchFace3CanvasRenderer.AnalogSharedAssets>(
    surfaceHolder,
    currentUserStyleRepository,
    watchState,
    canvasType,
    FRAME_PERIOD_MS_DEFAULT,
    clearWithBackgroundTintBeforeRenderingHighlightLayer = false
) {

    private val clockPaint = Paint().apply {
        isAntiAlias = true
    }


    private val scope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private var watchFaceData: WatchFaceData = WatchFaceData()

    init {
        scope.launch {
            currentUserStyleRepository.userStyle.collect { userStyle ->
                updateWatchFaceData(userStyle)
            }
        }
    }

    /*
    * Triggered when the user makes changes to the watch face through the settings activity. The
    * function is called by a flow.
    */
    private fun updateWatchFaceData(userStyle: UserStyle) {
        var newWatchFaceData: WatchFaceData = watchFaceData

        // Loops through user style and applies new values to watchFaceData.
        for (options in userStyle) {
            when (options.key.id.toString()) {
                STYLE_SETTING -> {
                    val listOption = options.value as
                            UserStyleSetting.ListUserStyleSetting.ListOption

                    newWatchFaceData = newWatchFaceData.copy(
                        activeStyle = StyleIdAndResourceIds.getStyleConfig(
                            listOption.id.toString()
                        )
                    )
                }
            }

            // Only updates if something changed.
            if (watchFaceData != newWatchFaceData) {
                watchFaceData = newWatchFaceData

            }

        }
    }

    class AnalogSharedAssets : Renderer.SharedAssets {
        override fun onDestroy() {
        }
    }

    override suspend fun createSharedAssets(): AnalogSharedAssets {
        return AnalogSharedAssets()
    }

    override fun onRenderParametersChanged(renderParameters: RenderParameters) {
        Log.d("TSWatchFace","onRenderParametersChanged")
        super.onRenderParametersChanged(renderParameters)
    }

    override fun render(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: AnalogSharedAssets
    ) {
       // drawBackground(canvas,bounds,zonedDateTime)

        drawLayoutStyle1(canvas,bounds,zonedDateTime)
        if (watchFaceData.activeStyle.watchFaceStyle == TYPE_4||watchFaceData.activeStyle.watchFaceStyle == TYPE_5){
            drawClockHands(canvas,bounds,zonedDateTime)
        }

//        if (renderParameters.watchFaceLayers.contains(WatchFaceLayer.COMPLICATIONS_OVERLAY)) {
//            drawClockHands(canvas, bounds, zonedDateTime)
//        }
    }
    //绘制样式
    private fun drawLayoutStyle1(canvas: Canvas, bounds: Rect, time: ZonedDateTime){
        //判断当前是否息屏模式
        val drawAmbient = renderParameters.drawMode == DrawMode.AMBIENT
        //绘制息屏模式的背景
        val bgAmbientBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ambent_bg)
        //绘制背景图片
        //draw background
        val ambientBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.background)
        if(drawAmbient){
            canvas.drawBitmap(bgAmbientBitmap,null,bounds,clockPaint)
        }else{
            canvas.drawBitmap(ambientBitmap,null,bounds,clockPaint)
            drawComplications(canvas, bounds, time)
        }
        //绘制电池的样式
        //获取当前的电池电量
        val batteryStatus: Intent? =
            context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        var batteryPct: Float? = batteryStatus?.let { intent ->
            val level: Int = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale: Int = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            ((level * 100 / scale.toFloat()))
        }
        val batteryValue = batteryPct?.toInt() ?: 0
        //绘制当前的电量
        val batteryRes = BitmapTranslateUtils.currentBatteryPercentToRes(batteryPct)
        val batteryBitmap = BitmapFactory.decodeResource(context.resources,batteryRes)
        if (watchFaceData.activeStyle.watchFaceStyle == TYPE_1){
            canvas.drawBitmap(batteryBitmap, 313.88f, 144.37f, clockPaint)
        }else if(watchFaceData.activeStyle.watchFaceStyle == TYPE_3){
            canvas.drawBitmap(batteryBitmap, 383.01f, 244.44f, clockPaint)
        }

        //绘制日期的样式
        val data = BitmapTranslateUtils.currentMonthAndDayStyle1(watchFaceData.activeStyle.watchFaceStyle)
        //获取月份高位，低位，分隔符，日期高位，低位
        val monthTen = BitmapFactory.decodeResource(context.resources,data[0])
        val monthBit = BitmapFactory.decodeResource(context.resources,data[1])
        val dataSpit = BitmapFactory.decodeResource(context.resources,data[2])
        val dayTen = BitmapFactory.decodeResource(context.resources,data[3])
        val dayBit = BitmapFactory.decodeResource(context.resources,data[4])
        when (watchFaceData.activeStyle.watchFaceStyle) {
            TYPE_1 -> {
                //绘制月份高位
                canvas.drawBitmap(monthTen, 209.57f, 144.19f, clockPaint)
                //绘制月份低位
                canvas.drawBitmap(monthBit, 228.91f, 144.19f, clockPaint)
                //绘制分隔符
                canvas.drawBitmap(dataSpit, 247.45f, 144.75f, clockPaint)
                //绘制日期高位
                canvas.drawBitmap(dayTen, 258.39f, 144.19f, clockPaint)
                //绘制日期低位
                canvas.drawBitmap(dayBit, 277.53f, 144.19f, clockPaint)
            }
            TYPE_2 -> {
                //绘制月份高位
                canvas.drawBitmap(monthTen, 173.34f, 347.66f, clockPaint)
                //绘制月份低位
                canvas.drawBitmap(monthBit, 192.74f, 347.66f, clockPaint)
                //绘制分隔符
                canvas.drawBitmap(dataSpit, 211.2f, 347.21f, clockPaint)
                //绘制日期高位
                canvas.drawBitmap(dayTen, 222.13f, 347.66f, clockPaint)
                //绘制日期低位
                canvas.drawBitmap(dayBit, 241.28f, 347.66f, clockPaint)
            }
            TYPE_3 -> {
                //绘制月份高位
                canvas.drawBitmap(monthTen, 342.38f, 217.68f, clockPaint)
                //绘制月份低位
                canvas.drawBitmap(monthBit, 361.68f, 217.68f, clockPaint)
                //绘制分隔符
                canvas.drawBitmap(dataSpit, 380.22f, 217.27f, clockPaint)
                //绘制日期高位
                canvas.drawBitmap(dayTen, 391.17f, 217.68f, clockPaint)
                //绘制日期低位
                canvas.drawBitmap(dayBit, 410.35f, 217.68f, clockPaint)
            }
        }

        //绘制time的样式
        val mTime = BitmapTranslateUtils.currentHourAndMinuteStyle1(watchFaceData.activeStyle.watchFaceStyle)
        //获取小时的十位，小时的个位，冒号，分钟的十位，分钟的个位
        val hourTen = BitmapFactory.decodeResource(context.resources, mTime[0])
        val hourBit = BitmapFactory.decodeResource(context.resources, mTime[1])
        val timeColon = BitmapFactory.decodeResource(context.resources, mTime[2])
        val minuteTen = BitmapFactory.decodeResource(context.resources, mTime[3])
        val minuteBit = BitmapFactory.decodeResource(context.resources, mTime[4])
        //绘制小时的十位，个位，冒号，分钟的十位，分钟的个位
        //绘制时间的位置，根据表盘的style处理
        when (watchFaceData.activeStyle.watchFaceStyle) {
            TYPE_1 -> {
                canvas.drawBitmap(hourTen, 83.35f, 84.86f, clockPaint)
                canvas.drawBitmap(hourBit, 151.74f, 84.86f, clockPaint)
                canvas.drawBitmap(timeColon, 226.7f, 80.86f, clockPaint)
                canvas.drawBitmap(minuteTen, 257.51f, 84.86f, clockPaint)
                canvas.drawBitmap(minuteBit, 325.98f, 84.86f, clockPaint)
            }
            TYPE_2 -> {
                canvas.drawBitmap(hourTen, 36.07f, 207.28f, clockPaint)
                canvas.drawBitmap(hourBit, 123.34f, 207.28f, clockPaint)
                canvas.drawBitmap(minuteTen, 103.75f, 276.38f, clockPaint)
                canvas.drawBitmap(minuteBit, 190.44f, 276.38f, clockPaint)
            }
            TYPE_3 -> {
                canvas.drawBitmap(hourTen, 204.67f, 77.07f, clockPaint)
                canvas.drawBitmap(hourBit, 291.9f, 77.07f, clockPaint)
                canvas.drawBitmap(minuteTen, 250.34f, 146.16f, clockPaint)
                canvas.drawBitmap(minuteBit, 338.08f, 146.16f, clockPaint)
            }
        }
        //绘制星期
        val weekRes = BitmapTranslateUtils.currentWeekdayStyle1()
        val weekBitmap = BitmapFactory.decodeResource(context.resources,weekRes)
        when (watchFaceData.activeStyle.watchFaceStyle) {
            TYPE_1 -> {
                canvas.drawBitmap(weekBitmap, 126.54f, 143.1f, clockPaint)
            }
            TYPE_2 -> {
                canvas.drawBitmap(weekBitmap,108.22f,347.9f,clockPaint)
            }
            TYPE_3 -> {
                canvas.drawBitmap(weekBitmap,274.06f,217.68f,clockPaint)
            }
        }

    }

    private fun drawClockHands(canvas: Canvas, bounds: Rect, zonedDateTime: ZonedDateTime) {
        //adjust bounds size
        val drawAmbient = renderParameters.drawMode == DrawMode.AMBIENT
        //绘制息屏模式的背景
        val bgAmbientBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ambent_bg)
        //绘制背景图片
        //draw background
        val ambientBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.background)
        if(drawAmbient){
            canvas.drawBitmap(bgAmbientBitmap,null,bounds,clockPaint)
        }else{
            canvas.drawBitmap(ambientBitmap,null,bounds,clockPaint)
            drawComplications(canvas, bounds, zonedDateTime)
        }
        // Retrieve current time to calculate location/rotation of watch arms.
        val secondOfDay = zonedDateTime.toLocalTime().toSecondOfDay()

        // Determine the rotation of the hour and minute hand.

        // Determine how many seconds it takes to make a complete rotation for each hand
        // It takes the hour hand 12 hours to make a complete rotation
        val secondsPerHourHandRotation = Duration.ofHours(12).seconds
        // It takes the minute hand 1 hour to make a complete rotation
        val secondsPerMinuteHandRotation = Duration.ofHours(1).seconds

        // Determine the angle to draw each hand expressed as an angle in degrees from 0 to 360
        // Since each hand does more than one cycle a day, we are only interested in the remainder
        // of the secondOfDay modulo the hand interval
        val hourRotation = secondOfDay.rem(secondsPerHourHandRotation) * 360.0f /
                secondsPerHourHandRotation
        val minuteRotation = secondOfDay.rem(secondsPerMinuteHandRotation) * 360.0f /
                secondsPerMinuteHandRotation

        val hourHand = watchFaceData.activeStyle.hourHand
        val minuteHand =  watchFaceData.activeStyle.minuteHand
        val hourHandBitmap = BitmapFactory.decodeResource(context.resources,hourHand)
        val minuteHandBitmap = BitmapFactory.decodeResource(context.resources,minuteHand)
        canvas.withScale(
            x = WATCH_HAND_SCALE,
            y = WATCH_HAND_SCALE,
            pivotX = bounds.exactCenterX(),
            pivotY = bounds.exactCenterY()
        ) {
            // Draw hour hand.
            canvas.withRotation(hourRotation, bounds.exactCenterX(), bounds.exactCenterY()) {
                drawBitmap(
                    hourHandBitmap,
                    bounds.exactCenterX() - hourHandBitmap.width/2,
                    bounds.exactCenterY() - hourHandBitmap.height/2
                    , clockPaint
                )
            }
            // Draw minute hand.
            canvas.withRotation(minuteRotation, bounds.exactCenterX(), bounds.exactCenterY()) {
                drawBitmap(
                    minuteHandBitmap,
                    bounds.exactCenterX() - minuteHandBitmap.width/2,
                    bounds.exactCenterY() - minuteHandBitmap.height/2
                    , clockPaint
                )
            }
            // Draw second hand if not in ambient mode
            if (!drawAmbient) {
                //clockHandPaint.color = watchFaceColors.activeSecondaryColor
                // Second hand has a different color style (secondary color) and is only drawn in
                // active mode, so we calculate it here (not above with others).
                val secondsPerSecondHandRotation = Duration.ofMinutes(1).seconds
                val secondsRotation = secondOfDay.rem(secondsPerSecondHandRotation) * 360.0f /
                        secondsPerSecondHandRotation
                val secondHand = watchFaceData.activeStyle.secondHand
                val secondHandBitmap = BitmapFactory.decodeResource(context.resources, secondHand)
                canvas.withRotation(secondsRotation, bounds.exactCenterX(), bounds.exactCenterY()) {
                    drawBitmap(
                        secondHandBitmap,
                        bounds.exactCenterX() - secondHandBitmap.width/2,
                        bounds.exactCenterY() - secondHandBitmap.height/2
                        , clockPaint
                    )
                }
            }
        }
    }


    override fun renderHighlightLayer(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: AnalogSharedAssets
    ) {
        canvas.drawColor(renderParameters.highlightLayer!!.backgroundTint)

        for ((_, complication) in complicationSlotsManager.complicationSlots) {
            if (complication.enabled) {
                complication.renderHighlightLayer(canvas, zonedDateTime, renderParameters)
            }
        }
    }
    // ----- All drawing functions -----
    private fun drawComplications(canvas: Canvas, bounds: Rect,zonedDateTime: ZonedDateTime) {
        Log.d("wjjj", "drawComplications: ")
        for ((_, complication) in complicationSlotsManager.complicationSlots) {
            if (complication.enabled) {
                renderParameters.lastComplicationTapDownEvents
                complication.render(canvas, zonedDateTime, renderParameters)
                when (complication.complicationData.value){
                    is RangedValueComplicationData -> {

                    }
                    is ShortTextComplicationData -> {
                        val complicationData = complication.complicationData.value as ShortTextComplicationData
                        val applicationIdString  = complicationData.dataSource?.packageName
                        when (complication.id) {
                            LEFT_COMPLICATION_ID -> {

                            }
                            RIGHT_COMPLICATION_ID -> {
                            }
                        }
                    }
                    is NoDataComplicationData -> {
                    }
                    else -> {
                    }
                }
            }
        }
    }



    companion object {
//        private const val TAG = "AnalogWatchCanvasRenderer"

        // Painted between pips on watch face for hour marks.
        private val HOUR_MARKS = arrayOf("3", "6", "9", "12")

        // Used to canvas.scale() to scale watch hands in proper bounds. This will always be 1.0.
        private const val WATCH_HAND_SCALE = 1.0f
    }
}