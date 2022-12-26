package com.android.mi.wearable.watchface5

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.*
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.os.BatteryManager
import android.text.format.DateFormat
import android.text.method.MovementMethod
import android.util.Log
import android.view.SurfaceHolder
import android.view.animation.Animation.RESTART
import android.view.animation.RotateAnimation
import androidx.core.content.ContextCompat.registerReceiver
import androidx.core.graphics.withRotation
import androidx.core.graphics.withScale
import androidx.wear.watchface.*
import androidx.wear.watchface.complications.data.*
import androidx.wear.watchface.complications.rendering.CanvasComplicationDrawable
import androidx.wear.watchface.complications.rendering.ComplicationDrawable
import androidx.wear.watchface.style.CurrentUserStyleRepository
import androidx.wear.watchface.style.UserStyle
import androidx.wear.watchface.style.UserStyleSetting
import androidx.wear.watchface.style.WatchFaceLayer
import com.android.mi.wearable.watchface5.data.watchface.ColorStyleIdAndResourceIds
import com.android.mi.wearable.watchface5.data.watchface.ShapeStyleIdAndResourceIds
import com.android.mi.wearable.watchface5.data.watchface.WatchFaceData
import com.android.mi.wearable.watchface5.utils.BitmapTranslateUtils
import com.android.mi.wearable.watchface5.utils.COLOR_STYLE_SETTING
import com.android.mi.wearable.watchface5.utils.SHAPE_STYLE_SETTING
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.ZonedDateTime
import java.util.*


