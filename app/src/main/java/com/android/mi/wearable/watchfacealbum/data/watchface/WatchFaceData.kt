package com.android.mi.wearable.watchface5.data.watchface


/**
 * watchface style
 */
//三个style
//也就是三个位置
const val TYPE_1 = 1
const val TYPE_2 = 2
const val TYPE_3 = 3
/**
 * complicationType
 */
//无、电量、步数、心率、卡路里、天气温度、血氧。
const val BATTERY = 1
const val STEP_COUNT = 2
const val HEART_RATE = 3
const val CALORIE = 4
const val WEATHER_TEMP = 5
const val BLOOD_OXYGEN = 6


data class WatchFaceData(
    val activeColorStyle: ColorStyleIdAndResourceIds = ColorStyleIdAndResourceIds.STYLE1,
)
