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
const val BATTERY = 1;


/**
 * index type
 */
const val CIRCLE_TYPE: Int = 1
const val SQUARE_TYPE: Int = 2
const val TRIANGLE_TYPE: Int = 3
val indexTypeArray: IntArray = IntArray(3).apply {
   this[0] = CIRCLE_TYPE
   this[1] = SQUARE_TYPE
   this[2] = TRIANGLE_TYPE
}
data class WatchFaceData(
    val activeColorStyle: ColorStyleIdAndResourceIds = ColorStyleIdAndResourceIds.STYLE1,
//    val ambientColorStyle: ColorStyleIdAndResourceIds = ColorStyleIdAndResourceIds.AMBIENT_YELLOW,
    val shapeStyle: ShapeStyleIdAndResourceIds = ShapeStyleIdAndResourceIds.CIRCLE,
)
