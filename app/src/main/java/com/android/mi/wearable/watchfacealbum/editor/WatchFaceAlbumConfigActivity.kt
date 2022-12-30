package com.android.mi.wearable.watchfacealbum.editor

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.android.mi.wearable.watchfacealbum.data.watchface.StyleIdAndResourceIds
import com.android.mi.wearable.watchface5.databinding.ActivityWatchFaceConfig5Binding
import com.android.mi.wearable.watchfacealbum.utils.BitmapTranslateUtils
import com.android.mi.wearable.watchfacealbum.utils.LEFT_COMPLICATION_ID
import com.android.mi.wearable.watchfacealbum.utils.RIGHT_COMPLICATION_ID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
interface IComplicationClick {
    fun onTopClick()
    fun onBottomClick()

    fun onStylePagerChange(isUp: Boolean)
    fun onColorPagerChange(isUp: Boolean)
}
class WatchFace5ConfigActivity : ComponentActivity(), IComplicationClick {
    private lateinit var binding: ActivityWatchFaceConfig5Binding
    private lateinit var currentColorId: String
    private var currentColorPosition = 0
    private var isFirst: Boolean = true
    var myAdapter: HorizontalPagerAdapter? = null
    var isSelected: Boolean = false
    private val stateHolder: WatchFaceConfigStateHolder by lazy {
    WatchFaceConfigStateHolder(
            lifecycleScope,
            this@WatchFace5ConfigActivity
        )
    }

    override fun onTopClick() {
        if (stateHolder.pageType == 1){
            stateHolder.setComplication(LEFT_COMPLICATION_ID)
        }
    }

    override fun onBottomClick() {
        if (stateHolder.pageType == 1){
            stateHolder.setComplication(RIGHT_COMPLICATION_ID)
        }
    }

    override fun onStylePagerChange(isUp: Boolean) {
        TODO("Not yet implemented")
    }


    override fun onColorPagerChange(isUp: Boolean){
        val colorStyleIdAndResourceIdsList = enumValues<StyleIdAndResourceIds>()
        currentColorPosition = if (isUp){
            if (currentColorPosition == 4) 2 else (currentColorPosition + 1)
        }else{
            if (currentColorPosition == 0) 0 else (currentColorPosition - 1)
        }
        val newColorStyle: StyleIdAndResourceIds = colorStyleIdAndResourceIdsList[currentColorPosition]
        stateHolder.setColorStyle(newColorStyle.id)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWatchFaceConfig5Binding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch(Dispatchers.Main.immediate) {
            stateHolder.uiState
                .collect { uiState: WatchFaceConfigStateHolder.EditWatchFaceUiState ->
                    when (uiState) {
                        is WatchFaceConfigStateHolder.EditWatchFaceUiState.Loading -> {
                            Log.d("TAG", "StateFlow Loading: ${uiState.message}")
                        }
                        is WatchFaceConfigStateHolder.EditWatchFaceUiState.Success -> {
                            Log.d("TAG", "StateFlow Success.")
                            updateWatchFacePreview(uiState.userStylesAndPreview)
                        }
                        is WatchFaceConfigStateHolder.EditWatchFaceUiState.Error -> {
                            Log.e("TAG", "Flow error: ${uiState.exception}")
                        }
                    }
                }
        }
    }

    private fun initHorizontalViewPager() {
        myAdapter = HorizontalPagerAdapter(listener = this, context = this)
        binding.pager.apply {
            offscreenPageLimit = 1
            adapter = myAdapter
        }
        binding.pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                stateHolder.pageType = position
                val isShowHighLayer:Boolean = position == 1
                binding.preview.vPreviewMask.visibility = if (isShowHighLayer) View.GONE else View.VISIBLE
                binding.preview.watchFaceBackground.setImageBitmap(stateHolder.createWatchFacePreview(isShowHighLayer))
            }
        })
    }

    fun onConfirmClick(view: View){
        isSelected = true
        onBackPressedDispatcher.onBackPressed()
    }

    private fun updateWatchFacePreview(
        userStylesAndPreview: WatchFaceConfigStateHolder.UserStylesAndPreview
    ) {
        binding.preview.watchFaceBackground.setImageBitmap(userStylesAndPreview.previewImage)
        if (isFirst){
            isFirst = false
            currentColorId = userStylesAndPreview.colorStyleId
            currentColorPosition = BitmapTranslateUtils.currentColorItemPosition(currentColorId)
            initHorizontalViewPager()
        }
    }

    override fun onPause() {
        if (!isSelected){
            stateHolder.setColorStyle(currentColorId)
        }
        super.onPause()
    }
}