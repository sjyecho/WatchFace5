package com.android.mi.wearable.watchfacealbum

import android.view.SurfaceHolder
import androidx.wear.watchface.*
import androidx.wear.watchface.style.CurrentUserStyleRepository
import androidx.wear.watchface.style.UserStyleSchema
import com.android.mi.wearable.watchfacealbum.utils.createComplicationSlotManager
import com.android.mi.wearable.watchfacealbum.utils.createUserStyleSchema

class WatchFaceAlbumService : WatchFaceService(){
    private lateinit var mCurrentUserStyleRepository: CurrentUserStyleRepository

    override fun createUserStyleSchema(): UserStyleSchema = createUserStyleSchema(context = applicationContext)

    override fun createComplicationSlotsManager(currentUserStyleRepository: CurrentUserStyleRepository): ComplicationSlotsManager {
        return createComplicationSlotManager(
            context = applicationContext,
            currentUserStyleRepository = currentUserStyleRepository
        )
    }
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
        return WatchFace(
            watchFaceType = WatchFaceType.DIGITAL,
            renderer = renderer
        )
    }

}