// Default for how long each frame is displayed at expected frame rate.
private const val FRAME_PERIOD_MS_DEFAULT: Long = 16L
//private const val DEFAULT_COMPLICATION_STYLE_DRAWABLE_ID = R.drawable.complication_red_style
//private const val DEFAULT_COMPLICATION_STYLE_DRAWABLE_ID_TEST = R.drawable.complication_left_style1
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
                COLOR_STYLE_SETTING -> {
                    val listOption = options.value as
                            UserStyleSetting.ListUserStyleSetting.ListOption

                    newWatchFaceData = newWatchFaceData.copy(
                        activeColorStyle = ColorStyleIdAndResourceIds.getColorStyleConfig(
                            listOption.id.toString()
                        )
                    )
                }
                SHAPE_STYLE_SETTING -> {
                    val listOption = options.value as
                            UserStyleSetting.ListUserStyleSetting.ListOption
                    newWatchFaceData = newWatchFaceData.copy(
                        shapeStyle = ShapeStyleIdAndResourceIds.getShapeStyleConfig(
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
        drawBackground(canvas,bounds,zonedDateTime)

        if (renderParameters.watchFaceLayers.contains(WatchFaceLayer.COMPLICATIONS_OVERLAY)) {
            drawClockHands(canvas, bounds, zonedDateTime)
        }
    }

    private fun drawBackground(canvas: Canvas, bounds: Rect,zonedDateTime: ZonedDateTime) {
        val drawAmbient = renderParameters.drawMode == DrawMode.AMBIENT
        if (drawAmbient) {
            //draw background
            val ambientIndexRes = BitmapTranslateUtils.currentAmbientIndexRes(watchFaceData.shapeStyle.shapeType,watchFaceData.activeColorStyle.watchFaceStyle)
            val ambientBitmap = BitmapFactory.decodeResource(context.resources, ambientIndexRes)
            canvas.drawBitmap(ambientBitmap,null,bounds,clockPaint)

            //draw month
            //draw date month
            val monthRes = BitmapTranslateUtils.currentAmbientMonth(watchFaceData.activeColorStyle.watchFaceStyle)
            val monthBitmap = BitmapFactory.decodeResource(context.resources, monthRes)
            canvas.drawBitmap(
                monthBitmap,
                bounds.width() * 0.71f,
                bounds.exactCenterY() - monthBitmap.height / 2,
                clockPaint
            )
            val today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
            val todayNumberArray = BitmapTranslateUtils.currentNumberArray(today)
            val secondTodayRes = BitmapTranslateUtils.currentAmbientTodayNumber(
                todayNumberArray[1],
                watchFaceData.activeColorStyle.watchFaceStyle
            )
            val secondTodayBitmap = BitmapFactory.decodeResource(context.resources, secondTodayRes)
            if (todayNumberArray[0] == 0 && todayNumberArray[1] != 0) {
                canvas.drawBitmap(
                    secondTodayBitmap,
                    bounds.width() * 0.85f,
                    bounds.exactCenterY() - secondTodayBitmap.height / 2,
                    clockPaint
                )
            } else {
                val firstNumberRes = BitmapTranslateUtils.currentBatterNumber(todayNumberArray[0])
                val firstNumberBitmap =
                    BitmapFactory.decodeResource(context.resources, firstNumberRes)
                canvas.drawBitmap(
                    firstNumberBitmap,
                    bounds.width() * 0.85f,
                    bounds.exactCenterY() - firstNumberBitmap.height / 2,
                    clockPaint
                )
                canvas.drawBitmap(
                    secondTodayBitmap,
                    bounds.width() * 0.85f + firstNumberBitmap.width,
                    bounds.exactCenterY() - secondTodayBitmap.height / 2,
                    clockPaint
                )
            }


        } else {
            val batteryStatus: Intent? =
                context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            var batteryPct: Float? = batteryStatus?.let { intent ->
                val level: Int = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                val scale: Int = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                ((level * 100 / scale.toFloat()))
            }
            val batteryValue = batteryPct?.toInt() ?: 0
            val indexRes = watchFaceData.shapeStyle.index
            val indexBgRes = BitmapTranslateUtils.currentBgRes(
                watchFaceData.shapeStyle.shapeType,
                watchFaceData.activeColorStyle.watchFaceStyle
            )
            val indexBg = BitmapFactory.decodeResource(context.resources, indexRes)
            val indexBgMask = BitmapFactory.decodeResource(context.resources, indexBgRes)
            val nanoOfDay = zonedDateTime.toLocalTime().toNanoOfDay()
            val secondsPerSecondHandRotation = Duration.ofSeconds(1).toNanos()
            val secondsRotation =
                nanoOfDay.rem(secondsPerSecondHandRotation) * 360.0f / secondsPerSecondHandRotation
            canvas.withRotation(secondsRotation, bounds.exactCenterX(), bounds.exactCenterY()) {
                canvas.drawBitmap(indexBgMask, null, bounds, clockPaint)
            }
            canvas.drawBitmap(indexBg, null, bounds, clockPaint)


            //draw week
            val dateRes = BitmapTranslateUtils.currentDate()
            val dateBitmap = BitmapFactory.decodeResource(context.resources, dateRes)
            canvas.drawBitmap(
                dateBitmap,
                bounds.exactCenterX() - dateBitmap.width / 2,
                bounds.height() * 0.27f,
                clockPaint
            )

            //draw battery
            val batteryRes = R.drawable.battery
            val batteryBitmap = BitmapFactory.decodeResource(context.resources, batteryRes)
            val batteryUnitRes = R.drawable.battery_unit
            val batteryUnitBitmap = BitmapFactory.decodeResource(context.resources, batteryUnitRes)
            val batteryNumberArray = BitmapTranslateUtils.currentNumberArray(batteryValue)
            val secondNumberRes = BitmapTranslateUtils.currentBatterNumber(batteryNumberArray[1])
            val secondNumberBitmap =
                BitmapFactory.decodeResource(context.resources, secondNumberRes)
            canvas.drawBitmap(
                batteryBitmap,
                bounds.exactCenterX() - batteryBitmap.width / 2,
                bounds.height() * 0.63f,
                clockPaint
            )

            if (batteryNumberArray[0] == 0 && batteryNumberArray[1] != 0) {
                canvas.drawBitmap(
                    batteryUnitBitmap,
                    bounds.exactCenterX(),
                    bounds.height() * 0.72f - batteryUnitBitmap.height / 2,
                    clockPaint
                )
                canvas.drawBitmap(
                    secondNumberBitmap,
                    bounds.exactCenterX() - secondNumberBitmap.width,
                    bounds.height() * 0.72f - secondNumberBitmap.height / 2,
                    clockPaint
                )
            } else {
                val firstNumberRes = BitmapTranslateUtils.currentBatterNumber(batteryNumberArray[0])
                val firstNumberBitmap =
                    BitmapFactory.decodeResource(context.resources, firstNumberRes)
                canvas.drawBitmap(
                    batteryUnitBitmap,
                    bounds.exactCenterX() + secondNumberBitmap.width / 2,
                    bounds.height() * 0.72f - batteryUnitBitmap.height / 2,
                    clockPaint
                )
                canvas.drawBitmap(
                    secondNumberBitmap,
                    bounds.exactCenterX() - secondNumberBitmap.width / 2,
                    bounds.height() * 0.72f - secondNumberBitmap.height / 2,
                    clockPaint
                )
                canvas.drawBitmap(
                    firstNumberBitmap,
                    bounds.exactCenterX() - secondNumberBitmap.width / 2 - firstNumberBitmap.width,
                    bounds.height() * 0.72f - firstNumberBitmap.height / 2,
                    clockPaint
                )
            }

            //draw date month
            val frameRes = watchFaceData.shapeStyle.frameShape
            val frameShadowRes = watchFaceData.shapeStyle.frameShadow
            val frameBitmap = BitmapFactory.decodeResource(context.resources, frameRes)
            val frameShadowBitmap = BitmapFactory.decodeResource(context.resources, frameShadowRes)
            val monthRes = BitmapTranslateUtils.currentMonth()
            val monthBitmap = BitmapFactory.decodeResource(context.resources, monthRes)


            canvas.drawBitmap(
                frameShadowBitmap,
                bounds.width() * 0.67f,
                bounds.exactCenterY() - frameShadowBitmap.height / 2,
                clockPaint
            )
            canvas.drawBitmap(
                frameBitmap,
                bounds.width() * 0.67f,
                bounds.exactCenterY() - frameBitmap.height / 2,
                clockPaint
            )
            canvas.drawBitmap(
                monthBitmap,
                bounds.width() * 0.71f,
                bounds.exactCenterY() - monthBitmap.height / 2,
                clockPaint
            )
            val today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
            val todayNumberArray = BitmapTranslateUtils.currentNumberArray(today)
            val secondTodayRes = BitmapTranslateUtils.currentTodayNumber(
                todayNumberArray[1],
                watchFaceData.activeColorStyle.watchFaceStyle
            )
            val secondTodayBitmap = BitmapFactory.decodeResource(context.resources, secondTodayRes)
            if (todayNumberArray[0] == 0 && todayNumberArray[1] != 0) {
                canvas.drawBitmap(
                    secondTodayBitmap,
                    bounds.width() * 0.85f,
                    bounds.exactCenterY() - secondTodayBitmap.height / 2,
                    clockPaint
                )
            } else {
                val firstNumberRes = BitmapTranslateUtils.currentBatterNumber(todayNumberArray[0])
                val firstNumberBitmap =
                    BitmapFactory.decodeResource(context.resources, firstNumberRes)
                canvas.drawBitmap(
                    firstNumberBitmap,
                    bounds.width() * 0.85f,
                    bounds.exactCenterY() - firstNumberBitmap.height / 2,
                    clockPaint
                )
                canvas.drawBitmap(
                    secondTodayBitmap,
                    bounds.width() * 0.85f + firstNumberBitmap.width,
                    bounds.exactCenterY() - secondTodayBitmap.height / 2,
                    clockPaint
                )
            }
        }
    }


    private fun drawClockHands(canvas: Canvas, bounds: Rect, zonedDateTime: ZonedDateTime) {
        //adjust bounds size
        val drawAmbient = renderParameters.drawMode == DrawMode.AMBIENT
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

        val hourHand = if (drawAmbient) {
            BitmapTranslateUtils.currentAmbientHourHandRes(watchFaceData.activeColorStyle.watchFaceStyle)
        } else {
            if (hourRotation > 180f) {
                watchFaceData.activeColorStyle.hourHandLeft
            } else {
                watchFaceData.activeColorStyle.hourHandRight
            }
        }
        val minuteHand =  if (drawAmbient) {
            BitmapTranslateUtils.currentAmbientMinuteHandRes(watchFaceData.activeColorStyle.watchFaceStyle)
        } else {
            if (minuteRotation > 180f) {
                watchFaceData.activeColorStyle.minuteHandLeft
            } else {
                watchFaceData.activeColorStyle.minuteHandRight
            }
        }

//        val hourHand = R.drawable.hour_hand_left1
        val hourHandShadow = watchFaceData.activeColorStyle.hourHandShadow
//        val minuteHand = R.drawable.minute_left_hand1
        val minuteHandShadow = watchFaceData.activeColorStyle.minuteHandShadow
        val hourHandBitmap = BitmapFactory.decodeResource(context.resources,hourHand)
        val hourHandShadowBitmap = BitmapFactory.decodeResource(context.resources,hourHandShadow)
        val minuteHandBitmap = BitmapFactory.decodeResource(context.resources,minuteHand)
        val minuteHandShadowBitmap = BitmapFactory.decodeResource(context.resources,minuteHandShadow)
        canvas.withScale(
            x = WATCH_HAND_SCALE,
            y = WATCH_HAND_SCALE,
            pivotX = bounds.exactCenterX(),
            pivotY = bounds.exactCenterY()
        ) {
            // Draw hour hand.
            canvas.withRotation(hourRotation, bounds.exactCenterX(), bounds.exactCenterY()) {
                drawBitmap(
                    hourHandShadowBitmap, null, RectF(
                        bounds.exactCenterX() - hourHandShadowBitmap.width / 2,
                        0f,
                        bounds.exactCenterX() + hourHandShadowBitmap.width / 2,
                        bounds.height().toFloat()
                    ), clockPaint
                )
                drawBitmap(
                    hourHandBitmap, null, RectF(
                        bounds.exactCenterX() - hourHandBitmap.width / 2,
                        0f,
                        bounds.exactCenterX() + hourHandBitmap.width / 2,
                        bounds.height().toFloat()
                    ), clockPaint
                )
            }

            // Draw minute hand.
            canvas.withRotation(minuteRotation, bounds.exactCenterX(), bounds.exactCenterY()) {
                drawBitmap(
                    minuteHandShadowBitmap, null, RectF(
                        bounds.exactCenterX() - minuteHandShadowBitmap.width / 2,
                        0f,
                        bounds.exactCenterX() + minuteHandShadowBitmap.width / 2,
                        bounds.height().toFloat()
                    ), clockPaint
                )
                drawBitmap(
                    minuteHandBitmap, null, RectF(
                        bounds.exactCenterX() - minuteHandBitmap.width / 2,
                        0f,
                        bounds.exactCenterX() + minuteHandBitmap.width / 2,
                        bounds.height().toFloat()
                    ), clockPaint
                )
            }

            // Draw second hand if not in ambient mode
            if (!drawAmbient) {
//                clockHandPaint.color = watchFaceColors.activeSecondaryColor

                // Second hand has a different color style (secondary color) and is only drawn in
                // active mode, so we calculate it here (not above with others).
                val nanoOfDay = zonedDateTime.toLocalTime().toNanoOfDay()
                val secondsPerSecondHandRotation = Duration.ofMinutes(1).toNanos()
                val secondsRotation = nanoOfDay.rem(secondsPerSecondHandRotation) * 360.0f / secondsPerSecondHandRotation
                val secondHand = watchFaceData.shapeStyle.secondsHand
                val secondHandShadow = watchFaceData.shapeStyle.secondsHandShadow
                val secondHandBitmap = BitmapFactory.decodeResource(context.resources, secondHand)
                val secondHandShadowBitmap = BitmapFactory.decodeResource(context.resources, secondHandShadow)
                canvas.withRotation(secondsRotation, bounds.exactCenterX(), bounds.exactCenterY()) {
                    drawBitmap(
                        secondHandShadowBitmap, null, RectF(
                            bounds.exactCenterX() - secondHandShadowBitmap.width / 2,
                            0f,
                            bounds.exactCenterX() + secondHandShadowBitmap.width / 2,
                            bounds.height().toFloat()
                        ), clockPaint
                    )
                    drawBitmap(
                        secondHandBitmap, null, RectF(
                            bounds.exactCenterX() - secondHandBitmap.width / 2,
                            0f,
                            bounds.exactCenterX() + secondHandBitmap.width / 2,
                            bounds.height().toFloat()
                        ), clockPaint
                    )
                }
            }

            val pointRes = watchFaceData.activeColorStyle.pointHand
            val handPoint1Bitmap = BitmapFactory.decodeResource(context.resources,pointRes)
            drawBitmap(
                handPoint1Bitmap, null, RectF(
                    bounds.exactCenterX() - handPoint1Bitmap.width / 2,
                    bounds.exactCenterY() - handPoint1Bitmap.height / 2,
                    bounds.exactCenterX() + handPoint1Bitmap.width / 2,
                    bounds.exactCenterY() + handPoint1Bitmap.height / 2
                ), clockPaint)
        }
    }


    // ----- All drawing functions -----
//    private fun drawComplications(canvas: Canvas, bounds: Rect,zonedDateTime: ZonedDateTime) {
//        for ((_, complication) in complicationSlotsManager.complicationSlots) {
//            if (complication.enabled) {
//                renderParameters.lastComplicationTapDownEvents
//                complication.render(canvas, zonedDateTime, renderParameters)
//            }
//        }
//    }

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



    companion object {
//        private const val TAG = "AnalogWatchCanvasRenderer"

        // Painted between pips on watch face for hour marks.
        private val HOUR_MARKS = arrayOf("3", "6", "9", "12")

        // Used to canvas.scale() to scale watch hands in proper bounds. This will always be 1.0.
        private const val WATCH_HAND_SCALE = 1.0f
    }
}