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
package com.android.mi.wearable.watchface5.data.watchface

import android.content.Context
import android.graphics.drawable.Icon
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.wear.watchface.style.UserStyleSetting
import androidx.wear.watchface.style.UserStyleSetting.ListUserStyleSetting
import com.android.mi.wearable.watchface5.R

// Defaults for all styles.
// X_COLOR_STYLE_ID - id in watch face database for each style id.
// X_COLOR_STYLE_NAME_RESOURCE_ID - String name to display in the user settings UI for the style.
// X_COLOR_STYLE_ICON_ID - Icon to display in the user settings UI for the style.
const val AMBIENT_YELLOW_COLOR_STYLE_ID = "ambient_yellow_style_id"
private const val AMBIENT_YELLOW_COLOR_STYLE_NAME_RESOURCE_ID = R.string.ambient_yellow_style_name
//private const val AMBIENT_YELLOW_COLOR_STYLE_ICON_ID = R.drawable.complication_left_style1

const val AMBIENT_BLUE_COLOR_STYLE_ID = "ambient_blue_style_id"
private const val AMBIENT_BLUE_COLOR_STYLE_NAME_RESOURCE_ID = R.string.ambient_blue_style_name
//private const val AMBIENT_BLUE_COLOR_STYLE_ICON_ID = R.drawable.complication_left_style1

const val YELLOW_COLOR_STYLE_ID = "yellow_style_id"
private const val YELLOW_COLOR_STYLE_NAME_RESOURCE_ID = R.string.yellow_style_name
//private const val RED_COLOR_STYLE_ICON_ID = R.drawable.complication_left_style1

const val BLUE_COLOR_STYLE_ID = "blue_style_id"
private const val BLUE_COLOR_STYLE_NAME_RESOURCE_ID = R.string.blue_style_name
//private const val GREEN_COLOR_STYLE_ICON_ID = R.drawable.complication_left_style1


/**
 * Represents watch face color style options the user can select (includes the unique id, the
 * complication style resource id, and general watch face color style resource ids).
 *
 * The companion object offers helper functions to translate a unique string id to the correct enum
 * and convert all the resource ids to their correct resources (with the Context passed in). The
 * renderer will use these resources to render the actual colors and ComplicationDrawables of the
 * watch face.
 */
enum class ColorStyleIdAndResourceIds(
    val id: String,
    @StringRes val nameResourceId: Int,
    @DrawableRes val hourHandRight: Int,
    @DrawableRes val hourHandLeft: Int,
    @DrawableRes val hourHandShadow: Int,
    @DrawableRes val minuteHandRight: Int,
    @DrawableRes val minuteHandLeft: Int,
    @DrawableRes val minuteHandShadow: Int,
    @DrawableRes val pointHand: Int,
    @DrawableRes val watchFaceStyle: Int,
) {
    YELLOW(
        id = YELLOW_COLOR_STYLE_ID,
        nameResourceId = YELLOW_COLOR_STYLE_NAME_RESOURCE_ID,
        hourHandRight = R.drawable.hour_hand_right1,
        hourHandLeft = R.drawable.hour_hand_left1,
        hourHandShadow = R.drawable.hour_hand_shadow,
        minuteHandRight = R.drawable.minute_right_hand1,
        minuteHandLeft = R.drawable.minute_left_hand1,
        minuteHandShadow = R.drawable.minute_hand_shadow,
        pointHand = R.drawable.hand_point,
        watchFaceStyle = TYPE_1,
    ),

    BLUE(
        id = BLUE_COLOR_STYLE_ID,
        nameResourceId = BLUE_COLOR_STYLE_NAME_RESOURCE_ID,
        hourHandRight = R.drawable.hour_hand_right2,
        hourHandLeft = R.drawable.hour_hand_left2,
        hourHandShadow = R.drawable.hour_hand_shadow,
        minuteHandRight = R.drawable.minute_right_hand2,
        minuteHandLeft = R.drawable.minute_left_hand2,
        minuteHandShadow = R.drawable.minute_hand_shadow,
        pointHand = R.drawable.hand_point,
        watchFaceStyle = TYPE_2,
    );

    companion object {
        /**
         * Translates the string id to the correct ColorStyleIdAndResourceIds object.
         */
        fun getColorStyleConfig(id: String): ColorStyleIdAndResourceIds {
            return when (id) {
//                AMBIENT.id -> AMBIENT
                YELLOW.id -> YELLOW
                BLUE.id -> BLUE
                else -> YELLOW
            }
        }

        /**
         * Returns a list of [UserStyleSetting.ListUserStyleSetting.ListOption] for all
         * ColorStyleIdAndResourceIds enums. The watch face settings APIs use this to set up
         * options for the user to select a style.
         */
        fun toOptionList(context: Context): List<ListUserStyleSetting.ListOption> {
            val colorStyleIdAndResourceIdsList = enumValues<ColorStyleIdAndResourceIds>()

            return colorStyleIdAndResourceIdsList.map { colorStyleIdAndResourceIds ->
                ListUserStyleSetting.ListOption(
                    UserStyleSetting.Option.Id(colorStyleIdAndResourceIds.id),
                    context.resources,
                    colorStyleIdAndResourceIds.nameResourceId,
                    null
                )
            }
        }
    }
}
