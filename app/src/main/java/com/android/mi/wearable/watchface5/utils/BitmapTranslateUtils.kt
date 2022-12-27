package com.android.mi.wearable.watchface5.utils
import com.android.mi.wearable.watchface5.R
import com.android.mi.wearable.watchface5.data.watchface.*
import java.util.*
import kotlin.random.Random

object BitmapTranslateUtils {
    /***
    相册表盘style1
     ***/
//weekday
    fun cameraWfWeekdayStyle1(): Int {
        val weekday = Calendar.getInstance().get(
            Calendar.DAY_OF_WEEK
        )
        return when (weekday) {
            Calendar.MONDAY -> R.drawable.camera_wf_style1_week_1
            Calendar.TUESDAY -> R.drawable.camera_wf_style1_week_2
            Calendar.WEDNESDAY -> R.drawable.camera_wf_style1_week_3
            Calendar.THURSDAY -> R.drawable.camera_wf_style1_week_4
            Calendar.FRIDAY -> R.drawable.camera_wf_style1_week_5
            Calendar.SATURDAY -> R.drawable.camera_wf_style1_week_6
            Calendar.SUNDAY -> R.drawable.camera_wf_style1_week_7
            else -> R.drawable.camera_wf_style1_week_1
        }
    }

    //mouth and day
    fun currentWfMonthAndDayStyle1(mouth: Int): Int {
        return when (mouth) {
            0 -> R.drawable.camera_wf_style1_date_digital_0
            1 -> R.drawable.camera_wf_style1_date_digital_1
            2 -> R.drawable.camera_wf_style1_date_digital_2
            3 -> R.drawable.camera_wf_style1_date_digital_3
            4 -> R.drawable.camera_wf_style1_date_digital_4
            5 -> R.drawable.camera_wf_style1_date_digital_5
            6 -> R.drawable.camera_wf_style1_date_digital_6
            7 -> R.drawable.camera_wf_style1_date_digital_7
            8 -> R.drawable.camera_wf_style1_date_digital_8
            9 -> R.drawable.camera_wf_style1_date_digital_9
            else -> R.drawable.camera_wf_style1_date_digital_0
        }
    }

    //battery
    fun currentWfStepStyle1(battery: Int): Int {
        return when (battery) {
            0 -> R.drawable.camera_wf_style1_step_digital_0
            1 -> R.drawable.camera_wf_style1_step_digital_1
            2 -> R.drawable.camera_wf_style1_step_digital_2
            3 -> R.drawable.camera_wf_style1_step_digital_3
            4 -> R.drawable.camera_wf_style1_step_digital_4
            5 -> R.drawable.camera_wf_style1_step_digital_5
            6 -> R.drawable.camera_wf_style1_step_digital_6
            7 -> R.drawable.camera_wf_style1_step_digital_7
            8 -> R.drawable.camera_wf_style1_step_digital_8
            9 -> R.drawable.camera_wf_style1_step_digital_9
            else -> R.drawable.camera_wf_style1_step_digital_0
        }
    }

    //time  big 和 small
    private fun digitalToResStyle1(
        watchType: Int,
        digital: Int,
        drawableResArray: Array<Int>,
        i: Int
    ) {
        //时间大字体
        if (watchType == TYPE_1) {
            when (digital) {
                0 -> drawableResArray[i] = R.drawable.camera_wf_style1_time_big_0
                1 -> drawableResArray[i] = R.drawable.camera_wf_style1_time_big_1
                2 -> drawableResArray[i] = R.drawable.camera_wf_style1_time_big_2
                3 -> drawableResArray[i] = R.drawable.camera_wf_style1_time_big_3
                4 -> drawableResArray[i] = R.drawable.camera_wf_style1_time_big_4
                5 -> drawableResArray[i] = R.drawable.camera_wf_style1_time_big_5
                6 -> drawableResArray[i] = R.drawable.camera_wf_style1_time_big_6
                7 -> drawableResArray[i] = R.drawable.camera_wf_style1_time_big_7
                8 -> drawableResArray[i] = R.drawable.camera_wf_style1_time_big_8
                9 -> drawableResArray[i] = R.drawable.camera_wf_style1_time_big_9
            }
            //时间小字体
        } else {
            when (digital) {
                0 -> drawableResArray[i] = R.drawable.camera_wf_style1_time_small_0
                1 -> drawableResArray[i] = R.drawable.camera_wf_style1_time_small_1
                2 -> drawableResArray[i] = R.drawable.camera_wf_style1_time_small_2
                3 -> drawableResArray[i] = R.drawable.camera_wf_style1_time_small_3
                4 -> drawableResArray[i] = R.drawable.camera_wf_style1_time_small_4
                5 -> drawableResArray[i] = R.drawable.camera_wf_style1_time_small_5
                6 -> drawableResArray[i] = R.drawable.camera_wf_style1_time_small_6
                7 -> drawableResArray[i] = R.drawable.camera_wf_style1_time_small_7
                8 -> drawableResArray[i] = R.drawable.camera_wf_style1_time_small_8
                9 -> drawableResArray[i] = R.drawable.camera_wf_style1_time_small_9
            }
        }
    }


