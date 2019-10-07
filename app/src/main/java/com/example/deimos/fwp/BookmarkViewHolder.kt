package com.example.deimos.fwp


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

class BookmarkViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.itembookmark, parent, false)) {
    private var mTitleView: TextView? = null
    private var mDateView : TextView? = null



    init {
        mTitleView = itemView.findViewById(R.id.title)
        mDateView = itemView.findViewById(R.id.date)

    }

    fun bind(bookmark: Bookmark) {
        mTitleView?.text = bookmark.title
        mDateView?.text=bookmark.date

    }

}