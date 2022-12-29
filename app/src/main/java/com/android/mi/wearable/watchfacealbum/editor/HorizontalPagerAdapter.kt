package com.android.mi.wearable.watchface5.editor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.android.mi.wearable.watchface5.R

class HorizontalPagerAdapter (private val listener: IComplicationClick,val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mCurrentItem: Int = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0){
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_album_watch_face_config_style, parent, false)
            StyleViewHolder(itemView)
        }else{
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_watch_face_config_style, parent, false)
            ColorViewHolder(itemView)

        }
    }

    fun currentPagerItem(position: Int){
        mCurrentItem = position
        notifyItemChanged(0)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ColorViewHolder) {
//            holder.mViewPager.apply {
//                orientation = ViewPager2.ORIENTATION_VERTICAL
//                offscreenPageLimit = 4
//                val recyclerView = getChildAt(0) as RecyclerView
//                recyclerView.apply {
//                    val padding = resources.getDimensionPixelOffset(R.dimen.watch_face_view_pager_padding) +
//                            resources.getDimensionPixelOffset(R.dimen.watch_face_view_pager_padding)
//                    setPadding(padding,0,padding,0)
//                    clipToPadding = false
//                }
//            }
//            holder.mViewPager.adapter = holder.verticalAdapter
//            val compositePageTransformer = CompositePageTransformer()
//            compositePageTransformer.addTransformer(ScaleInTransformer())
//            compositePageTransformer.addTransformer(MarginPageTransformer(context.resources.getDimension(R.dimen.watch_face_view_pager_padding).toInt()))
//            holder.mViewPager.setPageTransformer(compositePageTransformer)
//            holder.mViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//                override fun onPageSelected(position: Int) {
//                    super.onPageSelected(position)
//                    listener.onPagerChange(position)
//                }
//            })
//            holder.mViewPager.currentItem = mCurrentItem
        } else {
            val complicationViewHolder = holder as StyleViewHolder
        }

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return 2
    }
    class ColorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    class StyleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }
}

class ScaleInTransformer : ViewPager2.PageTransformer {
    private val mMinScale = DEFAULT_MIN_SCALE
    override fun transformPage(view: View, position: Float) {
        view.elevation = -kotlin.math.abs(position)
        val pageWidth = view.width
        val pageHeight = view.height

        view.pivotY = (pageHeight / 2).toFloat()
        view.pivotX = (pageWidth / 2).toFloat()
        if (position < -1) {
            view.scaleX = mMinScale
            view.scaleY = mMinScale
            view.pivotX = pageWidth.toFloat()
        } else if (position <= 1) {
            if (position < 0) {
                val scaleFactor = (1 + position) * (1 - mMinScale) + mMinScale
                view.scaleX = scaleFactor
                view.scaleY = scaleFactor
                view.pivotX = pageWidth * (DEFAULT_CENTER + DEFAULT_CENTER * -position)
            } else {
                val scaleFactor = (1 - position) * (1 - mMinScale) + mMinScale
                view.scaleX = scaleFactor
                view.scaleY = scaleFactor
                view.pivotX = pageWidth * ((1 - position) * DEFAULT_CENTER)
            }
        } else {
            view.pivotX = 0f
            view.scaleX = mMinScale
            view.scaleY = mMinScale
        }
    }

    companion object {

        const val DEFAULT_MIN_SCALE = 0.85f
        const val DEFAULT_CENTER = 0.5f
    }
}