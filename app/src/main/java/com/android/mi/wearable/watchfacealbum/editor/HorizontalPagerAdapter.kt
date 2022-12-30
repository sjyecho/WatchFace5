package com.android.mi.wearable.watchfacealbum.editor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.android.mi.wearable.watchface5.R

class HorizontalPagerAdapter (private val listener: IComplicationClick, val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mCurrentItem: Int = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0){
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_album_watch_face_config_style, parent, false)
            ColorViewHolder(itemView)
        }else{
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_watch_face_config_complication, parent, false)
            ComplicationViewHolder(itemView)

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
            val complicationViewHolder = holder as ComplicationViewHolder
            complicationViewHolder.topBt.setOnClickListener{
                listener.onTopClick()
            }
            complicationViewHolder.bottomBt.setOnClickListener{
                listener.onBottomClick()
            }
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

    class ComplicationViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val topBt :Button = itemView.findViewById(R.id.left_complication)
        val bottomBt : Button = itemView.findViewById(R.id.right_complication)
    }

    class StyleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }
}