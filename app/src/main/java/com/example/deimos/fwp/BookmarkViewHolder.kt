package com.example.deimos.fwp


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

class BookmarkViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(inflater.inflate(R.layout.item_bookmark, parent, false)) {
    private var mTitleView: TextView? = null
    private var mDateView : TextView? = null



    init {
        mTitleView = itemView.findViewById(R.id.title)
        mDateView = itemView.findViewById(R.id.date)

    }

    fun bind(bookmark: Bookmark) {
        mTitleView?.text =" "+ bookmark.title
        mDateView?.text=bookmark.date

    }

}