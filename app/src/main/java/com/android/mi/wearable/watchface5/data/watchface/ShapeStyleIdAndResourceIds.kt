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
const val CIRCLE_SHAPE_STYLE_ID = "circle_shape_style_id"
private const val CIRCLE_SHAPE_STYLE_NAME_RESOURCE_ID = R.string.circle_shape_style_name
//private const val AMBIENT_YELLOW_COLOR_STYLE_ICON_ID = R.drawable.complication_left_style1

const val SQUARE_SHAPE_STYLE_ID = "square_shape_style_id"
private const val SQUARE_SHAPE_STYLE_NAME_RESOURCE_ID = R.string.square_shape_style_name
//private const val AMBIENT_BLUE_COLOR_STYLE_ICON_ID = R.drawable.complication_left_style1

const val TRIANGLE_SHAPE_STYLE_ID = "triangle_shape_style_id"
private const val TRIANGLE_SHAPE_STYLE_NAME_RESOURCE_ID = R.string.triangle_shape_style_name
//private const val RED_COLOR_STYLE_ICON_ID = R.drawable.complication_left_style1


/**
 * Represents watch face color style options the user can select (includes the unique id, the
 * complication style resource id, and general watch face color style resource ids).
 *
 * The companion object offers helper functions to translate a unique string id to the correct enum
 * and convert all the resource ids to their correct resources (with the Context passed in). The
 * renderer will use these resources to render the actual colors and ComplicationDrawables of the
 * watch face.
 */
enum class ShapeStyleIdAndResourceIds(
    val id: String,
    @StringRes val nameResourceId: Int,
    @DrawableRes val secondsHand: Int,
    @DrawableRes val secondsHandShadow: Int,
    @DrawableRes val index: Int,
    @DrawableRes val frameShape: Int,
    @DrawableRes val frameShadow: Int,
    @DrawableRes val shapeType: Int,
) {
    CIRCLE(
        id = CIRCLE_SHAPE_STYLE_ID,
        nameResourceId = CIRCLE_SHAPE_STYLE_NAME_RESOURCE_ID,
        secondsHand = R.drawable.seconds_circle_hand,
        secondsHandShadow = R.drawable.seconds_circle_shadow,
        index = R.drawable.circle_index,
        frameShape =  R.drawable.circle_frame_yellow,
        frameShadow = R.drawable.frame_shadow1,
        shapeType = CIRCLE_TYPE,
    ),

    SQUARE(
        id = SQUARE_SHAPE_STYLE_ID,
        nameResourceId = SQUARE_SHAPE_STYLE_NAME_RESOURCE_ID,
        secondsHand = R.drawable.seconds_square_hand,
        secondsHandShadow = R.drawable.seconds_square_shadow,
        index = R.drawable.square_index,
        frameShape =  R.drawable.square_frame_yellow,
        frameShadow = R.drawable.frame_shadow2,
        shapeType = SQUARE_TYPE,
    ),

    TRIANGLE(
        id = TRIANGLE_SHAPE_STYLE_ID,
        nameResourceId = TRIANGLE_SHAPE_STYLE_NAME_RESOURCE_ID,
        secondsHand = R.drawable.seconds_triangle_hand,
        secondsHandShadow = R.drawable.seconds_triangle_shadow,
        index = R.drawable.triangle_index,
        frameShape =  R.drawable.triangle_frame_yellow,
        frameShadow = R.drawable.frame_shadow3,
        shapeType = TRIANGLE_TYPE,
    );

    companion object {
        /**
         * Translates the string id to the correct ColorStyleIdAndResourceIds object.
         */
        fun getShapeStyleConfig(id: String): ShapeStyleIdAndResourceIds {
            return when (id) {
                CIRCLE.id -> CIRCLE
                SQUARE.id -> SQUARE
                TRIANGLE.id -> TRIANGLE
                else -> CIRCLE
            }
        }

        /**
         * Returns a list of [UserStyleSetting.ListUserStyleSetting.ListOption] for all
         * ColorStyleIdAndResourceIds enums. The watch face settings APIs use this to set up
         * options for the user to select a style.
         */
        fun toOptionList(context: Context): List<ListUserStyleSetting.ListOption> {
            val shapeStyleIdAndResourceIdsList = enumValues<ShapeStyleIdAndResourceIds>()
            return shapeStyleIdAndResourceIdsList.map { shapeStyleIdAndResourceIds ->
                ListUserStyleSetting.ListOption(
                    UserStyleSetting.Option.Id(shapeStyleIdAndResourceIds.id),
                    context.resources,
                    shapeStyleIdAndResourceIds.nameResourceId,
                    null
                )
            }
        }
    }
}