    fun currentDate(): Int {
        val month = Calendar.getInstance().get(
            Calendar.DAY_OF_WEEK
        )
        return when (month) {
            Calendar.MONDAY -> R.drawable.monday
            Calendar.TUESDAY -> R.drawable.tuesday
            Calendar.WEDNESDAY -> R.drawable.wednesday
            Calendar.THURSDAY -> R.drawable.thursday
            Calendar.FRIDAY -> R.drawable.friday
            Calendar.SATURDAY -> R.drawable.saturday
            Calendar.SUNDAY -> R.drawable.sunday
            else -> R.drawable.monday
        }
    }


    fun currentAmbientMonth(watchType: Int): Int {
        val month = Calendar.getInstance().get(
            Calendar.MONTH
        )
        return when (month) {
            Calendar.JANUARY -> if (watchType == TYPE_1) R.drawable.ambient_jan else R.drawable.ambient2_jan
            Calendar.FEBRUARY -> if (watchType == TYPE_1) R.drawable.ambient_feb else R.drawable.ambient2_feb
            Calendar.MARCH -> if (watchType == TYPE_1) R.drawable.ambient_mar else R.drawable.ambient2_mar
            Calendar.APRIL -> if (watchType == TYPE_1) R.drawable.ambient_apr else R.drawable.ambient2_apr
            Calendar.MAY -> if (watchType == TYPE_1) R.drawable.ambient_may else R.drawable.ambient2_may
            Calendar.JUNE -> if (watchType == TYPE_1) R.drawable.ambient_jun else R.drawable.ambient2_jun
            Calendar.JULY -> if (watchType == TYPE_1) R.drawable.ambient_jul else R.drawable.ambient2_jul
            Calendar.AUGUST -> if (watchType == TYPE_1) R.drawable.ambient_aug else R.drawable.ambient2_aug
            Calendar.SEPTEMBER -> if (watchType == TYPE_1) R.drawable.ambient_sep else R.drawable.ambient2_sep
            Calendar.OCTOBER -> if (watchType == TYPE_1) R.drawable.ambient_oct else R.drawable.ambient2_oct
            Calendar.NOVEMBER -> if (watchType == TYPE_1) R.drawable.ambient_nov else R.drawable.ambient2_nov
            Calendar.DECEMBER -> if (watchType == TYPE_1) R.drawable.ambient_dec else R.drawable.ambient2_dec
            else -> if (watchType == TYPE_1) R.drawable.ambient_jan else R.drawable.ambient2_jan
        }
    }


    fun currentMonth(): Int {
        val month = Calendar.getInstance().get(
            Calendar.MONTH
        )
        return when (month) {
            Calendar.JANUARY -> R.drawable.jan
            Calendar.FEBRUARY -> R.drawable.feb
            Calendar.MARCH -> R.drawable.mar
            Calendar.APRIL -> R.drawable.apr
            Calendar.MAY -> R.drawable.may
            Calendar.JUNE -> R.drawable.jun
            Calendar.JULY -> R.drawable.jul
            Calendar.AUGUST -> R.drawable.aug
            Calendar.SEPTEMBER -> R.drawable.sep
            Calendar.OCTOBER -> R.drawable.oct
            Calendar.NOVEMBER -> R.drawable.nov
            Calendar.DECEMBER -> R.drawable.dec
            else -> R.drawable.jan
        }
    }

