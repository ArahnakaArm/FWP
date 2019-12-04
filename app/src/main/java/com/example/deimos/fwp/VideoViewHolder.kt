package com.example.deimos.fwp

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

class VideoViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(inflater.inflate(R.layout.item_video, parent, false)) {
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