package com.android.mi.wearable.watchfacealbum.utils
import com.android.mi.wearable.watchface5.R
import com.android.mi.wearable.watchfacealbum.data.watchface.BATTERY
import com.android.mi.wearable.watchfacealbum.data.watchface.StyleIdAndResourceIds
import com.android.mi.wearable.watchfacealbum.data.watchface.TYPE_1
import java.util.*

object BitmapTranslateUtils {
    /***
    相册表盘style1
     ***/
//weekday
    fun currentWeekdayStyle1(): Int {
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
    //关于电量的处理
    fun currentBatteryPercentToRes(batteryNumber: Float?):Int{
        if (batteryNumber != null) {
            if (batteryNumber>90 && batteryNumber<=100){
                return R.drawable.camera_wf_style1_battery_icon_100
            }else if (batteryNumber==0f){
                return R.drawable.camera_wf_style1_battery_icon_0
            }else if (batteryNumber>0 && batteryNumber<=10){
                return R.drawable.camera_wf_style1_battery_icon_10
            }else if (batteryNumber>10 && batteryNumber<=20){
                return R.drawable.camera_wf_style1_battery_icon_20
            }else if (batteryNumber>20 && batteryNumber<=30){
                return R.drawable.camera_wf_style1_battery_icon_30
            }else if (batteryNumber>30 && batteryNumber<=40){
                return R.drawable.camera_wf_style1_battery_icon_40
            }else if (batteryNumber>40 && batteryNumber<=50){
                return R.drawable.camera_wf_style1_battery_icon_50
            }else if (batteryNumber>50 && batteryNumber<=60){
                return R.drawable.camera_wf_style1_battery_icon_60
            }else if (batteryNumber>60 && batteryNumber<=70){
                return R.drawable.camera_wf_style1_battery_icon_70
            }else if(batteryNumber>70 && batteryNumber<=80){
                return R.drawable.camera_wf_style1_battery_icon_80
            }else if(batteryNumber>80 && batteryNumber<=90){
                return R.drawable.camera_wf_style1_battery_icon_90
            }
        }
        return R.drawable.camera_wf_style1_battery_icon_0
    }

//mouth and day 转换函数
    private fun monthAndDayToResStyle1(
    watchType: Int,
    mouth: Int,
    drawableResArray: Array<Int>,
    i: Int
    ){
        when (mouth) {
            0 -> drawableResArray[i] = R.drawable.camera_wf_style1_date_digital_0
            1 -> drawableResArray[i] = R.drawable.camera_wf_style1_date_digital_1
            2 -> drawableResArray[i] = R.drawable.camera_wf_style1_date_digital_2
            3 -> drawableResArray[i] = R.drawable.camera_wf_style1_date_digital_3
            4 -> drawableResArray[i] = R.drawable.camera_wf_style1_date_digital_4
            5 -> drawableResArray[i] =R.drawable.camera_wf_style1_date_digital_5
            6 -> drawableResArray[i] = R.drawable.camera_wf_style1_date_digital_6
            7 -> drawableResArray[i] = R.drawable.camera_wf_style1_date_digital_7
            8 -> drawableResArray[i] = R.drawable.camera_wf_style1_date_digital_8
            9 -> drawableResArray[i] = R.drawable.camera_wf_style1_date_digital_9
            else -> drawableResArray[i] = R.drawable.camera_wf_style1_date_digital_0
        }
    }
//time  big 和 small 转换函数
    private fun timeDigitalToResStyle1(
    watchType: Int,
    digital: Int,
    drawableResArray: Array<Int>,
    i: Int
    ) {
        //时间大字体
        if (watchType == TYPE_1) {
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
            //时间小字体
        } else {
            when (digital) {
                0-> drawableResArray[i] = R.drawable.camera_wf_style1_time_big_0
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
        }
    }
    //对于月份和日期的处理，显示在一格数组中
    fun currentMonthAndDayStyle1(watchType: Int) : Array<Int> {
        //存放当前的日期的数组
        val drawableResArray = arrayOf(
            R.drawable.camera_wf_style1_date_digital_0,
            R.drawable.camera_wf_style1_date_digital_0,
            R.drawable.camera_wf_style1_date_digital_spit_sign,
            R.drawable.camera_wf_style1_date_digital_0,
            R.drawable.camera_wf_style1_date_digital_0)
        //获取当前月份
        val month = Calendar.getInstance().get(Calendar.MONTH)
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        //判断当前的是否需要显示分隔符
        drawableResArray[2] = R.drawable.camera_wf_style1_date_digital_spit_sign
        //小时和分钟十位和各位
        val monthTen  = month / 10 % 10
        val monthBit = month % 10
        val dayTen = day / 10 %10
        val dayBit = day % 10
        //将图片放置在对应的位置
        monthAndDayToResStyle1(watchType, monthTen, drawableResArray,0)
        monthAndDayToResStyle1(watchType, monthBit, drawableResArray,1)
        monthAndDayToResStyle1(watchType, dayTen, drawableResArray,3)
        monthAndDayToResStyle1(watchType, dayBit, drawableResArray,4)
        return drawableResArray
    }
    //对于时间的处理逻辑显示在一个数组中
    fun currentHourAndMinuteStyle1(watchType: Int): Array<Int> {
        //存放当前时间的数组
        val drawableResArray = arrayOf(
            R.drawable.camera_wf_style1_time_big_0,
            R.drawable.camera_wf_style1_time_big_0,
            R.drawable.camera_wf_style1_time_big_colon,
            R.drawable.camera_wf_style1_time_big_0,
            R.drawable.camera_wf_style1_time_big_0,)
        //获取当前的小时
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        //获取当前的分钟
        val minute = Calendar.getInstance().get(Calendar.MINUTE)
        //判断当前的是否需要显示分隔符
        drawableResArray[2] = R.drawable.camera_wf_style1_time_small_colon
        //小时和分钟十位和各位
        val hourTen  = hour / 10 % 10
        val hourBit = hour % 10
        val minuteTen = minute / 10 %10
        val minuteBit = minute % 10
        //防止是三个图片
        timeDigitalToResStyle1(watchType, hourTen, drawableResArray,0)
        timeDigitalToResStyle1(watchType, hourBit, drawableResArray,1)
        timeDigitalToResStyle1(watchType, minuteTen, drawableResArray,3)
        timeDigitalToResStyle1(watchType, minuteBit, drawableResArray,4)
        return drawableResArray
    }

//判断当前style
    fun currentColorItemPosition(id: String): Int {
        return when (id) {
            StyleIdAndResourceIds.STYLE1.id -> 0
            StyleIdAndResourceIds.STYLE2.id -> 1
            StyleIdAndResourceIds.STYLE3.id -> 2
            StyleIdAndResourceIds.STYLE4.id ->3
            StyleIdAndResourceIds.STYLE5.id ->4
            else -> 0
        }
    }

    fun currentFunctionStyle(watchFaceStyle: Int, funcType: Int): Int {
        when (watchFaceStyle) {
            TYPE_1 -> {
                return when (funcType) {
                    BATTERY -> R.drawable.camera_wf_style1_battery_icon_0
                    else -> 0
                }
            }
        }
        return 0
    }


}