    fun currentNumberArray(value: Int): IntArray {
        val numberArray = IntArray(2)
        val remainder = value / 10
        val modValue = value % 10
        if (remainder == 0) {
            numberArray[0] = 0
        } else {
            numberArray[0] = remainder
        }
        numberArray[1] = modValue
        return numberArray
    }


    fun currentBatterNumber(number: Int): Int {
        return when (number) {
            0 -> R.drawable.battery0
            1 -> R.drawable.battery1
            2 -> R.drawable.battery2
            3 -> R.drawable.battery3
            4 -> R.drawable.battery4
            5 -> R.drawable.battery5
            6 -> R.drawable.battery6
            7 -> R.drawable.battery7
            8 -> R.drawable.battery8
            9 -> R.drawable.battery9
            else -> R.drawable.battery0
        }
    }

    fun currentTodayNumber(number: Int, watchType: Int): Int {
        return when (number) {
            0 -> if (watchType == TYPE_1) R.drawable.date_yellow0 else R.drawable.date_blue0
            1 -> if (watchType == TYPE_1) R.drawable.date_yellow1 else R.drawable.date_blue1
            2 -> if (watchType == TYPE_1) R.drawable.date_yellow2 else R.drawable.date_blue2
            3 -> if (watchType == TYPE_1) R.drawable.date_yellow3 else R.drawable.date_blue3
            4 -> if (watchType == TYPE_1) R.drawable.date_yellow4 else R.drawable.date_blue4
            5 -> if (watchType == TYPE_1) R.drawable.date_yellow5 else R.drawable.date_blue5
            6 -> if (watchType == TYPE_1) R.drawable.date_yellow6 else R.drawable.date_blue6
            7 -> if (watchType == TYPE_1) R.drawable.date_yellow7 else R.drawable.date_blue7
            8 -> if (watchType == TYPE_1) R.drawable.date_yellow8 else R.drawable.date_blue8
            9 -> if (watchType == TYPE_1) R.drawable.date_yellow9 else R.drawable.date_blue9
            else -> if (watchType == TYPE_1) R.drawable.date_yellow0 else R.drawable.date_blue0
        }
    }

    fun currentAmbientTodayNumber(number: Int, watchType: Int): Int {
        return when (number) {
            0 -> if (watchType == TYPE_1) R.drawable.date_ambient_yellow0 else R.drawable.date_ambient_blue0
            1 -> if (watchType == TYPE_1) R.drawable.date_ambient_yellow1 else R.drawable.date_ambient_blue1
            2 -> if (watchType == TYPE_1) R.drawable.date_ambient_yellow2 else R.drawable.date_ambient_blue2
            3 -> if (watchType == TYPE_1) R.drawable.date_ambient_yellow3 else R.drawable.date_ambient_blue3
            4 -> if (watchType == TYPE_1) R.drawable.date_ambient_yellow4 else R.drawable.date_ambient_blue4
            5 -> if (watchType == TYPE_1) R.drawable.date_ambient_yellow5 else R.drawable.date_ambient_blue5
            6 -> if (watchType == TYPE_1) R.drawable.date_ambient_yellow6 else R.drawable.date_ambient_blue6
            7 -> if (watchType == TYPE_1) R.drawable.date_ambient_yellow7 else R.drawable.date_ambient_blue7
            8 -> if (watchType == TYPE_1) R.drawable.date_ambient_yellow8 else R.drawable.date_ambient_blue8
            9 -> if (watchType == TYPE_1) R.drawable.date_ambient_yellow9 else R.drawable.date_ambient_blue9
            else -> if (watchType == TYPE_1) R.drawable.date_ambient_yellow0 else R.drawable.date_ambient_blue0
        }
    }


