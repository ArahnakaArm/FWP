package com.example.deimos.fwp

import android.graphics.Movie
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

class VideoViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.itemvideo, parent, false)) {
    private var mTitleView: TextView? = null
    private var mDateView : TextView? = null



    init {
        mTitleView = itemView.findViewById(R.id.videotopic)
        mDateView = itemView.findViewById(R.id.date)

    }

    fun bind(video: Video) {
        mTitleView?.text = video.name
        mDateView?.text=video.username

    }

}