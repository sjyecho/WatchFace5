package com.android.mi.wearable.watchface5

import android.util.Log
import android.view.SurfaceHolder
import androidx.wear.watchface.*
import androidx.wear.watchface.style.CurrentUserStyleRepository
import androidx.wear.watchface.style.UserStyle
import androidx.wear.watchface.style.UserStyleSchema
import androidx.wear.watchface.style.UserStyleSetting
import com.android.mi.wearable.watchface5.data.watchface.ColorStyleIdAndResourceIds
import com.android.mi.wearable.watchface5.data.watchface.ShapeStyleIdAndResourceIds
import com.android.mi.wearable.watchface5.data.watchface.indexTypeArray
import com.android.mi.wearable.watchface5.utils.BitmapTranslateUtils
import com.android.mi.wearable.watchface5.utils.SHAPE_STYLE_SETTING
import com.android.mi.wearable.watchface5.utils.createUserStyleSchema
import kotlin.random.Random
import kotlin.random.nextInt

class WatchFaceService5 : WatchFaceService(), WatchFace.TapListener{
    private lateinit var shapeStyleKey: UserStyleSetting.ListUserStyleSetting
    private var nextShapeId: String = ShapeStyleIdAndResourceIds.SQUARE.id
    private lateinit var mCurrentUserStyleRepository: CurrentUserStyleRepository

    override fun createUserStyleSchema(): UserStyleSchema = createUserStyleSchema(context = applicationContext)

    override suspend fun createWatchFace(
        surfaceHolder: SurfaceHolder,
        watchState: WatchState,
        complicationSlotsManager: ComplicationSlotsManager,
        currentUserStyleRepository: CurrentUserStyleRepository
    ): WatchFace {
        mCurrentUserStyleRepository = currentUserStyleRepository
        // Creates class that renders the watch face.
        val renderer = WatchFace3CanvasRenderer(
            context = applicationContext,
            surfaceHolder = surfaceHolder,
            watchState = watchState,
            complicationSlotsManager = complicationSlotsManager,
            currentUserStyleRepository = currentUserStyleRepository,
            canvasType = CanvasType.HARDWARE
        )

        //Creates the watch face
        val watchFace = WatchFace(
            watchFaceType = WatchFaceType.ANALOG,
            renderer = renderer
        )
        watchFace.setTapListener(this)

        return watchFace
    }

    override fun onTapEvent(tapType: Int, tapEvent: TapEvent, complicationSlot: ComplicationSlot?) {
        val style: UserStyle = mCurrentUserStyleRepository.userStyle.value
        val userStyleSettingList = mCurrentUserStyleRepository.schema.userStyleSettings
        nextShapeId = BitmapTranslateUtils.nextResId(nextShapeId)
        style.apply {
            for (userStyleSetting in userStyleSettingList){
                if(userStyleSetting.id == UserStyleSetting.Id(SHAPE_STYLE_SETTING)){
                    shapeStyleKey = userStyleSetting as UserStyleSetting.ListUserStyleSetting
                    for (settingOption in userStyleSetting.options) {
                        if (settingOption.id.toString() == nextShapeId) {
                            val newShapeStyle = mCurrentUserStyleRepository.userStyle.value.toMutableUserStyle()
                            newShapeStyle[shapeStyleKey] = settingOption
                            mCurrentUserStyleRepository.updateUserStyle(newShapeStyle.toUserStyle())
                        }
                    }

                }
            }
        }
    }
}