    fun currentAmbientIndexRes(indexType: Int, watchType: Int): Int {
        return when (indexType) {
            CIRCLE_TYPE -> if (watchType == TYPE_1) R.drawable.ambient_circle_index1 else R.drawable.ambient_circle_index2
            SQUARE_TYPE -> if (watchType == TYPE_1) R.drawable.ambient_square_index1 else R.drawable.ambient_square_index2
            TRIANGLE_TYPE -> if (watchType == TYPE_1) R.drawable.ambient_triangle_index1 else R.drawable.ambient_triangle_index2
            else -> if (watchType == TYPE_1) R.drawable.ambient_circle_index1 else R.drawable.ambient_circle_index2
        }
    }


    fun currentAmbientHourHandRes(watchType: Int): Int {
        return when (watchType) {
            TYPE_1 -> R.drawable.hour_ambient1
            TYPE_2 -> R.drawable.hour_ambient2
            else -> R.drawable.hour_ambient1
        }
    }

    fun currentAmbientMinuteHandRes(watchType: Int): Int {
        return when (watchType) {
            TYPE_1 -> R.drawable.minute_ambient1
            TYPE_2 -> R.drawable.minute_ambient2
            else -> R.drawable.minute_ambient1
        }
    }

    fun currentSecondsHandShadowRes(indexType: Int): Int {
        return when (indexType) {
            CIRCLE_TYPE -> R.drawable.seconds_circle_shadow
            SQUARE_TYPE -> R.drawable.seconds_square_shadow
            TRIANGLE_TYPE -> R.drawable.seconds_triangle_shadow
            else -> R.drawable.seconds_circle_shadow
        }
    }


    fun currentFrameRes(indexType: Int, watchType: Int): Int {
        return when (indexType) {
            CIRCLE_TYPE -> if (watchType == TYPE_1) R.drawable.circle_frame_yellow else R.drawable.circle_frame_blue
            SQUARE_TYPE -> if (watchType == TYPE_1) R.drawable.square_frame_yellow else R.drawable.square_frame_blue
            TRIANGLE_TYPE -> if (watchType == TYPE_1) R.drawable.triangle_frame_yellow else R.drawable.triangle_frame_blue
            else -> if (watchType == TYPE_1) R.drawable.circle_frame_yellow else R.drawable.circle_frame_blue
        }
    }

    fun currentFrameShadowRes(indexType: Int): Int {
        return when (indexType) {
            CIRCLE_TYPE -> R.drawable.frame_shadow1
            SQUARE_TYPE -> R.drawable.frame_shadow2
            TRIANGLE_TYPE -> R.drawable.frame_shadow3
            else -> R.drawable.frame_shadow1
        }
    }


    fun currentBgRes(indexType: Int, watchType: Int): Int {
        return when (indexType) {
            CIRCLE_TYPE -> if (watchType == TYPE_1) R.drawable.index_bg_mask11 else R.drawable.index_bg_mask2
            SQUARE_TYPE, TRIANGLE_TYPE -> if (watchType == TYPE_1) R.drawable.index_bg_mask1 else R.drawable.index_bg_mask2
            else -> if (watchType == TYPE_1) R.drawable.index_bg_mask1 else R.drawable.index_bg_mask2
        }
    }

    fun currentColorItemPosition(id: String): Int {
        return when (id) {
            ColorStyleIdAndResourceIds.YELLOW.id -> 0
            ColorStyleIdAndResourceIds.BLUE.id -> 1
            else -> 0
        }
    }


    fun currentShapeItemPosition(id: String): Int {
        return when (id) {
            ShapeStyleIdAndResourceIds.CIRCLE.id -> 0
            ShapeStyleIdAndResourceIds.SQUARE.id -> 1
            ShapeStyleIdAndResourceIds.TRIANGLE.id -> 2
            else -> 0
        }
    }


    fun nextResId(currentId: String): String {
        return when (currentId) {
            ShapeStyleIdAndResourceIds.CIRCLE.id -> ShapeStyleIdAndResourceIds.SQUARE.id
            ShapeStyleIdAndResourceIds.SQUARE.id -> ShapeStyleIdAndResourceIds.TRIANGLE.id
            ShapeStyleIdAndResourceIds.TRIANGLE.id -> ShapeStyleIdAndResourceIds.CIRCLE.id
            else -> ShapeStyleIdAndResourceIds.CIRCLE.id
        }
    }
}
