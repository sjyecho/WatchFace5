package com.android.mi.wearable.watchface5

import android.view.SurfaceHolder
import androidx.wear.watchface.*
import androidx.wear.watchface.style.CurrentUserStyleRepository
import androidx.wear.watchface.style.UserStyle
import androidx.wear.watchface.style.UserStyleSchema
import androidx.wear.watchface.style.UserStyleSetting
import com.android.mi.wearable.watchface5.utils.BitmapTranslateUtils
import com.android.mi.wearable.watchface5.utils.SHAPE_STYLE_SETTING
import com.android.mi.wearable.watchface5.utils.createUserStyleSchema

class WatchFaceAlbumService : WatchFaceService(){
    private lateinit var shapeStyleKey: UserStyleSetting.ListUserStyleSetting
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
            watchFaceType = WatchFaceType.DIGITAL,
            renderer = renderer
        )
        return watchFace
    }

}