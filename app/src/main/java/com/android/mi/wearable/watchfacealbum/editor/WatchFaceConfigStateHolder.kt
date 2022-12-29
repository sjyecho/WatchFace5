package com.android.mi.wearable.watchface5.editor

import android.graphics.Bitmap
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.wear.watchface.DrawMode
import androidx.wear.watchface.RenderParameters
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.editor.EditorSession
import androidx.wear.watchface.style.UserStyle
import androidx.wear.watchface.style.UserStyleSchema
import androidx.wear.watchface.style.UserStyleSetting
import androidx.wear.watchface.style.WatchFaceLayer
import com.android.mi.wearable.watchface5.utils.COLOR_STYLE_SETTING
import com.android.mi.wearable.watchface5.utils.SHAPE_STYLE_SETTING
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class WatchFaceConfigStateHolder (
    private val scope: CoroutineScope,
    private val activity: ComponentActivity
        ){
    private lateinit var editorSession: EditorSession

    // Keys from Watch Face Data Structure
    private lateinit var colorStyleKey: UserStyleSetting.ListUserStyleSetting

    // Keys from Watch Face Data Structure
    private lateinit var shapeStyleKey: UserStyleSetting.ListUserStyleSetting
//    var pageType: Int = 0
    val uiState: StateFlow<EditWatchFaceUiState> =
        flow<EditWatchFaceUiState> {
            editorSession = EditorSession.createOnWatchEditorSession(
                activity = activity
            )

            extractsUserStyles(editorSession.userStyleSchema)

            emitAll(
                combine(
                    editorSession.userStyle,
                    editorSession.complicationsPreviewData
                ) { userStyle, complicationsPreviewData ->
                    yield()
                    EditWatchFaceUiState.Success(
                        createWatchFacePreview(userStyle, complicationsPreviewData)
                    )
                }
            )
        }
            .stateIn(
                scope + Dispatchers.Main.immediate,
                SharingStarted.Eagerly,
                EditWatchFaceUiState.Loading("Initializing")
            )

    private fun extractsUserStyles(userStyleSchema: UserStyleSchema) {
        // Loops through user styles and retrieves user editable styles.
        for (setting in userStyleSchema.userStyleSettings) {
            when (setting.id.toString()) {
                COLOR_STYLE_SETTING -> {
                    colorStyleKey = setting as UserStyleSetting.ListUserStyleSetting
                }
//
                SHAPE_STYLE_SETTING -> {
                    shapeStyleKey = setting as UserStyleSetting.ListUserStyleSetting
                }
//
//                WATCH_HAND_LENGTH_STYLE_SETTING -> {
//                    minuteHandLengthKey = setting as UserStyleSetting.DoubleRangeUserStyleSetting
//                }
                // TODO (codingjeremy): Add complication change support if settings activity
                // PR doesn't cover it. Otherwise, remove comment.
            }
        }
    }


    fun createWatchFacePreview(): Bitmap{
//        val highlightLayer = if (showHighlightLayer) RenderParameters.HighlightLayer(
//            RenderParameters.HighlightedElement.AllComplicationSlots,
//            activity.getColor(R.color.complication_border), // Red complication highlight.
//            Color.argb(128, 0, 0, 0) // Darken everything else.
//        ) else null
        return editorSession.renderWatchFaceToBitmap(
            RenderParameters(
                DrawMode.INTERACTIVE,
                WatchFaceLayer.ALL_WATCH_FACE_LAYERS,
                null
            ),
            editorSession.previewReferenceInstant,
            editorSession.complicationsPreviewData.value
        )
    }

    /* Creates a new bitmap render of the updated watch face and passes it along (with all the other
 * updated values) to the Activity to render.
 */
    private fun createWatchFacePreview(
        userStyle: UserStyle,
        complicationsPreviewData: Map<Int, ComplicationData>
    ): UserStylesAndPreview {
        val bitmap = editorSession.renderWatchFaceToBitmap(
            RenderParameters(
                DrawMode.INTERACTIVE,
                WatchFaceLayer.ALL_WATCH_FACE_LAYERS,
                null
            ),
            editorSession.previewReferenceInstant,
            complicationsPreviewData
        )

        val colorStyle = userStyle[colorStyleKey] as UserStyleSetting.ListUserStyleSetting.ListOption

        return UserStylesAndPreview(
            colorStyleId = colorStyle.id.toString(),
            previewImage = bitmap
        )
    }


    fun setColorStyle(newColorStyleId: String) {
        val userStyleSettingList = editorSession.userStyleSchema.userStyleSettings
        // Loops over all UserStyleSettings (basically the keys in the map) to find the setting for
        // the color style (which contains all the possible options for that style setting).
        for (userStyleSetting in userStyleSettingList) {
            if (userStyleSetting.id == UserStyleSetting.Id(COLOR_STYLE_SETTING)) {
                val colorUserStyleSetting =
                    userStyleSetting as UserStyleSetting.ListUserStyleSetting

                // Loops over the UserStyleSetting.Option colors (all possible values for the key)
                // to find the matching option, and if it exists, sets it as the color style.
                for (colorOptions in colorUserStyleSetting.options) {
                    if (colorOptions.id.toString() == newColorStyleId) {
                        setUserStyleOption(colorStyleKey, colorOptions)
                        return
                    }
                }
            }
        }
    }


    fun setShapeStyle(newShapeStyleId: String) {
        val userStyleSettingList = editorSession.userStyleSchema.userStyleSettings
        // Loops over all UserStyleSettings (basically the keys in the map) to find the setting for
        // the color style (which contains all the possible options for that style setting).
        for (userStyleSetting in userStyleSettingList) {
            if (userStyleSetting.id == UserStyleSetting.Id(SHAPE_STYLE_SETTING)) {
                val shapeUserStyleSetting =
                    userStyleSetting as UserStyleSetting.ListUserStyleSetting

                // Loops over the UserStyleSetting.Option colors (all possible values for the key)
                // to find the matching option, and if it exists, sets it as the color style.
                for (shapeOptions in shapeUserStyleSetting.options) {
                    if (shapeOptions.id.toString() == newShapeStyleId) {
                        setUserStyleOption(shapeStyleKey, shapeOptions)
                        return
                    }
                }
            }
        }
    }

    // Saves User Style Option change back to the back to the EditorSession.
    // Note: The UI widgets in the Activity that can trigger this method (through the 'set' methods)
    // will only be enabled after the EditorSession has been initialized.
    private fun setUserStyleOption(
        userStyleSetting: UserStyleSetting,
        userStyleOption: UserStyleSetting.Option
    ) {
        Log.d(TAG, "setUserStyleOption()")
        Log.d(TAG, "\tuserStyleSetting: $userStyleSetting")
        Log.d(TAG, "\tuserStyleOption: $userStyleOption")

        // TODO: As of watchface 1.0.0-beta01 We can't use MutableStateFlow.compareAndSet, or
        //       anything that calls through to that (like MutableStateFlow.update) because
        //       MutableStateFlow.compareAndSet won't properly update the user style.
        val mutableUserStyle = editorSession.userStyle.value.toMutableUserStyle()
        mutableUserStyle[userStyleSetting] = userStyleOption
        editorSession.userStyle.value = mutableUserStyle.toUserStyle()
    }

    sealed class EditWatchFaceUiState {
        data class Success(val userStylesAndPreview: UserStylesAndPreview) : EditWatchFaceUiState()
        data class Loading(val message: String) : EditWatchFaceUiState()
        data class Error(val exception: Throwable) : EditWatchFaceUiState()
    }

    data class UserStylesAndPreview(
        val colorStyleId: String,
        val previewImage: Bitmap
    )

    companion object {
        private const val TAG = "WatchFaceConfigStateHolder"

        // To convert the double representing the arm length to valid float value in the range the
        // slider can support, we need to multiply the original value times 1,000.
        private const val MULTIPLE_FOR_SLIDER: Float = 1000f

        private fun multiplyByMultipleForSlider(lengthFraction: Double) =
            lengthFraction * MULTIPLE_FOR_SLIDER
    }

}