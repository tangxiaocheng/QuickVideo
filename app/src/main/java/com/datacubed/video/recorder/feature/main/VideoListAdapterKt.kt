package com.datacubed.video.recorder.feature.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.datacubed.video.recorder.R
import com.datacubed.video.recorder.data.kotlin.VideoModel

class VideoListAdapterKt internal constructor(
        context: Context
) : RecyclerView.Adapter<VideoListAdapterKt.ItemViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var videoList = emptyList<VideoModel>()

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTv: TextView = itemView.findViewById(R.id.tv_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView: View = inflater.inflate(R.layout.saved_recordings_rv_item, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val current = videoList[position]
        holder.titleTv.text = current.title
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    fun setList(list: List<VideoModel>) {
        videoList = list
        notifyDataSetChanged()
    }
}