package com.android.mi.wearable.watchfacealbum.utils/*
 * Copyright 2020 The Android Open Source Project
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

import android.content.Context
import androidx.wear.watchface.style.UserStyleSchema
import androidx.wear.watchface.style.UserStyleSetting
import androidx.wear.watchface.style.WatchFaceLayer
import com.android.mi.wearable.watchface5.R
import com.android.mi.wearable.watchfacealbum.data.watchface.StyleIdAndResourceIds
// Keys to matched content in the  the user style settings. We listen for changes to these
// values in the renderer and if new, we will update the database and update the watch face
// being rendered.
//相册表盘的样式
const val STYLE_SETTING = "style_setting"

const val SHAPE_STYLE_SETTING = "shape_style_setting"


//const val DRAW_HOUR_PIPS_STYLE_SETTING = "draw_hour_pips_style_setting"
//const val WATCH_HAND_LENGTH_STYLE_SETTING = "watch_hand_length_style_setting"

/*
 * Creates user styles in the settings activity associated with the watch face, so users can
 * edit different parts of the watch face. In the renderer (after something has changed), the
 * watch face listens for a flow from the watch face API data layer and updates the watch face.
 */
fun createUserStyleSchema(context: Context): UserStyleSchema {
    //相册表盘对于位置样式的选择
    // 1. Allows user to change the color styles of the watch face (if any are available).
    val styleSetting =
        UserStyleSetting.ListUserStyleSetting(
            UserStyleSetting.Id(STYLE_SETTING),
            context.resources,
            R.string.colors_style_setting,
            R.string.colors_style_setting_description,
            null,
            StyleIdAndResourceIds.toOptionList(context),
            listOf(
                WatchFaceLayer.BASE,
                WatchFaceLayer.COMPLICATIONS,
                WatchFaceLayer.COMPLICATIONS_OVERLAY
            )
        )

    // 4. Create style settings to hold all options.
    return UserStyleSchema(
        listOf(
            styleSetting,
        )
    )
}
