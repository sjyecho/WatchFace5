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
 * 相册表盘的样式
 */
const val ONE_STYLE_ID = "one_style_id"
private const val ONE_STYLE_NAME_RESOURCE_ID = R.string.one_style_name

const val TWO_STYLE_ID = "two_style_id"
private const val TWO_STYLE_NAME_RESOURCE_ID = R.string.two_style_name

const val THREE_STYLE_ID = "three_style_id"
private const val THREE_STYLE_NAME_RESOURCE_ID = R.string.three_style_name


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
    @DrawableRes val watchFaceStyle: Int,
) {
    //相册表盘样式
    STYLE1(
        id = ONE_STYLE_ID,
        nameResourceId = ONE_STYLE_NAME_RESOURCE_ID,
        watchFaceStyle = TYPE_1,
    ),

    STYLE2(
        id = TWO_STYLE_ID,
        nameResourceId = TWO_STYLE_NAME_RESOURCE_ID,
        watchFaceStyle = TYPE_2,
    ),

    STYLE3(
        id = THREE_STYLE_ID,
        nameResourceId = THREE_STYLE_NAME_RESOURCE_ID,
        watchFaceStyle = TYPE_3
    );

    companion object {
        /**
         * Translates the string id to the correct ColorStyleIdAndResourceIds object.
         */
        fun getColorStyleConfig(id: String): ColorStyleIdAndResourceIds {
            return when (id) {
//                AMBIENT.id -> AMBIENT
                STYLE1.id -> STYLE1
                STYLE2.id -> STYLE2
                STYLE3.id -> STYLE3
                else -> STYLE1
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
