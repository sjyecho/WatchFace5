/*
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
package com.android.mi.wearable.watchfacealbum.utils

import android.content.Context
import android.graphics.RectF
import androidx.wear.watchface.CanvasComplicationFactory
import androidx.wear.watchface.ComplicationSlot
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.complications.ComplicationSlotBounds
import androidx.wear.watchface.complications.DefaultComplicationDataSourcePolicy
import androidx.wear.watchface.complications.SystemDataSources
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.rendering.CanvasComplicationDrawable
import androidx.wear.watchface.complications.rendering.ComplicationDrawable
import androidx.wear.watchface.style.CurrentUserStyleRepository
import com.android.mi.wearable.watchface5.R

// Information needed for complications.
// Creates bounds for the locations of both right and left complications. (This is the
// location from 0.0 - 1.0.)
// Both left and right complications use the same top and bottom bounds.
private const val LEFT_AND_RIGHT_COMPLICATIONS_TOP_BOUND = 0.4f
private const val LEFT_AND_RIGHT_COMPLICATIONS_BOTTOM_BOUND = 0.7f

private const val LEFT_COMPLICATION_LEFT_BOUND = 0.2f
private const val LEFT_COMPLICATION_RIGHT_BOUND = 0.5f

private const val RIGHT_COMPLICATION_LEFT_BOUND = 0.6f
private const val RIGHT_COMPLICATION_RIGHT_BOUND = 0.8f

private const val DEFAULT_COMPLICATION_STYLE_DRAWABLE_ID = R.drawable.complication_left_style1

private const val DEFAULT_COMPLICATION_STYLE_DRAWABLE_ID_LEFT = R.drawable.complication_left_style1

// Unique IDs for each complication. The settings activity that supports allowing users
// to select their complication data provider requires numbers to be >= 0.
internal const val LEFT_COMPLICATION_ID = 100
internal const val RIGHT_COMPLICATION_ID = 101

/**
 * Represents the unique id associated with a complication and the complication types it supports.
 */
sealed class ComplicationConfig(val id: Int, val supportedTypes: List<ComplicationType>) {
    object Left : ComplicationConfig(
        LEFT_COMPLICATION_ID,
        listOf(
            ComplicationType.PHOTO_IMAGE,
            ComplicationType.RANGED_VALUE,
            ComplicationType.MONOCHROMATIC_IMAGE,
            ComplicationType.SHORT_TEXT,
            ComplicationType.SMALL_IMAGE,
        )
    )

    object Right : ComplicationConfig(
        RIGHT_COMPLICATION_ID,
        listOf(
            ComplicationType.PHOTO_IMAGE,
            ComplicationType.RANGED_VALUE,
            ComplicationType.MONOCHROMATIC_IMAGE,
            ComplicationType.SHORT_TEXT,
            ComplicationType.SMALL_IMAGE
        )
    )
}

// Utility function that initializes default complication slots (left and right).
fun createComplicationSlotManager(
    context: Context,
    currentUserStyleRepository: CurrentUserStyleRepository,
    drawableLeftId: Int = DEFAULT_COMPLICATION_STYLE_DRAWABLE_ID_LEFT,
    drawableRightId: Int = DEFAULT_COMPLICATION_STYLE_DRAWABLE_ID
): ComplicationSlotsManager {
    val defaultRightCanvasComplicationFactory =
        CanvasComplicationFactory { watchState, listener ->
            CanvasComplicationDrawable(
                ComplicationDrawable.getDrawable(context, drawableRightId)!!,
                watchState,
                listener
            )
        }

    val defaultLeftCanvasComplicationFactory =
        CanvasComplicationFactory { watchState, listener ->
            CanvasComplicationDrawable(
                ComplicationDrawable.getDrawable(context, drawableLeftId)!!,
                watchState,
                listener
            )
        }

    val leftComplication = ComplicationSlot.createRoundRectComplicationSlotBuilder(
        id = ComplicationConfig.Left.id,
        canvasComplicationFactory = defaultLeftCanvasComplicationFactory,
        supportedTypes = ComplicationConfig.Left.supportedTypes,
        defaultDataSourcePolicy = DefaultComplicationDataSourcePolicy(
            SystemDataSources.DATA_SOURCE_APP_SHORTCUT,
            ComplicationType.SHORT_TEXT
        ),
        bounds = ComplicationSlotBounds(
            RectF(
                0.09f,
                0.36f,
                0.36f,
                0.63f
            )
        )
    ).build()

    val rightComplication = ComplicationSlot.createRoundRectComplicationSlotBuilder(
        id = ComplicationConfig.Right.id,
        canvasComplicationFactory = defaultRightCanvasComplicationFactory,
        supportedTypes = ComplicationConfig.Right.supportedTypes,
        defaultDataSourcePolicy = DefaultComplicationDataSourcePolicy(
            SystemDataSources.DATA_SOURCE_WATCH_BATTERY,
            ComplicationType.SHORT_TEXT
        ),
        bounds = ComplicationSlotBounds(
            RectF(
                0.64f,
                0.36f,
                0.91f,
                0.63f
            )
        )
    ).build()

    return ComplicationSlotsManager(
        listOf(leftComplication,rightComplication),
        currentUserStyleRepository
    )
}
