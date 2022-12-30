/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.mi.wearable.watchfacealbum.data.watchface

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.wear.watchface.style.UserStyleSetting
import androidx.wear.watchface.style.UserStyleSetting.ListUserStyleSetting
import com.android.mi.wearable.watchface5.R
/**
 * 相册表盘的样式
 */
const val ONE_STYLE_ID = "one_style_id"
private const val ONE_STYLE_NAME_RESOURCE_ID = R.string.one_style_name

const val TWO_STYLE_ID = "two_style_id"
private const val TWO_STYLE_NAME_RESOURCE_ID = R.string.two_style_name

const val THREE_STYLE_ID = "three_style_id"
private const val THREE_STYLE_NAME_RESOURCE_ID = R.string.three_style_name

//指针的样式
const val FOUR_STYLE_ID = "four_style_id"
private const val FOUR_STYLE_NAME_RESOURCE_ID = R.string.four_style_name

const val FIVE_STYLE_ID = "five_style_id"
private const val FIVE_STYLE_NAME_RESOURCE_ID = R.string.five_style_name

/**
 * Represents watch face color style options the user can select (includes the unique id, the
 * complication style resource id, and general watch face color style resource ids).
 *
 * The companion object offers helper functions to translate a unique string id to the correct enum
 * and convert all the resource ids to their correct resources (with the Context passed in). The
 * renderer will use these resources to render the actual colors and ComplicationDrawables of the
 * watch face.
 */
enum class StyleIdAndResourceIds(
    val id: String,
    @StringRes val nameResourceId: Int,
    @DrawableRes val watchFaceStyle: Int,
    //小组件的位置
    @DrawableRes val complicationTopStyleDrawableId : Int,
    @DrawableRes val complicationBottomStyleDrawableId : Int,

    //指针的样式
    /**
     * TODO 指针的样式
     */
    @DrawableRes val hourHand:Int,
    @DrawableRes val minuteHand:Int,
    @DrawableRes val secondHand:Int

    ) {
    //相册表盘样式
    STYLE1(
        id = ONE_STYLE_ID,
        nameResourceId = ONE_STYLE_NAME_RESOURCE_ID,
        watchFaceStyle = TYPE_1,
        //表盘样式
        complicationTopStyleDrawableId = R.drawable.complication_left_style1,
        complicationBottomStyleDrawableId = R.drawable.complication_left_style1,

        //指针 0表示当前没有
        hourHand = -1,
        minuteHand = -1,
        secondHand = -1
    ),

    STYLE2(
        id = TWO_STYLE_ID,
        nameResourceId = TWO_STYLE_NAME_RESOURCE_ID,
        watchFaceStyle = TYPE_2,

        complicationTopStyleDrawableId = R.drawable.complication_left_style1,
        complicationBottomStyleDrawableId = R.drawable.complication_left_style1,

        //指针 0表示当前没有
        hourHand = -1,
        minuteHand = -1,
        secondHand = -1
    ),

    STYLE3(
        id = THREE_STYLE_ID,
        nameResourceId = THREE_STYLE_NAME_RESOURCE_ID,
        watchFaceStyle = TYPE_3,

        complicationTopStyleDrawableId = R.drawable.complication_left_style1,
        complicationBottomStyleDrawableId = R.drawable.complication_left_style1,
        //指针 0表示当前没有
        hourHand = -1,
        minuteHand = -1,
        secondHand = -1
    ),

    STYLE4(
        id = FOUR_STYLE_ID,
        nameResourceId = FOUR_STYLE_NAME_RESOURCE_ID,
        watchFaceStyle = TYPE_4,

        complicationTopStyleDrawableId = R.drawable.complication_left_style1,
        complicationBottomStyleDrawableId = R.drawable.complication_left_style1,

        //指针
        hourHand = R.drawable.hour_ambient1,
        minuteHand = R.drawable.minute_ambient1,
        secondHand = R.drawable.seconds_circle_hand
    ),

    STYLE5(
        id = FIVE_STYLE_ID,
        nameResourceId = FIVE_STYLE_NAME_RESOURCE_ID,
        watchFaceStyle = TYPE_5,

        complicationTopStyleDrawableId = R.drawable.complication_left_style1,
        complicationBottomStyleDrawableId = R.drawable.complication_left_style1,

        //指针
        hourHand = R.drawable.hour_ambient2,
        minuteHand = R.drawable.minute_ambient2,
        secondHand = R.drawable.seconds_circle_hand

    );




    companion object {
        /**
         * Translates the string id to the correct ColorStyleIdAndResourceIds object.
         */
        fun getStyleConfig(id: String): StyleIdAndResourceIds {
            return when (id) {
                STYLE1.id -> STYLE1
                STYLE2.id -> STYLE2
                STYLE3.id -> STYLE3
                //指针
                STYLE4.id -> STYLE4
                STYLE5.id -> STYLE5
                else -> STYLE1
            }
        }

        /**
         * Returns a list of [UserStyleSetting.ListUserStyleSetting.ListOption] for all
         * ColorStyleIdAndResourceIds enums. The watch face settings APIs use this to set up
         * options for the user to select a style.
         */
        fun toOptionList(context: Context): List<ListUserStyleSetting.ListOption> {
            val styleIdAndResourceIdsList = enumValues<StyleIdAndResourceIds>()

            return styleIdAndResourceIdsList.map { styleIdAndResourceIds ->
                ListUserStyleSetting.ListOption(
                    UserStyleSetting.Option.Id(styleIdAndResourceIds.id),
                    context.resources,
                    styleIdAndResourceIds.nameResourceId,
                    null
                )
            }
        }
    }
}
